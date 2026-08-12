#!/bin/bash

REPOS=(
    "/home/jaruiz/Desarrollo/AppViajes"
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices"
    "/home/jaruiz/Desarrollo/SaaSRegantes"
    "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
)

MESSAGE="Refactor: Integración masiva con el Gemelo Digital Unificado (World Model)"

for repo in "${REPOS[@]}"; do
    echo "Processing $repo..."
    cd "$repo" || continue
    git add .
    git commit -m "$MESSAGE"
    git push
done

echo "Done."
