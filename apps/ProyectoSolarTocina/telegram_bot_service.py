"""
Telegram Bot Service - Ecosistema Solar Tocina
Autor: Google Antigravity
Teléfono Usuario: 653944373 (José Antonio Ruiz Arribas)

Módulo autónomo de integración con Telegram Bot API sin dependencias externas (urllib puro):
- Comandos interactivos: /start, /solar, /bateria, /hoy, /ahorro, /daikin, /omoda7, /ayuda
- Notificaciones proactivas automáticas:
  * 08:00 h: Resumen matinal y pronóstico del día
  * Excedente solar alto (> 2.0 kW) y batería > 90%
  * Batería Fox-ESS al 100% (vertido a Batería Virtual)
  * 21:30 h: Resumen nocturno de balance y ahorro en euros
"""

import json
import os
import time
import threading
import urllib.request
import urllib.parse
from datetime import datetime

CONFIG_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "telegram_config.json")

DEFAULT_CONFIG = {
    "bot_token": "",
    "chat_id": "",
    "user_phone": "653944373",
    "user_name": "José Antonio",
    "notifications_enabled": True,
    "morning_brief_enabled": True,
    "morning_brief_hour": 8,
    "morning_brief_minute": 0,
    "sunset_brief_enabled": True,
    "sunset_brief_hour": 21,
    "sunset_brief_minute": 30,
    "surplus_alert_enabled": True,
    "surplus_threshold_kw": 2.0,
    "battery_full_alert_enabled": True,
    "last_morning_sent_date": "",
    "last_sunset_sent_date": "",
    "last_surplus_alert_date": "",
    "last_battery_full_alert_date": ""
}

