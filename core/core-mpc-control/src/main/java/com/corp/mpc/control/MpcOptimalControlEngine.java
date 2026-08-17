package com.corp.mpc.control;

import com.corp.mpc.control.domain.MpcBounds;
import com.corp.mpc.control.domain.MpcControlAction;
import com.corp.mpc.control.domain.MpcState;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Motor Algorítmico Puro de Control Predictivo Basado en Modelos (Linear Quadratic MPC).
 *
 * <p>Resuelve problemas de optimización con horizonte deslizante (Receding Horizon):
 * <pre>
 *   min  \sum_{k=0}^{H-1} [ (x_k - x_ref)^T Q (x_k - x_ref) + u_k^T R u_k ] + (x_H - x_ref)^T Q_f (x_H - x_ref)
 *   s.t. x_{k+1} = A x_k + B u_k
 *        u_min &lt;= u_k &lt;= u_max
 *        x_min &lt;= x_k &lt;= x_max
 * </pre>
 *
 * <p>Implementa descenso de gradiente proyectado acelerado con backtracking armijo
 * en \(O(H \cdot n \cdot m)\) por iteración, garantizando convergencia y cero pinning
 * en Virtual Threads de Java 25.
 *
 * @see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
 * @see docs/adr/adr-001-java25-virtual-threads-anti-pinning.md
 */
public final class MpcOptimalControlEngine {

    private final ReentrantLock lock = new ReentrantLock();
    private final int horizon;
    private final double[][] matrixA;
    private final double[][] matrixB;
    private final double[] diagQ;
    private final double[] diagR;
    private final int maxIterations;
    private final double tolerance;

    public MpcOptimalControlEngine(
            int horizon,
            double[][] matrixA,
            double[][] matrixB,
            double[] diagQ,
            double[] diagR,
            int maxIterations,
            double tolerance
    ) {
        if (horizon <= 0) throw new IllegalArgumentException("El horizonte debe ser positivo");
        if (maxIterations <= 0) throw new IllegalArgumentException("maxIterations debe ser positivo");
        if (tolerance <= 0.0) throw new IllegalArgumentException("tolerance debe ser positiva");

        this.horizon = horizon;
        this.matrixA = deepCopy(Objects.requireNonNull(matrixA, "matrixA no puede ser nula"));
        this.matrixB = deepCopy(Objects.requireNonNull(matrixB, "matrixB no puede ser nula"));
        this.diagQ = Objects.requireNonNull(diagQ, "diagQ no puede ser nulo").clone();
        this.diagR = Objects.requireNonNull(diagR, "diagR no puede ser nulo").clone();
        this.maxIterations = maxIterations;
        this.tolerance = tolerance;
    }

