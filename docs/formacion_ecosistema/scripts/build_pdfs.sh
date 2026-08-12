#!/usr/bin/env bash
# Script de Compilación Automatizada de la Suite a Libros PDF (Pandoc + XeLaTeX)

set -euo pipefail

BASE_DIR="/home/jaruiz/Desarrollo/corp-spring-boot-starter/docs/formacion_ecosistema"
OUTPUT_DIR="${BASE_DIR}/dist_pdfs"

mkdir -p "${OUTPUT_DIR}"

echo "========================================================="
echo " Compilador Enciclopédico de Libros PDF (Pandoc Engine) "
echo "========================================================="

compile_module_pdf() {
    local module_dir="$1"
    local output_pdf="$2"
    local title="$3"

    echo "[+] Compilando ${title}..."
    if command -v pandoc &> /dev/null; then
        pandoc "${BASE_DIR}/${module_dir}"/*.md \
            -o "${OUTPUT_DIR}/${output_pdf}" \
            --from=markdown \
            --pdf-engine=xelatex \
            --toc \
            --highlight-style=tango \
            -V geometry:margin=1in \
            -V title="${title}"
        echo "    └─ Generado: ${OUTPUT_DIR}/${output_pdf}"
    else
        echo "    └─ [AVISO] Pandoc no está instalado en el sistema local. El contenido Markdown (.md) está preparado para su compilación."
    fi
}

compile_module_pdf "modulo_0_software_engineering" "Tomo_0_Software_Engineering_and_Compliance.pdf" "Tomo 0: Software Engineering, Toyota Kata & Compliance"
compile_module_pdf "modulo_1_backend_java_spring" "Tomo_1_Java25_SpringBoot4_OpenTelemetry.pdf" "Tomo 1: Java 25, Spring Boot 4.0 & OpenTelemetry"
compile_module_pdf "modulo_2_go_microservices" "Tomo_2_Go_Microservices_and_Resilience.pdf" "Tomo 2: High-Performance Go, Microservicios & Resiliencia"
compile_module_pdf "modulo_3_unified_twin_math" "Tomo_3_Unified_Twin_Physics_and_Math.pdf" "Tomo 3: Gemelo Digital, PDEs Clima & Matemáticas"
compile_module_pdf "modulo_4_frontend_mobility" "Tomo_4_React19_Flutter_H3_Mobility.pdf" "Tomo 4: Frontend React 19 & Flutter Movilidad H3"
compile_module_pdf "modulo_5_cloud_fintech" "Tomo_5_Cloud_Native_GCP_VertexAI_Stripe.pdf" "Tomo 5: Cloud Native GCP, BigQuery ML, Vertex AI & Stripe"
compile_module_pdf "modulo_6_zero_to_pro_blueprint" "Tomo_6_Zero_To_Pro_Blueprint.pdf" "Tomo 6: Blueprint Enciclopédico De Cero a PRO"

echo "========================================================="
echo " Proceso de Verificación y Compilación Finalizado "
echo "========================================================="
