package com.corp.core.robotics;

import java.io.Serializable;
import java.util.Objects;

/**
 * Modelo Analítico: SpecialEuclideanTransformation (Grupos de Lie SE(3) y Rotaciones SO(3)).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record SpecialEuclideanTransformation(
        String transformId,
        double[] translationVector, // [x, y, z]
        double[] rotationQuaternion, // [qw, qx, qy, qz]
        double geodesicDistanceToOrigin
) implements Serializable {

    public SpecialEuclideanTransformation {
        Objects.requireNonNull(transformId, "transformId no puede ser nulo");
        Objects.requireNonNull(translationVector, "translationVector no puede ser nulo");
        Objects.requireNonNull(rotationQuaternion, "rotationQuaternion no puede ser nulo");
    }

    public static SpecialEuclideanTransformation fromPose(
            String id,
            double x, double y, double z,
            double qw, double qx, double qy, double qz
    ) {
        double transDist = Math.sqrt(x * x + y * y + z * z);
        // Ángulo de rotación geodésico theta = 2 * acos(|qw|)
        double rotDist = 2.0 * Math.acos(Math.min(1.0, Math.abs(qw)));
        double geodesicNorm = Math.sqrt(transDist * transDist + rotDist * rotDist);

        return new SpecialEuclideanTransformation(
                id,
                new double[]{x, y, z},
                new double[]{qw, qx, qy, qz},
                geodesicNorm
        );
    }
}
