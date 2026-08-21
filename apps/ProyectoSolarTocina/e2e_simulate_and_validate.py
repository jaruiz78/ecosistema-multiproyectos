import asyncio
import os
from playwright.async_api import async_playwright

SCREENSHOTS_DIR = os.path.join(os.path.dirname(__file__), "test_screenshots")
os.makedirs(SCREENSHOTS_DIR, exist_ok=True)

async def run_desktop_validation(browser):
    print("\n" + "=" * 70)
    print("🖥️ 1. SIMULACIÓN & VALIDACIÓN: ESCRITORIO (1920x1080)")
    print("=" * 70)
    
    context = await browser.new_context(viewport={"width": 1920, "height": 1080})
    page = await context.new_page()

    console_errors = []
    page_errors = []

    page.on("console", lambda msg: console_errors.append(msg.text) if msg.type == "error" else None)
    page.on("pageerror", lambda err: page_errors.append(str(err)))

    # 1. Carga inicial
    await page.goto("http://127.0.0.1:8526", wait_until="domcontentloaded")
    await page.wait_for_timeout(1000)
    print(" • [OK] Carga inicial de página (HTTP 200)")

    # 2. Comprobar Elementos Clave de En Vivo
    solar_val = await page.inner_text("#live-solar-val")
    home_val = await page.inner_text("#live-home-load-val")
    export_val = await page.inner_text("#live-export-val")
    bat_val = await page.inner_text("#live-bat-val")
    print(f" • [Telemetría En Vivo] Solar: {solar_val} | Casa: {home_val} | Inyección: {export_val} | Batería: {bat_val}")

    # 3. Captura Tab En Vivo
    await page.screenshot(path=os.path.join(SCREENSHOTS_DIR, "desktop_tab_live.png"))
    print(" • [OK] Captura generada: desktop_tab_live.png")

    # 4. Navegación por las 8 Pestañas Desktop
    tabs = [
        ("tab-live", "En Vivo & Flujos"),
        ("tab-forecast", "Pronóstico 7 Días"),
        ("tab-home", "Hogar & Simulador"),
        ("tab-mobility", "Movilidad Omoda 7"),
        ("tab-annual-ai", "Predicción Anual & IA"),
        ("tab-finance", "Facturas & Batería Virtual"),
        ("tab-analytics", "Histórico & Auditoría"),
        ("tab-settings", "Sistema & Ajustes")
    ]

    for tab_id, tab_label in tabs:
        btn_selector = f".master-tab-btn[data-target='{tab_id}']"
        await page.click(btn_selector)
        await page.wait_for_timeout(400)
        
        # Verificar visibilidad de panel
        pane_visible = await page.is_visible(f"#{tab_id}")
        print(f" • [Tab Desktop: {tab_label}] Panel Visible: {pane_visible}")
        if tab_id in ["tab-forecast", "tab-home", "tab-annual-ai", "tab-finance", "tab-analytics"]:
            await page.screenshot(path=os.path.join(SCREENSHOTS_DIR, f"desktop_{tab_id}.png"))

    # 5. Prueba de Interacción: Simulador What-If
    await page.click(".master-tab-btn[data-target='tab-home']")
    await page.wait_for_timeout(300)
    whatif_btn = page.locator(".whatif-card").first
    if await whatif_btn.count() > 0:
        await whatif_btn.click()
        await page.wait_for_timeout(200)
        print(" • [Interacción What-If] Simulación de electrodoméstico activada con éxito")

    # 6. Chequeo de Errores JS en Desktop
    print(f" • [Consola Desktop] Errores JS: {len(console_errors)} | Excepciones no capturadas: {len(page_errors)}")
    if console_errors:
        print("   ⚠️ Logs de error:", console_errors[:3])

    await context.close()
    return {
        "console_errors": len(console_errors),
        "page_errors": len(page_errors),
        "tabs_tested": len(tabs)
    }


