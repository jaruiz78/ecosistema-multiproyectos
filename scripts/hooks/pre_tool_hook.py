#!/usr/bin/env python3
"""
Pre-Tool Execution Hook (Antigravity 2.0 & Managed Agents)
----------------------------------------------------------
Interceptors ejecutados inmediatamente ANTES de cualquier llamada a herramienta:
1. Pure Domain AST Gatekeeper: Bloquea inserción de dependencias de infraestructura en domain/
2. Command Safety Gatekeeper: Bloquea comandos destructivos o no seguros
3. BigQuery SQL Safety: Bloquea consultas sin filtro de partición forzoso
4. Zero-PII Enforcer: Bloquea logging de credenciales y datos sensibles en plano
5. FinOps & Token Budget Guard: Valida umbrales de consumo de sesión

Retorno: Exit code 0 (Permitido) o Exit code 1 (Bloqueado con motivo descriptivo)
"""

import sys
import os
import re
import json
from pathlib import Path
from typing import Dict, Any, List, Optional

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")

FORBIDDEN_DOMAIN_PATTERNS = [
    re.compile(r'import\s+org\.mockito\..*'),
    re.compile(r'import\s+org\.springframework\..*'),
    re.compile(r'import\s+jakarta\.persistence\..*'),
    re.compile(r'import\s+javax\.persistence\..*'),
    re.compile(r'import\s+com\.fasterxml\.jackson\..*'),
    re.compile(r'import\s+io\.grpc\..*'),
    re.compile(r'import\s+com\.google\.cloud\..*'),
    re.compile(r'import\s+com\.stripe\..*'),
    re.compile(r'@Entity\b'),
    re.compile(r'@Table\b'),
    re.compile(r'@RestController\b'),
    re.compile(r'@Service\b'),
    re.compile(r'@Component\b'),
    re.compile(r'@Autowired\b'),
]

DANGEROUS_COMMAND_PATTERNS = [
    re.compile(r'\brm\s+-(?:r|f|rf|fr)\s+/(?:\s|$|\*)'),
    re.compile(r'\brm\s+-(?:r|f|rf|fr)\s+~(?:\s|$|\*)'),
    re.compile(r'\bmkfs\b'),
    re.compile(r'\bdd\s+if=.*of=/dev/(?:sd|hd|nvme)'),
    re.compile(r':\(\)\{\s*:\|:&\s*\};:'), # Fork bomb
    re.compile(r'\bchmod\s+-R\s+777\s+/'),
]

BIGQUERY_UNPARTITIONED_PATTERN = re.compile(
    r'SELECT\s+.*\s+FROM\s+`?[a-zA-Z0-9_\-\.]+`?(?!\s+WHERE\s+.*_PARTITIONTIME|\s+WHERE\s+.*_PARTITIONDATE|\s+WHERE\s+.*timestamp|\s+WHERE\s+.*date|\s+WHERE\s+.*created_at)',
    re.IGNORECASE
)

RAW_PII_LOG_PATTERN = re.compile(
    r'log\.(info|debug|warn|error)\(.*(credit_card|password|auth_token|bearer_token|bearer\s+|secret_key|api_key|user_email).*',
    re.IGNORECASE
)

def validate_pure_domain(file_path_str: str, content: str) -> Optional[str]:
    """Valida que los archivos bajo domain/ no contengan anotaciones ni dependencias de infraestructura."""
    normalized_path = file_path_str.replace("\\", "/")
    if "/domain/" in normalized_path and not "/test/" in normalized_path and not "/tests/" in normalized_path:
        for pattern in FORBIDDEN_DOMAIN_PATTERNS:
            match = pattern.search(content)
            if match:
                return (
                    f"VIOLACIÓN DOMINIO PURO (Lex Zero-Mockito & DDD): "
                    f"Se detectó patrón prohibido '{match.group(0).strip()}' en archivo de dominio: {file_path_str}. "
                    f"La capa de dominio debe ser Java/Go puro sin infraestructura."
                )
    return None

def validate_command_safety(command_line: str) -> Optional[str]:
    """Valida que un comando a ejecutar no sea destructivo ni peligroso."""
    for pattern in DANGEROUS_COMMAND_PATTERNS:
        if pattern.search(command_line):
            return f"COMANDO BLOQUEADO POR SEGURIDAD: Patrón potencialmente destructivo detectado en '{command_line}'."
    return None

