import asyncio
from playwright.async_api import async_playwright

async def main():
    try:
        async with async_playwright() as p:
            browser = await p.chromium.launch(headless=True)
            page = await browser.new_page()
            await page.goto("http://127.0.0.1:8526")
            title = await page.title()
            print("Title:", title)
            await browser.close()
            print("Playwright launched successfully!")
    except Exception as e:
        print("Error launching Playwright:", e)

asyncio.run(main())
