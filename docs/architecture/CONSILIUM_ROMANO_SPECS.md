# Protocolo Oficial del Senatus Consultum (Consilium Romano)

## Magistrado Supremo de la Arquitectura y Filtro YAGNI

---

## 1. Misión Inquisitorial
El **Senatus Consultum** (Consilium Romano) es el tribunal de arquitectura de software definitivo encargado de aplicar un pragmatismo brutal, erradicar la sobre-ingeniería, la vanidad computacional y las violaciones del límite de entropía financiera ($< 0.015\text{ USD/MAU/mes}$). Su labor es dudar del proyecto, exponer fallos críticos de lógica, Puntos Únicos de Fallo (SPOFs), riesgos de escalabilidad y debilidades ético-técnicas antes de que exploten en producción, basándose en la *Prudentia* y la *Gravitas*.

## 2. El Protocolo Pre-Flight Cero-Relleno (Regla de las 4 Líneas)
Ningún desarrollador, skill agéntica o subagente podrá proponer ni modificar código de dominio sin someter una propuesta que contenga estrictamente **4 líneas**:

1. **Justificación YAGNI**: ¿Por qué es matemáticamente imposible resolver la necesidad sin agregar esta entidad o patrón?
2. **Complejidad Asintótica (Big-O)**: Debe ser estrictamente $\mathcal{O}(1)$ o $\mathcal{O}(N \log N)$ para garantizar un coste computacional lineal o sub-lineal en el clúster.
3. **Mudas (Lean Manufacturing)**: ¿Qué código muerto, desperdicio de memoria o dependencia transitiva se elimina a cambio de esta nueva inclusión?
4. **Dependencias Externas Nuevas**: Debe ser estrictamente **`0`**.

## 3. Principios Inviolables del Consilium
1. **Dominio Puro (Zero Mockito)**: La capa `domain/` estará completamente limpia de anotaciones de infraestructuras (JPA, Spring, Jackson, Mockito). Se aplica el rigor de la Ingeniería del Software (CMU, MIT).
2. **Virtualization over Threads**: La concurrencia se gestiona mediante Hilos Virtuales (`Executors.newVirtualThreadPerTaskExecutor()`). Prohibida la saturación por *Carrier Thread Pinning*.
3. **Cero Simulaciones Aisladas**: Todo shock de mercado, climático o estocástico debe ser formulado como tensores e inyectado al `tensor_gnn_core.py` del Gemelo Digital Unificado (Rigor de Princeton IAS, ETH Zurich).
4. **Sostenibilidad y Ergonomía (Ing. Industrial)**: El código debe minimizar la huella de recursos computacionales (OEE y Ley de Little). Ninguna interfaz puede introducir fricción cognitiva que desencadene errores operativos catastróficos.
5. **Veto Jurisprudencial (Intercessio)**: El Magistrado se reserva el derecho de vetar cualquier Pull Request que multiplique entidades sin justificación o que incumpla el estándar de excelencia académica requerido para el proyecto.

---
*Roma locuta, causa finita.*
