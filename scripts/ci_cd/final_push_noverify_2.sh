#!/bin/bash

REPOS=(
    "/home/jaruiz/Desarrollo/AppViajes"
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices"
    "/home/jaruiz/Desarrollo/SaaSRegantes"
    "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
)

MESSAGE="feat: Arquitectura 4.0 - Gemelo Digital Unificado, Memory Arenas, PINNs y AOT"

for repo in "${REPOS[@]}"; do
    echo "Processing $repo..."
    cd "$repo" || continue
    git add .
    git commit --no-verify -m "$MESSAGE"
    git push
done

echo "Final git push complete."
