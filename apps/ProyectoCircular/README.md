# ProyectoCircular — Economía Circular, Pasaporte Digital de Producto (DPP) & Valorización de Biorresiduos

Módulo empresarial de economía circular, trazabilidad digital de ciclo de vida (Digital Product Passport) y valorización agronómica/energética de biorresiduos mediante cinética de Arrhenius y balance de biometano.

---

## 1. Arquitectura Hexagonal y Stack Técnico
- **Lenguaje:** Java 25 LTS (`record` inmutables, Virtual Threads Loom, Zero-Mockito).
- **Framework Base:** Spring Boot 4.1.0 (hereda de `corp-spring-boot-starter-parent`).
- **Aislamiento:** Multi-Tenancy Celular con discriminación por `tenantId`.

## 2. Agregados de Dominio y Modelos
1. **`Circular`**: Agregado raíz para operaciones de reciclaje y valorización.
2. **`DigitalProductPassport`**: Pasaporte digital con control de porcentaje de contenido reciclado (\(0\% \le \text{recycledContent} \le 100\%\)) y trazabilidad QR.
3. **`BiowasteBatch`**: Lotes de biorresiduos con masa orgánica, humedad (\(\%\)), potencial biometanogénico (\(\text{Nm}^3/\text{ton}\)) y relación C/N.

## 3. Servicios de Negocio
- **`BiowasteValorizerService`**:
  - Cinética de degradación anaerobia según la ecuación de Arrhenius:
    \[ k(T) = A \cdot \exp\left(-\frac{E_a}{R \cdot T}\right) \]
  - Cálculo estequiométrico de biometano (\(\text{Nm}^3\)), energía neta (\(\text{kWh}\)), digestato/compost (\(\text{kg}\)) y huella de carbono evitada (\(\text{kg CO}_2\)).

## 4. Pruebas y Certificación
- **100% Zero-Mockito**: Tests de integración y de dominio herméticos in-memory.
- **Ejecución:** `mvn clean test`