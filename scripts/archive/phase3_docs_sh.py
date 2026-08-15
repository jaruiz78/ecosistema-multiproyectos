import os
import shutil
import re

repos = [
    "/home/jaruiz/Desarrollo/AppViajes",
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices",
    "/home/jaruiz/Desarrollo/SaaSRegantes",
    "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
]

def ensure_dir(path):
    if not os.path.exists(path):
        os.makedirs(path)

for repo in repos:
    print(f"Organizing {repo}...")
    
    docs_dir = os.path.join(repo, "docs")
    scripts_dir = os.path.join(repo, "scripts", "bin")
    
    ensure_dir(docs_dir)
    ensure_dir(scripts_dir)
    
    for root, dirs, files in os.walk(repo):
        if '.git' in dirs: dirs.remove('.git')
        if 'node_modules' in dirs: dirs.remove('node_modules')
        if 'venv' in dirs: dirs.remove('venv')
        if '.venv' in dirs: dirs.remove('.venv')
        if 'target' in dirs: dirs.remove('target')
        if 'build' in dirs: dirs.remove('build')
        if 'dist' in dirs: dirs.remove('dist')
        if '.pub-cache' in dirs: dirs.remove('.pub-cache')
        if '.dart_tool' in dirs: dirs.remove('.dart_tool')
        if 'ios' in dirs: dirs.remove('ios')
        if 'android' in dirs: dirs.remove('android')
        
        # Avoid recursion inside docs and scripts
        if os.path.abspath(root) == os.path.abspath(docs_dir):
            continue
        if os.path.abspath(root) == os.path.abspath(scripts_dir):
            continue
            
        for file in files:
            filepath = os.path.join(root, file)
            
            # Move docs (except README in root)
            if file.endswith(('.md', '.pdf', '.txt')):
                if file.lower() == 'readme.md' and root == repo:
                    continue
                # Ignore some standard files
                if file.lower() in ['help.md', 'license.txt']:
                    continue
                    
                # Move to docs
                dest = os.path.join(docs_dir, file)
                # prevent overwrite
                counter = 1
                while os.path.exists(dest):
                    name, ext = os.path.splitext(file)
                    dest = os.path.join(docs_dir, f"{name}_{counter}{ext}")
                    counter += 1
                try:
                    shutil.move(filepath, dest)
                    print(f"Moved {file} to docs/")
                except Exception as e:
                    print(f"Error moving {file}: {e}")
                    
            # Move shell scripts
            if file.endswith('.sh'):
                # Avoid moving things from bin/ already or other specific dirs
                if 'scripts/bin' in root or 'bin/' in root:
                    continue
                    
                dest = os.path.join(scripts_dir, file)
                counter = 1
                while os.path.exists(dest):
                    name, ext = os.path.splitext(file)
                    dest = os.path.join(scripts_dir, f"{name}_{counter}{ext}")
                    counter += 1
                try:
                    shutil.move(filepath, dest)
                    print(f"Moved {file} to scripts/bin/")
                except Exception as e:
                    print(f"Error moving {file}: {e}")

print("Phase 3 and 4 file movements complete.")
