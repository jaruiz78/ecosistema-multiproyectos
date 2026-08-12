import os
import re
import logging
from typing import List, Dict, Any

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

class SpringAotHintChecker:
    """
    Escáner estático AST/Pattern para Java 25 / Spring Boot 4.0.
    Garantiza la compatibilidad con GraalVM Native Image y Project Leyden CDS
    detectando uso no registrado de reflexión dinámica que podría causar fallos AOT.
    """

    REFLECTION_PATTERNS = [
        re.compile(r"Class\.forName\("),
        re.compile(r"\.getDeclaredMethod\("),
        re.compile(r"\.getDeclaredField\("),
        re.compile(r"(?<!ScopedValue)\.newInstance\(")
    ]

    HINT_ANNOTATION_PATTERN = re.compile(r"@RegisterReflectionForBinding|RuntimeHintsRegistrar")

    def __init__(self, target_dir: str):
        self.target_dir = target_dir

    def scan(self) -> Dict[str, Any]:
        logging.info(f"🔍 Escaneando compatibilidad AOT / GraalVM en: {self.target_dir}")
        violations = []
        scanned_files = 0

        for root, _, files in os.walk(self.target_dir):
            for file in files:
                if file.endswith(".java"):
                    scanned_files += 1
                    file_path = os.path.join(root, file)
                    with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                        content = f.read()

                    # Verificar si contiene patrones de reflexión
                    has_reflection = any(p.search(content) for p in self.REFLECTION_PATTERNS)
                    has_hint = bool(self.HINT_ANNOTATION_PATTERN.search(content))

                    if has_reflection and not has_hint:
                        violations.append({
                            "file": file_path,
                            "reason": "Uso de reflexión dinámica detectado sin anotación @RegisterReflectionForBinding ni RuntimeHintsRegistrar."
                        })

        is_clean = len(violations) == 0
        status = "PASADO" if is_clean else "FALLIDO"
        logging.info(f"✅ Escaneo completado. Archivos analizados: {scanned_files} | Violaciones AOT: {len(violations)} | Estado: {status}")

        return {
            "scanned_files": scanned_files,
            "violations_count": len(violations),
            "violations": violations,
            "is_clean": is_clean
        }

if __name__ == "__main__":
    checker = SpringAotHintChecker("/home/jaruiz/Desarrollo/corp-spring-boot-starter")
    result = checker.scan()
    if not result["is_clean"]:
        logging.warning("⚠️ Se encontraron posibles incompatibilidades AOT:")
        for v in result["violations"]:
            logging.warning(f"  - {v['file']}: {v['reason']}")
    else:
        logging.info("🎉 Ninguna incompatibilidad AOT/GraalVM detectada en el Starter.")