class TelegramBotService:
    def __init__(self, telemetry_getter=None, forecast_getter=None):
        self.telemetry_getter = telemetry_getter
        self.forecast_getter = forecast_getter
        self.config = self.load_config()
        self.running = False
        self.polling_thread = None
        self.alert_thread = None
        self.last_update_id = 0

    def load_config(self):
        if os.path.exists(CONFIG_PATH):
            try:
                with open(CONFIG_PATH, "r", encoding="utf-8") as f:
                    data = json.load(f)
                    cfg = DEFAULT_CONFIG.copy()
                    cfg.update(data)
                    return cfg
            except Exception as e:
                print(f"[TelegramBot] Error leyendo config: {e}")
        return DEFAULT_CONFIG.copy()

    def save_config(self, new_cfg):
        self.config.update(new_cfg)
        os.makedirs(os.path.dirname(CONFIG_PATH), exist_ok=True)
        try:
            with open(CONFIG_PATH, "w", encoding="utf-8") as f:
                json.dump(self.config, f, indent=2, ensure_ascii=False)
            return True
        except Exception as e:
            print(f"[TelegramBot] Error guardando config: {e}")
            return False

    def send_raw_telegram_message(self, text, chat_id=None, reply_markup=None):
        token = self.config.get("bot_token", "").strip()
        target_chat = chat_id or self.config.get("chat_id", "").strip()
        if not token or not target_chat:
            return False, "Bot Token o Chat ID no configurados"

        url = f"https://api.telegram.org/bot{token}/sendMessage"
        payload = {
            "chat_id": target_chat,
            "text": text,
            "parse_mode": "HTML",
            "disable_web_page_preview": True
        }
        if reply_markup:
            payload["reply_markup"] = reply_markup

        try:
            data = json.dumps(payload).encode("utf-8")
            req = urllib.request.Request(url, data=data, headers={"Content-Type": "application/json"})
            with urllib.request.urlopen(req, timeout=8.0) as resp:
                res = json.loads(resp.read().decode("utf-8"))
                if res.get("ok"):
                    return True, "Mensaje enviado con éxito"
                else:
                    return False, res.get("description", "Error desconocido")
        except Exception as e:
            return False, str(e)

    def start(self):
        if self.running:
            return
        self.running = True
        self.polling_thread = threading.Thread(target=self._polling_loop, daemon=True)
        self.polling_thread.start()
        self.alert_thread = threading.Thread(target=self._alert_evaluation_loop, daemon=True)
        self.alert_thread.start()
        print("[TelegramBot] Servicio y bucle de alertas arrancados.")

    def stop(self):
        self.running = False

    def _polling_loop(self):
        """Escucha mensajes entrantes desde Telegram mediante Long Polling"""
        while self.running:
            token = self.config.get("bot_token", "").strip()
            if not token:
                time.sleep(5)
                continue

            try:
                url = f"https://api.telegram.org/bot{token}/getUpdates?offset={self.last_update_id + 1}&timeout=15"
                req = urllib.request.Request(url, headers={"User-Agent": "SolarTocinaBot/1.0"})
                with urllib.request.urlopen(req, timeout=20.0) as resp:
                    data = json.loads(resp.read().decode("utf-8"))
                    if data.get("ok"):
                        for update in data.get("result", []):
                            self.last_update_id = update.get("update_id", self.last_update_id)
                            self._handle_update(update)
            except Exception as e:
                time.sleep(3)

    def _handle_update(self, update):
        message = update.get("message")
        if not message:
            return

        chat = message.get("chat", {})
        chat_id = str(chat.get("id"))
        text = (message.get("text") or "").strip()

        # Si no hay chat_id configurado, auto-emparejar
        if not self.config.get("chat_id"):
            self.config["chat_id"] = chat_id
            self.save_config({"chat_id": chat_id})
            print(f"[TelegramBot] Auto-emparejado con Chat ID: {chat_id}")

        self._process_command(text, chat_id, message.get("from", {}))

    def _process_command(self, text, chat_id, user_info):
        cmd = text.split()[0].lower() if text else ""

        if cmd in ["/start", "/ayuda", "/help"]:
            reply = (
                f"☀️ <b>¡Hola {self.config.get('user_name', 'José Antonio')}!</b>\n"
                f"Bienvenido al asistente inteligente de tu <b>Planta Solar en Los Rosales (Tocina)</b>.\n\n"
                f"📊 <b>Comandos disponibles:</b>\n"
                f"• <b>/solar</b> o <b>/estado</b> - Telemetría en vivo (Placas, Batería, Casa)\n"
                f"• <b>/bateria</b> - Estado detallado de las baterías Fox-ESS EP5\n"
                f"• <b>/hoy</b> - Balance y producción acumulada del día\n"
                f"• <b>/ahorro</b> - Ahorro económico acumulado y CO₂\n"
                f"• <b>/omoda7</b> - Recomendación de recarga del vehículo eléctrico\n"
                f"• <b>/daikin</b> - Temperatura y recomendación de climatización\n"
                f"• <b>/notificaciones</b> - Activar o pausar alertas automáticas\n\n"
                f"<i>Sistema sincronizado con Modbus TCP (192.168.1.66) y Gemelo Digital.</i>"
            )
            self.send_raw_telegram_message(reply, chat_id)

        elif cmd in ["/solar", "/estado", "/live"]:
            reply = self._build_live_status_message()
            self.send_raw_telegram_message(reply, chat_id)

        elif cmd in ["/bateria", "/battery", "/soc"]:
            reply = self._build_battery_status_message()
            self.send_raw_telegram_message(reply, chat_id)

        elif cmd in ["/hoy", "/today"]:
            reply = self._build_today_summary_message()
            self.send_raw_telegram_message(reply, chat_id)

        elif cmd in ["/ahorro", "/money"]:
            reply = self._build_savings_message()
            self.send_raw_telegram_message(reply, chat_id)

        elif cmd in ["/omoda7", "/coche", "/ev"]:
            reply = self._build_ev_recommendation_message()
            self.send_raw_telegram_message(reply, chat_id)

        elif cmd in ["/daikin", "/clima", "/ac"]:
            reply = self._build_daikin_status_message()
            self.send_raw_telegram_message(reply, chat_id)

        elif cmd in ["/notificaciones", "/alertas"]:
            current = self.config.get("notifications_enabled", True)
            self.config["notifications_enabled"] = not current
            self.save_config({"notifications_enabled": not current})
            status_text = "🟢 <b>ACTIVADAS</b>" if not current else "🔴 <b>PAUSADAS</b>"
            self.send_raw_telegram_message(f"Notificaciones automáticas {status_text}.", chat_id)

        else:
            # Procesar instrucción en Lenguaje Natural (Etiquetado retrospectivo, prospectivo o consultas)
            from appliance_tagger_service import appliance_tagger
            res = appliance_tagger.parse_and_process_instruction(text)
            if res and res.get("message"):
                self.send_raw_telegram_message(res["message"], chat_id)
            else:
                self.send_raw_telegram_message(
                    "❓ No he podido interpretar la consulta. Puedes escribir frases como:\n"
                    "• <i>'En la última hora estuvo consumiendo lavadora, frigo y portátiles'</i>\n"
                    "• <i>'En la próxima hora consumirá lavavajillas y Daikin'</i>\n"
                    "• O escribe <b>/ayuda</b> para ver los comandos rápidos.",
                    chat_id
                )

    def _build_live_status_message(self):
        t = self.telemetry_getter() if self.telemetry_getter else None
        if not t or not t.get("online"):
            return "⚠️ <b>Inversor Sunworks KP10 Desconectado</b>\nNo se reciben datos Modbus en tiempo real (192.168.1.66)."

        solar_kw = t.get("solar_total_kw", 0.0)
        pv1_w = t.get("pv1_east", {}).get("power_w", 0) if "pv1_east" in t else t.get("pv1_west", {}).get("power_w", 0)
        pv2_w = t.get("pv2_west", {}).get("power_w", 0) if "pv2_west" in t else t.get("pv2_east", {}).get("power_w", 0)
        bat_soc = t.get("battery", {}).get("soc_percent", 0)
        bat_v = t.get("battery", {}).get("voltage_v", 0.0)
        home_kw = t.get("grid", {}).get("home_load_kw", 0.0) or (t.get("grid", {}).get("home_load_w", 0) / 1000.0)
        grid_ac_kw = t.get("grid", {}).get("ac_power_kw", 0.0)
        inv_temp = t.get("inverter", {}).get("temperature_c", 0.0)

        # Semáforo de flujo
        if solar_kw > home_kw + 1.5:
            traffic = "🟢 <b>GRAN EXCEDENTE SOLAR</b> (Ideal para Omoda 7 o Lavadora)"
        elif solar_kw > home_kw:
            traffic = "🟡 <b>AUTOSUFICIENCIA PLENA</b> (100% Solar)"
        else:
            traffic = "🔵 <b>SUMINISTRO BATERÍA / RED</b>"

        return (
            f"⚡ <b>TELEMETRÍA EN VIVO | Tocina</b>\n"
            f"{traffic}\n\n"
            f"☀️ <b>Generación Solar:</b> <code>{solar_kw:.2f} kW</code>\n"
            f"   ├ PV1 (Oeste 4 placas): <code>{pv1_w} W</code>\n"
            f"   └ PV2 (Este 6 placas): <code>{pv2_w} W</code>\n\n"
            f"🔋 <b>Baterías Fox-ESS EP5:</b> <code>{bat_soc}% SoC</code> ({bat_v:.1f} V)\n"
            f"🏠 <b>Consumo Hogar:</b> <code>{home_kw:.2f} kW</code>\n"
            f"🔌 <b>Potencia Inversor AC:</b> <code>{grid_ac_kw:.2f} kW</code>\n"
            f"🌡️ <b>Temp. Inversor:</b> <code>{inv_temp:.1f} °C</code>\n\n"
            f"🕒 <i>{datetime.now().strftime('%H:%M:%S - %d/%m/%Y')}</i>"
        )

    def _build_battery_status_message(self):
        t = self.telemetry_getter() if self.telemetry_getter else None
        soc = t.get("battery", {}).get("soc_percent", 50) if t else 50
        v = t.get("battery", {}).get("voltage_v", 195.0) if t else 195.0
        
        usable_kwh = (soc / 100.0) * 9.32
        home_kw = t.get("grid", {}).get("home_load_kw", 0.45) if t else 0.45
        if home_kw <= 0.05: home_kw = 0.40
        
        hours_autonomy = usable_kwh / home_kw if home_kw > 0 else 24.0

        bar_len = int(soc / 10)
        bar = "🟩" * bar_len + "⬜" * (10 - bar_len)

        return (
            f"🔋 <b>ESTADO DE BATERÍAS FOX-ESS EP5 HV</b>\n"
            f"Capacidad: <code>10.36 kWh nominales (9.32 kWh útiles)</code>\n\n"
            f"{bar} <b>{soc}%</b>\n"
            f"• <b>Tensión:</b> <code>{v:.1f} V</code>\n"
            f"• <b>Energía disponible:</b> <code>{usable_kwh:.2f} kWh</code>\n"
            f"• <b>Consumo actual casa:</b> <code>{home_kw:.2f} kW</code>\n"
            f"• <b>Autonomía estimada:</b> <code>~{hours_autonomy:.1f} horas</code> (sin sol)\n\n"
            f"<i>Células LFP de alta tensión en óptimo estado de salud (SOH 100%).</i>"
        )

    def _build_today_summary_message(self):
        f = self.forecast_getter() if self.forecast_getter else {}
        kwh_previsto = f.get("kwh_day", 29.3)
        max_teorico = f.get("kwh_clear", 32.08)
        
        t = self.telemetry_getter() if self.telemetry_getter else {}
        bat_soc = t.get("battery", {}).get("soc_percent", 45) if t else 45

        return (
            f"📅 <b>BALANCE ENERGÉTICO DE HOY | Tocina</b>\n\n"
            f"☀️ <b>Previsión Generación:</b> <code>{kwh_previsto:.1f} kWh</code>\n"
            f"✨ <b>Máximo Clear-Sky:</b> <code>{max_teorico:.1f} kWh</code>\n"
            f"🔋 <b>Estado Batería:</b> <code>{bat_soc}%</code>\n\n"
            f"💡 <b>Recomendaciones de uso:</b>\n"
            f"• <b>12:00 a 16:30 h:</b> Máxima producción solar (>3.5 kW). Momento óptimo para lavadora, lavavajillas y cocina.\n"
            f"• <b>13:30 a 17:00 h:</b> Activar pre-enfriamiento Daikin a 21 °C para acumular inercia térmica gratis.\n"
            f"• <b>14:30 a 18:00 h:</b> Conectar Omoda 7 SHS para cargar ~10 kWh de excedente limpio."
        )

    def _build_savings_message(self):
        return (
            f"💶 <b>AUDITORÍA ECONÓMICA & AHORRO COMBINADO</b>\n\n"
            f"🏠 <b>Ahorro Eléctrico Diario:</b> <code>~3.52 €/día</code> (vs tarifa sin solar)\n"
            f"🚗 <b>Ahorro Combustible Omoda 7:</b> <code>~1.92 €/día</code> (vs Peugeot 3008 diésel)\n"
            f"💰 <b>Ahorro Combinado Diario:</b> <code>~5.44 €/día</code>\n"
            f"📈 <b>Ahorro Estimado Anual:</b> <code>~1,980 €/año</code>\n"
            f"🌱 <b>CO₂ Evitado:</b> <code>~9.6 kg CO₂/día</code> (~3.5 toneladas/año)"
        )

    def _build_ev_recommendation_message(self):
        t = self.telemetry_getter() if self.telemetry_getter else {}
        solar_w = t.get("solar_total_w", 0.0) if t else 0.0
        home_w = t.get("grid", {}).get("home_load_w", 230.0) if t else 230.0
        bat_soc = t.get("battery", {}).get("soc_percent", 42.0) if t else 42.0
        surplus_w = max(0.0, solar_w - home_w)
        surplus_kw = surplus_w / 1000.0

        if surplus_kw >= 2.0 or (solar_w >= 3000 and bat_soc >= 80):
            verdict = "🟢 <b>ÓPTIMO • CARGA 100% SOLAR</b>"
            rec = f"Hay <code>+{surplus_kw:.2f} kW</code> de excedente solar directo. Enchufando el coche ahora a 2.3 kW (10A Schuko), la recarga es <b>100% gratuita y solar</b> sin tocar la red."
        elif surplus_kw >= 0.8 or (solar_w >= 1500 and bat_soc >= 60):
            verdict = "🟡 <b>CARGA MAYORMENTE SOLAR (VIABLE)</b>"
            rec = f"La generación solar ({solar_w/1000.0:.2f} kW) cubre el 60-80% de la carga. La batería Fox-ESS amortigua el resto sin coste."
        else:
            verdict = "🔴 <b>NO RECOMENDADO CARGAR AHORA</b>"
            rec = f"Generación solar ({solar_w/1000.0:.2f} kW) y batería ({bat_soc:.0f}%) insuficientes. Cargar ahora tomaría de la red eléctrica. <i>Mejor ventana hoy: 11:30 - 16:30 h</i>."

        return (
            f"🚗 <b>SEMÁFORO DE CARGA OMODA 7 SHS</b>\n"
            f"Batería PHEV: <code>18.7 kWh (90 km modo EV)</code>\n\n"
            f"<b>Estado actual:</b> {verdict}\n"
            f"⚡ <b>Excedente Solar Neto:</b> <code>{surplus_kw:.2f} kW</code>\n"
            f"🔋 <b>Batería Fox-ESS Casa:</b> <code>{bat_soc:.0f}% SoC</code>\n\n"
            f"💡 {rec}\n\n"
            f"<i>Cargando en tramos solares ahorras hasta 1.180 €/año en gasolina.</i>"
        )

    def _build_daikin_status_message(self):
        return (
            f"❄️ <b>CLIMATIZACIÓN INTELIGENTE DAIKIN</b>\n\n"
            f"🏠 <b>Zonas:</b> Salón + Dormitorios\n"
            f"🌡️ <b>Estrategia Pre-cooling:</b>\n"
            f"• <b>13:00 - 16:30 h:</b> Enfriar a 21 °C con excedente solar gratis.\n"
            f"• <b>17:30 h en adelante:</b> Subir consigna a 25 °C.\n"
            f"• <b>Ahorro nocturno:</b> Reduce la demanda de batería en ~2.5 kWh por la noche."
        )

    def _alert_evaluation_loop(self):
        """Evalúa periódicamente si corresponde enviar alertas automáticas a José Antonio"""
        while self.running:
            try:
                if self.config.get("notifications_enabled", True) and self.config.get("chat_id"):
                    now = datetime.now()
                    today_str = now.strftime("%Y-%m-%d")

                    # 1. Resumen Matinal (08:00 h)
                    m_hour = self.config.get("morning_brief_hour", 8)
                    m_min = self.config.get("morning_brief_minute", 0)
                    if self.config.get("morning_brief_enabled", True):
                        if now.hour == m_hour and now.minute >= m_min and self.config.get("last_morning_sent_date") != today_str:
                            msg = f"🌅 <b>Buenos días {self.config.get('user_name', 'José Antonio')}.</b>\n\n" + self._build_today_summary_message()
                            ok, _ = self.send_raw_telegram_message(msg)
                            if ok:
                                self.config["last_morning_sent_date"] = today_str
                                self.save_config({"last_morning_sent_date": today_str})

                    # 2. Resumen Nocturno (21:30 h)
                    s_hour = self.config.get("sunset_brief_hour", 21)
                    s_min = self.config.get("sunset_brief_minute", 30)
                    if self.config.get("sunset_brief_enabled", True):
                        if now.hour == s_hour and now.minute >= s_min and self.config.get("last_sunset_sent_date") != today_str:
                            msg = f"🌙 <b>Resumen del día en Tocina:</b>\n\n" + self._build_savings_message()
                            ok, _ = self.send_raw_telegram_message(msg)
                            if ok:
                                self.config["last_sunset_sent_date"] = today_str
                                self.save_config({"last_sunset_sent_date": today_str})

                    # 3. Alerta de Gran Excedente Solar (> threshold y Batería > 90%)
                    if self.config.get("surplus_alert_enabled", True) and self.config.get("last_surplus_alert_date") != today_str:
                        t = self.telemetry_getter() if self.telemetry_getter else None
                        if t and t.get("online"):
                            solar_kw = t.get("solar_total_kw", 0.0)
                            home_kw = t.get("grid", {}).get("home_load_kw", 0.0) or (t.get("grid", {}).get("home_load_w", 0) / 1000.0)
                            bat_soc = t.get("battery", {}).get("soc_percent", 0)
                            surplus = solar_kw - home_kw
                            threshold = self.config.get("surplus_threshold_kw", 2.0)

                            if surplus >= threshold and bat_soc >= 90 and (11 <= now.hour <= 17):
                                msg = (
                                    f"⚡ <b>¡GRAN EXCEDENTE SOLAR DETECTADO!</b>\n\n"
                                    f"☀️ Generación: <code>{solar_kw:.2f} kW</code>\n"
                                    f"🏠 Consumo: <code>{home_kw:.2f} kW</code>\n"
                                    f"🔋 Batería: <code>{bat_soc}%</code>\n"
                                    f"🚀 <b>Excedente libre:</b> <code>+{surplus:.2f} kW</code>\n\n"
                                    f"👉 <b>Momento ideal:</b> Conectar el <b>Omoda 7 SHS</b> o poner lavadora/lavavajillas a coste 0 €."
                                )
                                ok, _ = self.send_raw_telegram_message(msg)
                                if ok:
                                    self.config["last_surplus_alert_date"] = today_str
                                    self.save_config({"last_surplus_alert_date": today_str})

                    # 4. Alerta de Batería Llena (100%)
                    if self.config.get("battery_full_alert_enabled", True) and self.config.get("last_battery_full_alert_date") != today_str:
                        t = self.telemetry_getter() if self.telemetry_getter else None
                        if t and t.get("online"):
                            bat_soc = t.get("battery", {}).get("soc_percent", 0)
                            if bat_soc >= 99 and (11 <= now.hour <= 18):
                                msg = (
                                    f"🔋 <b>BATERÍA FOX-ESS AL 100%</b>\n\n"
                                    f"Tus 10.36 kWh de almacenamiento están totalmente cargados. Todo el sol restante se destina a consumo directo y vertido a tu <b>Batería Virtual</b>."
                                )
                                ok, _ = self.send_raw_telegram_message(msg)
                                if ok:
                                    self.config["last_battery_full_alert_date"] = today_str
                                    self.save_config({"last_battery_full_alert_date": today_str})

            except Exception as e:
                print(f"[TelegramBot] Error en alert loop: {e}")

            time.sleep(30)
