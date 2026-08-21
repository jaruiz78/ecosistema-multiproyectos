import re

with open("ev_smart_charge_tracker.py", "r") as f:
    code = f.read()

# Make EV detection trigger reliably when home_load_w > 2000 W (even if smart plug is not configured)
old_detection = """        # 2. Determinar si hay carga real de VE
        if self.manual_override_charging is not None:
            is_ev_signature = self.manual_override_charging
        elif is_plug_on and (plug_power_w > 400.0 or home_load_w >= 1800.0):
            is_ev_signature = True
        else:
            # Si el enchufe inteligente está apagado o no hay confirmación explícita,
            # el consumo corresponde a electrodomésticos del hogar (lavavajillas, horno, etc.)
            is_ev_signature = False"""

new_detection = """        # 2. Determinar si hay carga real de VE (Firma de potencia > 2000W o Smart Plug)
        if self.manual_override_charging is not None:
            is_ev_signature = self.manual_override_charging
        elif (is_plug_on and (plug_power_w > 400.0 or home_load_w >= 1800.0)) or (home_load_w >= 2200.0):
            is_ev_signature = True
        else:
            is_ev_signature = False"""

code = code.replace(old_detection, new_detection)

with open("ev_smart_charge_tracker.py", "w") as f:
    f.write(code)

print("EV Tracker detection logic updated.")
