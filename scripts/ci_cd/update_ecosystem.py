import os

repos = [
    '/home/jaruiz/Desarrollo/AppViajes',
    '/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices',
    '/home/jaruiz/Desarrollo/SaaSRegantes',
    '/home/jaruiz/Desarrollo/corp-spring-boot-starter'
]

# Update Dockerfiles
for repo in repos:
    for root, dirs, files in os.walk(repo):
        if 'node_modules' in dirs:
            dirs.remove('node_modules')
        if 'venv' in dirs:
            dirs.remove('venv')
        if '.git' in dirs:
            dirs.remove('.git')
            
        for file in files:
            if file == 'Dockerfile':
                filepath = os.path.join(root, file)
                with open(filepath, 'a') as f:
                    f.write("\n# Hook to Unified Digital Twin\nENV UNIFIED_WORLD_MODEL_ACTIVE=true\n")
                print(f"Updated {filepath}")
                
            if file.endswith('.py') and 'unified_twin' not in root and 'venv' not in root:
                filepath = os.path.join(root, file)
                with open(filepath, 'r') as f:
                    content = f.read()
                
                # Check if it looks like a simulation script
                if 'def ' in content or 'import ' in content:
                    if 'DEPRECATED FOR STANDALONE USE' not in content:
                        warning = 'print("[WARNING] DEPRECATED FOR STANDALONE USE. This simulation is now a sub-node of the Unified World Model. Please run corp-spring-boot-starter/unified_twin/master_digital_twin.py instead.")\n'
                        with open(filepath, 'w') as f:
                            f.write(warning + content)
                        print(f"Updated simulation script {filepath}")

