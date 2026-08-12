import os

projects = [
    ("ProyectoLogistica", "logistica-backend"),
    ("ProyectoTokenRWA", "tokenrwa-backend"),
    ("ProyectoB2G", "b2g-backend"),
    ("ProyectoEnergia", "energia-backend")
]

template = """steps:
  - name: "gcr.io/google.com/cloudsdktool/cloud-sdk"
    entrypoint: "bash"
    args: ["-c", "echo 'Iniciando despliegue de servicio ${_SERVICE_NAME}'"]
    id: "Log Service Info"

  - name: "gcr.io/cloud-builders/docker"
    args:
      [
        "build",
        "-f",
        "Dockerfile",
        "-t",
        "${_REGION}-docker.pkg.dev/${_PROJECT_ID}/<REPO_NAME>/${_IMAGE_NAME}:latest",
        ".",
      ]
    id: "Build JVM Image"

  - name: "gcr.io/cloud-builders/docker"
    args:
      [
        "push",
        "${_REGION}-docker.pkg.dev/${_PROJECT_ID}/<REPO_NAME>/${_IMAGE_NAME}:latest",
      ]
    id: "Push Image"
    waitFor: ["Build JVM Image"]

  - name: "gcr.io/google.com/cloudsdktool/cloud-sdk"
    entrypoint: "gcloud"
    args:
      [
        "run",
        "deploy",
        "${_SERVICE_NAME}",
        "--image",
        "${_REGION}-docker.pkg.dev/${_PROJECT_ID}/<REPO_NAME>/${_IMAGE_NAME}:latest",
        "--region",
        "${_REGION}",
        "--platform",
        "managed",
        "--cpu",
        "1.0",
        "--memory",
        "512Mi",
        "--concurrency",
        "100",
        "--cpu-boost",
        "--timeout",
        "600",
        "--no-allow-unauthenticated",
        "--min-instances",
        "<MIN_INSTANCES>",
        "--max-instances",
        "<MAX_INSTANCES>",
        "--service-account",
        "${_SA_EMAIL}",
        "--update-env-vars=SPRING_PROFILES_ACTIVE=<PROFILE>,GCP_PROJECT_ID=${_PROJECT_ID},JAVA_TOOL_OPTIONS=-XX:+UseSerialGC -XX:TieredStopAtLevel=1 -XX:+ExitOnOutOfMemoryError -Xms256m -Xmx512m -Djdk.virtualThreadScheduler.parallelism=4",
      ]
    id: "Deploy Service"
    waitFor: ["Push Image"]

images:
  - "${_REGION}-docker.pkg.dev/${_PROJECT_ID}/<REPO_NAME>/${_IMAGE_NAME}:latest"

timeout: '1200s'
options:
  logging: CLOUD_LOGGING_ONLY
substitutions:
  _SERVICE_NAME: "<SERVICE_NAME>"
  _IMAGE_NAME: "<IMAGE_NAME>"
  _PROJECT_ID: "jara-pct-<ENV>"
  _SA_EMAIL: "<SERVICE_NAME>-sa@jara-pct-<ENV>.iam.gserviceaccount.com"
  _REGION: "europe-west1"
"""

for dir_name, img_name in projects:
    base_path = os.path.join(dir_name, "infra", "gcp", "cloudbuild")
    os.makedirs(base_path, exist_ok=True)
    
    # BETA
    beta_content = template.replace("<REPO_NAME>", "pct-repo") \
                           .replace("<MIN_INSTANCES>", "0") \
                           .replace("<MAX_INSTANCES>", "1") \
                           .replace("<PROFILE>", "beta") \
                           .replace("<SERVICE_NAME>", f"{img_name}-beta") \
                           .replace("<IMAGE_NAME>", img_name) \
                           .replace("<ENV>", "beta")
                           
    with open(os.path.join(base_path, "cloudbuild_beta.yaml"), "w") as f:
        f.write(beta_content)
        
    # PROD
    prod_content = template.replace("<REPO_NAME>", "pct-repo") \
                           .replace("<MIN_INSTANCES>", "1") \
                           .replace("<MAX_INSTANCES>", "5") \
                           .replace("<PROFILE>", "prod") \
                           .replace("<SERVICE_NAME>", f"{img_name}-prod") \
                           .replace("<IMAGE_NAME>", img_name) \
                           .replace("<ENV>", "prod")
                           
    with open(os.path.join(base_path, "cloudbuild_prod.yaml"), "w") as f:
        f.write(prod_content)
        
print("Generados ficheros de despliegue cloudbuild para BETA y PROD.")