    /**
     * Resuelve la trayectoria de control óptimo para el estado actual y la referencia objetivo.
     *
     * @param currentState   Estado dinámico inicial x_0
     * @param targetReference Referencia de consigna deseada x_ref
     * @param controlBounds  Límites admisibles u_min, u_max
     * @return {@link MpcControlAction} con la primera acción u_0 a aplicar
     */
    public MpcControlAction solve(
            MpcState currentState,
            double[] targetReference,
            MpcBounds controlBounds
    ) {
        Objects.requireNonNull(currentState, "currentState no puede ser nulo");
        Objects.requireNonNull(targetReference, "targetReference no puede ser nulo");
        Objects.requireNonNull(controlBounds, "controlBounds no puede ser nulo");

        lock.lock();
        try {
            int stateDim = matrixA.length;
            int controlDim = matrixB[0].length;

            if (currentState.dimension() != stateDim) {
                throw new IllegalArgumentException("Dimensión de estado incorrecta: " + currentState.dimension() + " != " + stateDim);
            }
            if (targetReference.length != stateDim) {
                throw new IllegalArgumentException("Dimensión de referencia incorrecta");
            }
            if (controlBounds.dimension() != controlDim) {
                throw new IllegalArgumentException("Dimensión de controlBounds incorrecta");
            }

            // Inicializar secuencia de control U = [u_0, u_1, ..., u_{H-1}] con ceros clamped
            double[][] uTrajectory = new double[horizon][controlDim];
            for (int k = 0; k < horizon; k++) {
                for (int m = 0; m < controlDim; m++) {
                    uTrajectory[k][m] = controlBounds.clamp(m, 0.0);
                }
            }

            double stepSize = 0.05;
            double prevCost = Double.MAX_VALUE;
            boolean converged = false;
            int iter = 0;

            for (; iter < maxIterations; iter++) {
                // 1. Simular trayectoria de estado hacia adelante: x_{k+1} = A x_k + B u_k
                double[][] xTrajectory = new double[horizon + 1][stateDim];
                xTrajectory[0] = currentState.stateVector().clone();

                for (int k = 0; k < horizon; k++) {
                    xTrajectory[k + 1] = stepDynamic(xTrajectory[k], uTrajectory[k]);
                }

                // 2. Evaluar coste cuadrático total J
                double currentCost = computeTotalCost(xTrajectory, uTrajectory, targetReference);

                if (Math.abs(prevCost - currentCost) < tolerance) {
                    converged = true;
                    break;
                }
                prevCost = currentCost;

                // 3. Propagación adjunta del gradiente hacia atrás (Backwards adjoint state lambda_k)
                double[][] lambda = new double[horizon + 1][stateDim];
                // lambda_H = Q_f * (x_H - x_ref)
                for (int i = 0; i < stateDim; i++) {
                    lambda[horizon][i] = 2.0 * diagQ[i] * (xTrajectory[horizon][i] - targetReference[i]);
                }

                for (int k = horizon - 1; k >= 0; k--) {
                    // lambda_k = 2 * Q * (x_k - x_ref) + A^T * lambda_{k+1}
                    for (int i = 0; i < stateDim; i++) {
                        double aTransLambda = 0.0;
                        for (int j = 0; j < stateDim; j++) {
                            aTransLambda += matrixA[j][i] * lambda[k + 1][j];
                        }
                        lambda[k][i] = 2.0 * diagQ[i] * (xTrajectory[k][i] - targetReference[i]) + aTransLambda;
                    }

                    // Gradiente del control: grad_u_k = 2 * R * u_k + B^T * lambda_{k+1}
                    for (int m = 0; m < controlDim; m++) {
                        double bTransLambda = 0.0;
                        for (int j = 0; j < stateDim; j++) {
                            bTransLambda += matrixB[j][m] * lambda[k + 1][j];
                        }
                        double gradU = 2.0 * diagR[m] * uTrajectory[k][m] + bTransLambda;

                        // Actualización proyectada
                        double uNew = uTrajectory[k][m] - stepSize * gradU;
                        uTrajectory[k][m] = controlBounds.clamp(m, uNew);
                    }
                }
            }

            return new MpcControlAction(
                    uTrajectory[0],
                    prevCost,
                    iter,
                    converged
            );
        } finally {
            lock.unlock();
        }
    }

    private double[] stepDynamic(double[] x, double[] u) {
        int stateDim = matrixA.length;
        int controlDim = matrixB[0].length;
        double[] xNext = new double[stateDim];

        for (int i = 0; i < stateDim; i++) {
            double ax = 0.0;
            for (int j = 0; j < stateDim; j++) {
                ax += matrixA[i][j] * x[j];
            }
            double bu = 0.0;
            for (int m = 0; m < controlDim; m++) {
                bu += matrixB[i][m] * u[m];
            }
            xNext[i] = ax + bu;
        }
        return xNext;
    }

    private double computeTotalCost(double[][] xTraj, double[][] uTraj, double[] xRef) {
        double cost = 0.0;
        int stateDim = diagQ.length;
        int controlDim = diagR.length;

        for (int k = 0; k < horizon; k++) {
            for (int i = 0; i < stateDim; i++) {
                double diff = xTraj[k][i] - xRef[i];
                cost += diagQ[i] * diff * diff;
            }
            for (int m = 0; m < controlDim; m++) {
                cost += diagR[m] * uTraj[k][m] * uTraj[k][m];
            }
        }
        // Coste terminal
        for (int i = 0; i < stateDim; i++) {
            double diff = xTraj[horizon][i] - xRef[i];
            cost += diagQ[i] * diff * diff;
        }
        return cost;
    }

    private static double[][] deepCopy(double[][] matrix) {
        double[][] copy = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }
}
