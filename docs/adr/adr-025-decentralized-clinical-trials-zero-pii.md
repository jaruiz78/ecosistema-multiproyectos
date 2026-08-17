# ADR-025: Ensayos Clínicos Descentralizados, Cadena de Custodia Inmutable y Privacidad Zero-PII

## Estado
Aprobado - Agosto 2026

## Contexto
El vertical de salud (`ProyectoSalud`) maneja datos biomédicos de alta sensibilidad (ePHI/PII) en ensayos clínicos multicéntricos. La regulación (FDA 21 CFR Part 11, HIPAA, GDPR/LOPD) exige inmutabilidad en la trazabilidad de muestras y consentimiento informado sin exponer la identidad de los pacientes.

## Decisión
1. Desarrollar `ProyectoSalud` con arquitectura hexagonal pura, utilizando `ClinicalTrialComplianceLedgerService`.
2. Integrar `core-zkp-privacy` para emitir pruebas de rango Zero-Knowledge (ZKP) sobre parámetros de inclusión (ej. edad \(\ge 18\), biomarcadores dentro de rango) sin revelar el valor exacto ni el nombre del sujeto.
3. Almacenar la cadena de custodia biológica mediante digests SHA-256 encadenados (Merkle Tree / Hash Chain) en records inmutables.

## Consecuencias
- **Positivas**: Cero almacenamiento de PII en texto plano, auditoría matemática inalterable y cumplimiento total de estándares sanitarios internacionales.
- **Negativas**: Mayor sobrecarga computacional de generación de pruebas ZKP (\(\approx 0.5\text{ ms}\) por verificación).
