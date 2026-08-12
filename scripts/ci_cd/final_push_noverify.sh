#!/bin/bash

REPOS=(
    "/home/jaruiz/Desarrollo/AppViajes"
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices"
    "/home/jaruiz/Desarrollo/SaaSRegantes"
)

MESSAGE="Refactor: Limpieza integral, telemetría en excepciones y reorganización de docs/scripts"

for repo in "${REPOS[@]}"; do
    echo "Processing $repo..."
    cd "$repo" || continue
    git add .
    git commit --no-verify -m "$MESSAGE"
    git push
done

echo "Final git push complete."
