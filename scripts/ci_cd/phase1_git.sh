#!/bin/bash

REPOS=(
    "/home/jaruiz/Desarrollo/AppViajes"
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices"
    "/home/jaruiz/Desarrollo/SaaSRegantes"
    "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
)

for repo in "${REPOS[@]}"; do
    echo "Processing $repo"
    
    # 1. Clean self-ignoring .gitignore
    sed -i '/^\.gitignore$/d' "$repo/.gitignore"
    
    # 2. Append Python and Java rules
    for rule in "__pycache__/" "*.pyc" "venv/" ".venv/" "*.class" "target/"; do
        if ! grep -q "^${rule}$" "$repo/.gitignore"; then
            echo "$rule" >> "$repo/.gitignore"
        fi
    done
    
    cd "$repo" || continue
    
    echo "Purging cache in $repo..."
    # 3. Purge cached files
    git rm -r --cached . 2>/dev/null
    git add .
    git commit -m "Fix: Saneamiento de .gitignore (Limpieza de caché)"
    
    # Normal push (since git rm --cached . + git add . just stops tracking ignored files, no need for force push unless we rewritten history with filter-repo)
    git push
done
echo "Phase 1 complete."
