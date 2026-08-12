import os
import re

repos = [
    "/home/jaruiz/Desarrollo/AppViajes",
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices",
    "/home/jaruiz/Desarrollo/SaaSRegantes",
]

for repo in repos:
    precommit_path = os.path.join(repo, ".git", "hooks", "pre-commit")
    if os.path.exists(precommit_path):
        with open(precommit_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        original = content
        # Update references to scripts
        content = content.replace('./scripts/start-cycle.sh', './scripts/bin/start-cycle.sh')
        content = content.replace('./scripts/sast-scanner.py', './scripts/bin/sast-scanner.py') # Just in case
        
        if content != original:
            with open(precommit_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Fixed pre-commit hook in {repo}")

