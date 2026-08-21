import asyncio
from playwright.async_api import async_playwright

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        context = await browser.new_context(viewport={"width": 390, "height": 844})
        page = await context.new_page()
        await page.goto("http://127.0.0.1:8526", wait_until="domcontentloaded")
        await page.wait_for_timeout(1000)

        # Find overflowing elements
        overflowing = await page.evaluate("""() => {
            const elements = document.querySelectorAll('*');
            const bad = [];
            for (let el of elements) {
                if (el.offsetWidth > 390) {
                    bad.push({
                        tag: el.tagName,
                        id: el.id,
                        className: el.className,
                        width: el.offsetWidth,
                        scrollWidth: el.scrollWidth
                    });
                }
            }
            return bad;
        }""")
        print("Overflowing elements count:", len(overflowing))
        for item in overflowing[:10]:
            print(item)
        await browser.close()

asyncio.run(main())
