# 🏛️ Kata 09: El Tribunal del Consilium Romano 3.0 (IA Adversarial)

## 1. El Problema: El Sesgo de Confirmación Agéntico
Cuando un único Agente IA o LLM escribe, revisa y aprueba su propio código, sufre invariablemente de "sesgo de confirmación algorítmica" y "ceguera de código". El LLM tenderá a aprobar sus propios atajos, ignorar vulnerabilidades de inyección y olvidar las métricas FinOps (Coste por MAU). 

## 2. La Solución: Oposición Dialéctica (Adversarial AI)
Para emular el rigor de las academias de élite y blindar la base de código, no usamos un único modelo omnisciente. Implementamos el **Consilium Romano 3.0**: un tribunal de tres Magistrados IA (modelos locales en Ollama) forzados a actuar en hostilidad arquitectónica entre sí antes de realizar un `git merge`.

### Los Tres Magistrados (Arquitectura Local Ollama)
1. **El Inquisidor (`deepseek-r1:8b`)**: Evalúa la Lógica de Hoare y la Complejidad Matemática \(\mathcal{O}(N)\). Su único objetivo es buscar cuellos de botella algorítmicos.
2. **El Censor Morum (`qwen2.5-coder:7b`)**: Vigila la pureza del código (Java 25, Hexagonal, Cero Carrier Pinning, Cero Mockito). Destrozará el PR si detecta dependencias circulares.
3. **El Praetor FinOps (`gemma3:4b`)**: Evalúa el impacto económico y SRE. Solo le importa si el cambio romperá la regla de $< 0.015$ USD/MAU/mes en BigQuery o Cloud Run.

## 3. Implementación Práctica del Tribunal

Cuando un desarrollador o agente completa un módulo, invoca el script de pre-commit `consilium_romano_tribunal.py`. El script realiza un **Fan-Out** (envía el diff simultáneamente a los 3 modelos).

### Flujo de Votación
- Cada modelo analiza el diff de forma independiente.
- Si un solo magistrado emite un **VETO**, el PR es rechazado automáticamente.
- El desarrollador recibe un reporte con las citas de las fuentes primarias de la *Universidad Privada* justificando el rechazo.

## 4. Ejercicio Práctico (Método Feynman)

> **Reto Feynman**: ¿Por qué usamos tres IAs pequeñas locales peleando entre sí en lugar de una IA gigante de pago?

*Respuesta Feynman*: "Imagina que eres un juez ciego tratando de averiguar si un plano para hacer un puente es seguro. Si solo le preguntas al arquitecto que lo dibujó, te dirá que es perfecto. Si le preguntas al arquitecto (para ver cómo se ve), a un inspector cascarrabias de Hacienda (para ver cuánto cuesta) y a un ingeniero miedoso (para ver si se cae), y los tres se ponen de acuerdo, sabes que el puente es seguro. Además, pagarles un sueldo a tres inspectores locales gratis es mejor que alquilar al inspector más caro del mundo para cada puente."
