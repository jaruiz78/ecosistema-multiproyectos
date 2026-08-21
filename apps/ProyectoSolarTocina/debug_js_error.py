import asyncio
from playwright.async_api import async_playwright

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        page = await browser.new_page()
        page.on("pageerror", lambda err: print("PAGE ERROR:", err))
        page.on("console", lambda msg: print(f"CONSOLE [{msg.type}]:", msg.text))
        await page.goto("http://127.0.0.1:8526", wait_until="domcontentloaded")
        await page.wait_for_timeout(1500)
        await page.click(".master-tab-btn[data-target='tab-forecast']")
        await page.wait_for_timeout(1000)
        await browser.close()

asyncio.run(main())
