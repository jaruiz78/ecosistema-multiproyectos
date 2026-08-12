import os

base_dir = "/home/jaruiz/Desarrollo"
projects = ["core-geogrid-h3", "core-govtech-ledger", "ProyectoDefensa", "ProyectoVPP", "ProyectoCircular"]

for proj in projects:
    pom_path = os.path.join(base_dir, proj, "pom.xml")
    if os.path.exists(pom_path):
        with open(pom_path, 'r') as f:
            content = f.read()
        
        # Add relative path
        if "<groupId>com.pct.corp</groupId>" in content:
            content = content.replace("<groupId>com.pct.corp</groupId>", "<groupId>com.corp.tenant</groupId>")
            
        with open(pom_path, 'w') as f:
            f.write(content)
        print(f"Fixed {pom_path}")
