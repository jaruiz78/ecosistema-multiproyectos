# ADR 005: Cadena de Suministro Segura SLSA L3 y Firmas Criptográficas Sigstore/Cosign

## Estado
Aprobado (Consilium Romano)

## Contexto
Proteger el ecosistema contra ataques a la cadena de suministro de software (inyección de dependencias maliciosas, modificaciones no autorizadas en contenedores).

## Decisión
1. Exigir atestación **SLSA Nivel 3 (L3)** en todos los pipelines de compilación.
2. Generar un SBOM (Software Bill of Materials) en formato CycloneDX/SPDX para cada imagen de contenedor.
3. Firmar todos los artefactos y contenedores Cloud Run con **Sigstore/Cosign** antes del despliegue en producción.

## Consecuencias
* **Positivas:** Inmutabilidad demostrable y trazabilidad forense de cada despliegue.
* **Negativas:** Añade una etapa de firma y verificación de claves en el ciclo de CI/CD.
