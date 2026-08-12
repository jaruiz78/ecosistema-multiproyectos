import os
import re

repos = {
    "AppViajes": "/home/jaruiz/Desarrollo/AppViajes",
    "pctMultiMicroservices": "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices",
    "SaaSRegantes": "/home/jaruiz/Desarrollo/SaaSRegantes",
    "corp-spring-boot-starter": "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
}

results = {
    "sh_files": {repo: [] for repo in repos},
    "doc_files": {repo: [] for repo in repos},
    "empty_blocks": {repo: 0 for repo in repos}
}

for repo_name, repo_path in repos.items():
    for root, dirs, files in os.walk(repo_path):
        if '.git' in dirs: dirs.remove('.git')
        if 'node_modules' in dirs: dirs.remove('node_modules')
        if 'venv' in dirs: dirs.remove('venv')
        if '.venv' in dirs: dirs.remove('.venv')
        
        for file in files:
            filepath = os.path.join(root, file)
            rel_path = os.path.relpath(filepath, repo_path)
            
            # Find .sh files
            if file.endswith('.sh'):
                results["sh_files"][repo_name].append(rel_path)
                
            # Find documentation files (excluding README.md at root as it's usually fine there, but maybe capture it)
            if file.endswith('.md') or file.endswith('.pdf') or file.endswith('.txt'):
                if 'node_modules' not in filepath and 'venv' not in filepath:
                    results["doc_files"][repo_name].append(rel_path)
                    
            # Empty blocks
            if file.endswith(('.py', '.java', '.go', '.ts', '.dart')):
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        content = f.read()
                        blocks = len(re.findall(r'catch\s*\([^\)]+\)\s*{\s*}|except[^:]*:\s*pass', content))
                        results["empty_blocks"][repo_name] += blocks
                except Exception:
                    pass

print("=== SH FILES ===")
for r, files in results["sh_files"].items():
    print(f"{r}: {len(files)} files")
    
print("\n=== DOC FILES ===")
for r, files in results["doc_files"].items():
    print(f"{r}: {len(files)} files")
    
print("\n=== EMPTY BLOCKS ===")
for r, count in results["empty_blocks"].items():
    print(f"{r}: {count}")