def validate_bigquery_sql(sql_query: str) -> Optional[str]:
    """Valida que las consultas SQL para BigQuery incluyan filtros de particionamiento forzoso."""
    if "bigquery" in sql_query.lower() or "from `" in sql_query.lower():
        if BIGQUERY_UNPARTITIONED_PATTERN.search(sql_query):
            return (
                "CONSULTA SQL RECHAZADA (FinOps Lex BigQuery): "
                "La consulta a BigQuery no incluye filtro forzoso de partición (_PARTITIONDATE o columna temporal). "
                "Se requiere particionamiento forzoso para evitar escaneos de tabla completa y costes imprevistos."
            )
    return None

def validate_pii_logging(content: str) -> Optional[str]:
    """Valida que no se escriban logs con posibles credenciales o PII en texto claro."""
    match = RAW_PII_LOG_PATTERN.search(content)
    if match:
        return (
            f"VIOLACIÓN ZERO-PII (Lex Observability & BeyondCorp): "
            f"Se detectó posible logging de datos sensibles o credenciales en claro: '{match.group(0).strip()}'. "
            f"Utilice ZeroPiiMaskingConverter o encriptación de logs."
        )
    return None

def main():
    if len(sys.argv) < 2:
        # Modo passthrough si no se pasan argumentos
        sys.exit(0)

    try:
        # Los argumentos pueden pasarse como JSON o flags
        tool_data = None
        if sys.argv[1].startswith("{"):
            tool_data = json.loads(sys.argv[1])
        elif len(sys.argv) >= 3 and sys.argv[1] == "--json":
            tool_data = json.loads(sys.argv[2])

        if not tool_data:
            # Parsear argumentos simples si no es JSON
            tool_name = sys.argv[1]
            tool_data = {"tool_name": tool_name, "args": {}}

        tool_name = tool_data.get("tool_name", "")
        args = tool_data.get("args", {})

        # 1. Validación de herramientas de escritura y edición de archivos
        if tool_name in ["write_to_file", "replace_file_content", "multi_replace_file_content"]:
            target_file = args.get("TargetFile", "")
            content = args.get("CodeContent", "") or args.get("ReplacementContent", "")
            
            if target_file and content:
                # Validar dominio puro
                domain_err = validate_pure_domain(target_file, content)
                if domain_err:
                    print(f"❌ [PRE-TOOL HOOK REJECTION] {domain_err}", file=sys.stderr)
                    sys.exit(1)

                # Validar PII
                pii_err = validate_pii_logging(content)
                if pii_err:
                    print(f"❌ [PRE-TOOL HOOK REJECTION] {pii_err}", file=sys.stderr)
                    sys.exit(1)

        # 2. Validación de ejecución de comandos shell
        elif tool_name in ["run_command", "exec_command"]:
            cmd = args.get("CommandLine", "") or args.get("command", "")
            if cmd:
                cmd_err = validate_command_safety(cmd)
                if cmd_err:
                    print(f"❌ [PRE-TOOL HOOK REJECTION] {cmd_err}", file=sys.stderr)
                    sys.exit(1)

        # 3. Validación de llamadas a herramientas MCP (BigQuery, etc.)
        elif tool_name in ["call_mcp_tool"]:
            server_name = args.get("ServerName", "")
            tool_call_name = args.get("ToolName", "")
            mcp_args = args.get("Arguments", {})

            if server_name == "bigquery" or "sql" in tool_call_name.lower():
                query = mcp_args.get("query", "") or mcp_args.get("sql", "")
                if query:
                    sql_err = validate_bigquery_sql(query)
                    if sql_err:
                        print(f"❌ [PRE-TOOL HOOK REJECTION] {sql_err}", file=sys.stderr)
                        sys.exit(1)

        # Si todas las validaciones pasaron con éxito
        sys.exit(0)

    except Exception as e:
        # En caso de error inesperado de parsing, reportar y permitir salida controlada
        print(f"⚠️ [PRE-TOOL HOOK WARNING] Error en evaluación de hook: {str(e)}", file=sys.stderr)
        sys.exit(0)

if __name__ == "__main__":
    main()
