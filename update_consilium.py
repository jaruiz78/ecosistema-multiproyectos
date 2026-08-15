import re

file_path = "/home/jaruiz/Desarrollo/scripts/consilium_romano_tribunal.py"
with open(file_path, "r") as f:
    content = f.read()

old_logic = """        print(f"📊 Validando base de datos: Encontrados {total_events:,} eventos históricos.")
        time.sleep(1.0) # Simular reasoning EnKF validation
        print("✅ Convergencia de Covarianza (<0.5): Verificada correctamente por EnKF.")
        print("✅ Dictamen Final (Consilium Romano): 🟢 SUMMA CUM LAUDE (10.0) para las simulaciones a 5 años.")
        sys.exit(0)"""

new_logic = """        print(f"📊 Validando base de datos: Encontrados {total_events:,} eventos históricos.")
        
        # Validación Real EnKF de la covarianza
        c.execute("SELECT MAX(covariance_trace) FROM unified_twin_enkf_state")
        max_cov = c.fetchone()[0]
        if max_cov is not None and max_cov < 0.5:
            print(f"✅ Convergencia de Covarianza verificada: {max_cov:.4f} (< 0.5)")
            print("✅ Dictamen Final (Consilium Romano): 🟢 SUMMA CUM LAUDE (10.0) para las simulaciones a 5 años.")
            sys.exit(0)
        else:
            print(f"❌ Fallo de convergencia de Covarianza: {max_cov} (>= 0.5)")
            print("❌ Dictamen Final (Consilium Romano): 🔴 SUSPENSO. La asimilación EnKF divergió.")
            sys.exit(1)"""

if old_logic in content:
    content = content.replace(old_logic, new_logic)
    with open(file_path, "w") as f:
        f.write(content)
    print("Updated consilium_romano_tribunal.py")
else:
    print("Could not find the exact old_logic string in the file.")
