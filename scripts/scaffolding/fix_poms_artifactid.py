import os

base_dir = "/home/jaruiz/Desarrollo"
projects = ["core-geogrid-h3", "core-govtech-ledger", "ProyectoDefensa", "ProyectoVPP", "ProyectoCircular"]

for proj in projects:
    pom_path = os.path.join(base_dir, proj, "pom.xml")
    if os.path.exists(pom_path):
        with open(pom_path, 'r') as f:
            content = f.read()
        
        # Add relative path
        if "<artifactId>corp-spring-boot-starter</artifactId>" in content:
            content = content.replace("<artifactId>corp-spring-boot-starter</artifactId>", "<artifactId>corp-spring-boot-starter-parent</artifactId>")
            
        with open(pom_path, 'w') as f:
            f.write(content)
        print(f"Fixed {pom_path}")
