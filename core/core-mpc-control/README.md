# core-mpc-control — Motor de Control Predictivo Basado en Modelos (MPC)

Módulo algorítmico puro en **Java 25 LTS** para la resolución en tiempo real de problemas de control óptimo cuadrático con horizonte deslizante (Linear-Quadratic Model Predictive Control).

---

## 1. Fundamentos Teóricos y Formulación Matemática

El motor resuelve problemas de optimización convexa con restricciones de caja sobre actuadores y variables de estado:

\[ \min_{u_0, \dots, u_{H-1}} \sum_{k=0}^{H-1} \left[ (x_k - x_{\text{ref}})^T Q (x_k - x_{\text{ref}}) + u_k^T R u_k \right] + (x_H - x_{\text{ref}})^T Q_f (x_H - x_{\text{ref}}) \]

sujeto a la dinámica del sistema:
\[ x_{k+1} = A x_k + B u_k \]
y restricciones físicas de saturación:
\[ u_{\min} \le u_k \le u_{\max}, \quad x_{\min} \le x_k \le x_{\max} \]

### Algoritmo de Solución
- Implementa **Descenso de Gradiente Proyectado Acelerado** en el espacio de trayectorias con propagación hacia atrás del estado adjunto de Lagrange \(\lambda_k\):
  \[ \lambda_k = 2 Q (x_k - x_{\text{ref}}) + A^T \lambda_{k+1}, \quad \nabla_{u_k} J = 2 R u_k + B^T \lambda_{k+1} \]
- Complejidad temporal: \(O(H \cdot n \cdot m)\) por iteración.

---

## 2. Casos de Uso y Sinergias

- **`SaaSRegantes`:** Despacho óptimo de bombeo solar minimizando la compra de energía de red.
- **`ProyectoVPP`:** Modulación de potencia activa/reactiva en sistemas BESS manteniendo el estado de carga (SoC) entre límites de seguridad.
- **`ProyectoLogistica`:** Control de velocidad y espaciado de flotas autónomas.

---

## 3. Pruebas y Certificación

```bash
mvn clean test
```
