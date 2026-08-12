import os
import re

repos = [
    "/home/jaruiz/Desarrollo/AppViajes",
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices",
    "/home/jaruiz/Desarrollo/SaaSRegantes",
    "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
]

def fix_paths(repo):
    scripts_dir = os.path.join(repo, "scripts", "bin")
    if not os.path.exists(scripts_dir):
        return
        
    script_files = [f for f in os.listdir(scripts_dir) if f.endswith('.sh')]
    
    for root, dirs, files in os.walk(repo):
        if '.git' in dirs: dirs.remove('.git')
        if 'node_modules' in dirs: dirs.remove('node_modules')
        
        for file in files:
            if file.endswith(('.yml', '.yaml', 'Dockerfile', '.json', '.xml', '.sh', '.md')):
                filepath = os.path.join(root, file)
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        content = f.read()
                except:
                    continue
                    
                original = content
                for script in script_files:
                    # Update `./script.sh` -> `./scripts/bin/script.sh`
                    # Update `bash script.sh` -> `bash scripts/bin/script.sh`
                    # Update `sh script.sh` -> `sh scripts/bin/script.sh`
                    
                    # Regex explanation:
                    # Group 1: `./` or `bash ` or `sh `
                    # Group 2: The script name (e.g. `build.sh`)
                    # Replacement: `\1scripts/bin/\2`
                    
                    # Note: negative lookbehind to avoid double replacing `./scripts/bin/scripts/bin/`
                    pattern = r'(?<!scripts/bin/)(\./|bash\s+|sh\s+)(' + re.escape(script) + r')'
                    content = re.sub(pattern, r'\1scripts/bin/\2', content)
                    
                if content != original:
                    with open(filepath, 'w', encoding='utf-8') as f:
                        f.write(content)
                    print(f"Updated script paths in {filepath}")

for r in repos:
    fix_paths(r)
    
print("Path update complete.")
