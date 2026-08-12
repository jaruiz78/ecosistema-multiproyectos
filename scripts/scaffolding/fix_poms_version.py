import os

base_dir = "/home/jaruiz/Desarrollo"
projects = ["core-geogrid-h3", "core-govtech-ledger", "ProyectoDefensa", "ProyectoVPP", "ProyectoCircular"]

for proj in projects:
    pom_path = os.path.join(base_dir, proj, "pom.xml")
    if os.path.exists(pom_path):
        with open(pom_path, 'r') as f:
            content = f.read()
        
        # Replace 1.0.0-SNAPSHOT in the parent block with 1.0.0
        if "<version>1.0.0-SNAPSHOT</version>" in content:
            # We only want to replace the parent's version, not the module's own version if we can help it, but doing both is fine for now.
            content = content.replace("<version>1.0.0-SNAPSHOT</version>", "<version>1.0.0</version>", 1)
            
        with open(pom_path, 'w') as f:
            f.write(content)
        print(f"Fixed {pom_path}")