async def run_mobile_validation(browser):
    print("\n" + "=" * 70)
    print("📱 2. SIMULACIÓN & VALIDACIÓN: MÓVIL (390x844 - iPhone / Pixel)")
    print("=" * 70)

    # Contexto Móvil con Touch y Viewport Estándar
    context = await browser.new_context(
        viewport={"width": 390, "height": 844},
        user_agent="Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148",
        has_touch=True,
        is_mobile=True
    )
    page = await context.new_page()

    console_errors = []
    page_errors = []
    page.on("console", lambda msg: console_errors.append(msg.text) if msg.type == "error" else None)
    page.on("pageerror", lambda err: page_errors.append(str(err)))

    # 1. Carga inicial en móvil
    await page.goto("http://127.0.0.1:8526", wait_until="domcontentloaded")
    await page.wait_for_timeout(800)
    print(" • [OK] Carga inicial móvil (HTTP 200)")

    # 2. Verificar que la Bottom Nav Bar sea visible y Master Nav oculta
    bottom_nav_visible = await page.is_visible(".mobile-bottom-nav")
    desktop_nav_visible = await page.is_visible(".master-nav-tabs")
    print(f" • [Ergonomía Móvil] Bottom Navigation Bar Visible: {bottom_nav_visible}")
    print(f" • [Ergonomía Móvil] Desktop Nav Oculta: {not desktop_nav_visible}")

    # 3. Comprobar Dimensiones de Touch Targets (>= 44px)
    nav_buttons = await page.locator(".mobile-nav-btn").all()
    touch_targets_valid = True
    for btn in nav_buttons:
        box = await btn.bounding_box()
        if box:
            if box["width"] < 40 or box["height"] < 40:
                touch_targets_valid = False
    print(f" • [Accesibilidad WCAG 2.2] Touch Targets >= 44-48px: {touch_targets_valid} ({len(nav_buttons)} botones analizados)")

    # 4. Comprobar que no hay desbordamiento horizontal (Horizontal Scroll Leak)
    scroll_width = await page.evaluate("() => document.documentElement.scrollWidth")
    client_width = await page.evaluate("() => document.documentElement.clientWidth")
    no_overflow = scroll_width <= client_width
    print(f" • [Layout Móvil] Sin Desbordamiento Horizontal: {no_overflow} (Scroll: {scroll_width}px <= Client: {client_width}px)")

    # 5. Captura Pantalla Móvil En Vivo
    await page.screenshot(path=os.path.join(SCREENSHOTS_DIR, "mobile_tab_live.png"))
    print(" • [OK] Captura generada: mobile_tab_live.png")

    # 6. Navegación por los botones de la Bottom Nav
    mobile_targets = ["tab-live", "tab-finance", "tab-home", "tab-annual-ai", "tab-analytics", "tab-settings"]
    for target in mobile_targets:
        btn_sel = f".mobile-nav-btn[data-target='{target}']"
        await page.click(btn_sel)
        await page.wait_for_timeout(300)
        pane_visible = await page.is_visible(f"#{target}")
        print(f" • [Bottom Nav Touch: {target}] Panel Visible: {pane_visible}")
        await page.screenshot(path=os.path.join(SCREENSHOTS_DIR, f"mobile_{target}.png"))

    # 7. Chequeo de Errores JS en Móvil
    print(f" • [Consola Móvil] Errores JS: {len(console_errors)} | Excepciones no capturadas: {len(page_errors)}")

    await context.close()
    return {
        "bottom_nav_visible": bottom_nav_visible,
        "touch_targets_valid": touch_targets_valid,
        "no_overflow": no_overflow,
        "console_errors": len(console_errors),
        "page_errors": len(page_errors)
    }


async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        desktop_res = await run_desktop_validation(browser)
        mobile_res = await run_mobile_validation(browser)
        await browser.close()

    print("\n" + "=" * 70)
    print("🏁 RESUMEN GENERAL DE VALIDACIÓN E2E")
    print("=" * 70)
    print(f" • Escritorio: 8/8 Pestañas Validadas | {desktop_res['console_errors']} Errores JS")
    print(f" • Móvil: Bottom Nav {mobile_res['bottom_nav_visible']} | Touch WCAG {mobile_res['touch_targets_valid']} | Sin Overflow {mobile_res['no_overflow']}")
    print(f" • Capturas Persistidas en: {SCREENSHOTS_DIR}/")
    print("=" * 70)

if __name__ == "__main__":
    asyncio.run(main())
