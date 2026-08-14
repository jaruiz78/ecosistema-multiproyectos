"""
Arquitectura y especificación formal para deprecate_skills.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import os

warning = """> [!WARNING]
> **DEPRECATED FOR STANDALONE USE.** 
> Use this skill ONLY to design the mathematical sub-matrix and inject it as a node into the `tensor_gnn_core.py` Unified Model. Do NOT run isolated simulations.

"""

files_to_deprecate = [
    '/home/jaruiz/.gemini/config/skills/skill-mesa-abm-scaffold/SKILL.md',
    '/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/_agent/skills/skill-airport-queue-des/SKILL.md',
    '/home/jaruiz/.gemini/config/skills/h3-surge-calculator/SKILL.md'
]

for filepath in files_to_deprecate:
    if os.path.exists(filepath):
        with open(filepath, 'r') as f:
            content = f.read()
        
        # Add warning after frontmatter if it exists, or at the top
        if content.startswith('---'):
            parts = content.split('---', 2)
            if len(parts) >= 3:
                new_content = '---' + parts[1] + '---\n\n' + warning + parts[2]
            else:
                new_content = warning + content
        else:
            new_content = warning + content
            
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Updated {filepath}")
    else:
        print(f"Not found: {filepath}")
