terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}

provider "google" {
  project = "my-antigravity-project"
  region  = "europe-west1"
}

# Despliegue Zero-Trust en Cloud Run (Scale-to-Zero)
resource "google_cloud_run_v2_service" "agentic_router" {
  name     = "antigravity-agentic-router"
  location = "europe-west1"
  ingress  = "INGRESS_TRAFFIC_INTERNAL_ONLY"

  template {
    containers {
      image = "europe-west1-docker.pkg.dev/my-antigravity-project/repo/agentic-router:latest"
      
      resources {
        limits = {
          cpu    = "1000m"
          memory = "256Mi"
        }
      }
      
      env {
        name  = "OTEL_EXPORTER_OTLP_ENDPOINT"
        value = "https://otlp.europe-west1.googlecloud.com"
      }
    }
    scaling {
      min_instance_count = 0
      max_instance_count = 10
    }
  }
}

# Obligar el uso de OIDC / Workload Identity
resource "google_cloud_run_service_iam_binding" "default" {
  location = google_cloud_run_v2_service.agentic_router.location
  project  = google_cloud_run_v2_service.agentic_router.project
  service  = google_cloud_run_v2_service.agentic_router.name
  role     = "roles/run.invoker"
  members = [
    "serviceAccount:agent-sa@my-antigravity-project.iam.gserviceaccount.com",
  ]
}
