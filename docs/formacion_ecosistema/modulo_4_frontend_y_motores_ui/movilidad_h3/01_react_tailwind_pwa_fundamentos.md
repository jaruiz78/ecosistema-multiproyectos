# Módulo 4 - Lección 1: Frontend Moderno: React 19, Tailwind CSS (OKLCH), PWA & WCAG 2.2 AA

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es React 19 y por qué lo usamos?
React es una biblioteca de Javascript para construir pantallas webs uniendo pequeñas piezas reutilizables llamadas **Componentes** (como si fueran bloques de LEGO).

### Espacio de Color OKLCH en Tailwind CSS
Tradicionalmente los colores en CSS se definían en RGB (`rgb(255, 0, 0)`) o HSL. Sin embargo, HSL distorsiona la percepción del brillo (el amarillo parece mucho más brillante que el azul aunque tengan el mismo valor de luminosidad). **OKLCH** resuelve esto, garantizando que los modos oscuro y claro tengan transiciones de color perfectas y legibles.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Cliente Web (Navegador PWA)
        UI[React 19 Functional Component]
        HOOKS[React State / Context]
        SW[Service Worker Cache IndexedDB]
    end

    subgraph Backend Cloud
        API[API REST Cloud Run]
    end

    UI --> HOOKS
    HOOKS --> SW
    SW -->|Si hay Red| API
    SW -->|Si Offline| CACHE[Data Cache Local]
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

```tsx
import React, { useState } from 'react';

interface IrrigationProps {
  plotId: string;
  initialActive: boolean;
}

export const IrrigationControl: React.FC<IrrigationProps> = ({ plotId, initialActive }) => {
  const [active, setActive] = useState(initialActive);

  return (
    <div className="p-4 rounded-xl bg-[var(--color-surface)] text-[var(--color-text)] border border-slate-700/20 shadow-md">
      <h3 className="text-lg font-bold mb-2">Parcela Riego #{plotId}</h3>
      <button
        id={`btn-toggle-${plotId}`}
        onClick={() => setActive(!active)}
        aria-pressed={active}
        aria-label={`Válvula de riego parcela ${plotId}`}
        className={`px-4 py-2 rounded-lg transition-colors focus-visible:ring-2 ${
          active ? 'bg-emerald-600 text-white' : 'bg-rose-600 text-white'
        }`}
      >
        {active ? 'Válvula Abierta' : 'Válvula Cerrada'}
      </button>
    </div>
  );
};
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Requisitos WCAG 2.2 AA (a11y) & Layout Metrics

| Métricas Web / a11y | Límite Objetivo | Regla CSS / HTML |
| :--- | :--- | :--- |
| **Contraste de Color** | \(\ge 4.5:1\) | Verificado con espacio de color OKLCH |
| **CLS (Cumulative Layout Shift)** | \(< 0.1\) | `content-visibility: auto` + dimensiones de imagen explícitas |
| **INP (Interaction to Next Paint)**| \(< 200 \text{ ms}\) | Transiciones CSS desacopladas de hilos pesados de JS |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Olvidar incluir etiquetas `aria-label` en botones interactivos que solo contienen iconos**:
   * *Síntoma*: Los lectores de pantalla para usuarios con discapacidad visual leen "Botón sin nombre", violando WCAG 2.2 AA.
   * *Solución*: Añade siempre un atributo `aria-label="Descripción"` a todo elemento interactivo.
