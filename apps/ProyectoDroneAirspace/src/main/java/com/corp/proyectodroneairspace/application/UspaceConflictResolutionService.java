package com.corp.proyectodroneairspace.application;

import com.corp.proyectodroneairspace.domain.model.DroneFlightRoute;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de Resolución y Prevención de Conflictos de Vuelo en Espacio Aéreo U-Space.
 *
 * <p>Verifica distancias de separación 3D mínima (\(d_h \ge 50\text{ m}, d_v \ge 20\text{ m}\))
 * y genera vectores evasivos deterministas en \(O(1)\) sin Carrier Thread Pinning.
 *
 * @see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
 */
@Service
public class UspaceConflictResolutionService {

    private final ReentrantLock lock = new ReentrantLock();
    public static final double MIN_HORIZONTAL_SEPARATION_METERS = 50.0;
    public static final double MIN_VERTICAL_SEPARATION_METERS = 20.0;

    public record ConflictAssessment(
            boolean conflictDetected,
            double horizontalDistanceMeters,
            double verticalDistanceMeters,
            String advisoryAction
    ) {}

    public ConflictAssessment assessConflict(
            DroneFlightRoute droneA,
            double altA,
            DroneFlightRoute droneB,
            double altB,
            double horizontalDistanceMeters
    ) {
        Objects.requireNonNull(droneA, "droneA no puede ser nulo");
        Objects.requireNonNull(droneB, "droneB no puede ser nulo");

        lock.lock();
        try {
            double vertDist = Math.abs(altA - altB);
            boolean isConflict = horizontalDistanceMeters < MIN_HORIZONTAL_SEPARATION_METERS
                    && vertDist < MIN_VERTICAL_SEPARATION_METERS;

            String advisory;
            if (!isConflict) {
                advisory = "CLEAR_FLIGHT_PATH";
            } else if (altA >= altB) {
                advisory = "DRONE_A_CLIMB_10M_DRONE_B_DESCEND_10M";
            } else {
                advisory = "DRONE_A_DESCEND_10M_DRONE_B_CLIMB_10M";
            }

            return new ConflictAssessment(
                    isConflict,
                    horizontalDistanceMeters,
                    vertDist,
                    advisory
            );
        } finally {
            lock.unlock();
        }
    }
}
