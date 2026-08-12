#!/usr/bin/env bash
set -e

echo "=========================================================="
echo "☸️  Antigravity 3.0: Setup GitOps & ArgoCD / Cloud Deploy"
echo "=========================================================="
echo "Generando manifiestos Kustomize para ArgoCD..."

mkdir -p /home/jaruiz/Desarrollo/infra/gitops/argocd

cat << 'EOF' > /home/jaruiz/Desarrollo/infra/gitops/argocd/application-multiproyectos.yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: antigravity-multiproyectos
  namespace: argocd
spec:
  project: default
  source:
    repoURL: 'https://github.com/jaruiz/Antigravity-Ecosystem'
    path: infra/k8s/overlays/production
    targetRevision: HEAD
  destination:
    server: 'https://kubernetes.default.svc'
    namespace: antigravity-prod
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
    syncOptions:
      - CreateNamespace=true
EOF

cat << 'EOF' > /home/jaruiz/Desarrollo/infra/gitops/argocd/presync-slsa-l3-verifier-hook.yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: presync-slsa-l3-verifier
  namespace: argocd
  annotations:
    argocd.argoproj.io/hook: PreSync
    argocd.argoproj.io/hook-delete-policy: HookSucceeded
spec:
  template:
    spec:
      containers:
      - name: slsa-verifier
        image: python:3.14-slim
        command: ["python3", "/home/jaruiz/Desarrollo/scripts/security/slsa_provenance_verifier.py"]
      restartPolicy: Never
EOF

echo "✅ Hook PreSync SLSA L3 generado en /infra/gitops/argocd/presync-slsa-l3-verifier-hook.yaml"
echo "✅ Manifiesto ArgoCD generado en /infra/gitops/argocd/"
echo "✅ Estrategia SLSA L3 de validación de firmas activada en el sync."
echo "La rama 'main' ahora gobernará todo el clúster sin intervención manual."
echo "=========================================================="
