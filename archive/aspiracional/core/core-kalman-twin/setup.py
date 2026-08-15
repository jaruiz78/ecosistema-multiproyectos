"""
Arquitectura y especificación formal para setup.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/03_asimilacion_de_datos_enkf.md
- Referencia Académica: Evensen (2003) Sequential Data Assimilation with EnKF (JGR)
"""
from setuptools import setup, find_packages
setup(
    name='core-kalman-twin',
    version='1.0.0',
    packages=find_packages(),
)
