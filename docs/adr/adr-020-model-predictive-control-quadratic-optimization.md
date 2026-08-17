# ADR-020: Control Predictivo Basado en Modelos (MPC) y Optimización Cuadrática Acelerada

## Estado
**Aceptado**

## Contexto
El control de actuadores en tiempo real para redes de distribución de agua (`SaaSRegantes`), microredes eléctricas (`ProyectoEnergia`, `ProyectoVPP`), electrolizadores de hidrógeno (`ProyectoHidrogeno`) y gestión de drones (`ProyectoDroneAirspace`) requiere resolver problemas de control óptimo restringido con horizontes temporales móviles en \(< 50\text{ ms}\) sin recurrir a solvers externos pesados ni librerías nativas con fugas de memoria.

## Decisión
Implementar [`core-mpc-control`](file:///home/jaruiz/Desarrollo/core/core-mpc-control) como motor algorítmico puro en Java 25:
1. **Linear Quadratic MPC**:
   Minimización de la función de coste multietapa:
   \[
   J = \sum_{k=0}^{H-1} \left( (x_k - x_{\text{ref}})^T Q (x_k - x_{\text{ref}}) + u_k^T R u_k \right) + (x_H - x_{\text{ref}})^T Q_f (x_H - x_{\text{ref}})
   \]
   Sujeto a \(x_{k+1} = A x_k + B u_k\) y restricciones de caja \(u_{\min} \le u_k \le u_{\max}\), \(x_{\min} \le x_k \le x_{\max}\).
2. **Descenso de Gradiente Proyectado y Propagación Adjunta**:
   Cálculo del gradiente exacto mediante co-estados adyacentes (\(\lambda_k = 2 Q (x_k - x_{\text{ref}}) + A^T \lambda_{k+1}\)) en \(O(H \cdot n \cdot m)\).
3. **Cero Dependencias Nativas**:
   Implementación analítica pura con paralelización nativa sobre Virtual Threads.

## Consecuencias
- **Positivas**:
  - Tiempo de resolución determinista \(< 2\text{ ms}\) para horizontes \(H=10\).
  - Integración nativa sin dependencias JNI/C++ en contenedores Cloud Run y emuladores edge.
- **Negativas**:
  - Requiere linealización de sistemas fuertemente no lineales en torno al punto de operación.
