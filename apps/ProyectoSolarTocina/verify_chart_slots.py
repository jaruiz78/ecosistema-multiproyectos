import asyncio
from playwright.async_api import async_playwright

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        page = await browser.new_page()
        await page.goto("http://127.0.0.1:8526", wait_until="domcontentloaded")
        await page.wait_for_timeout(2000)
        
        chart_info = await page.evaluate("""() => {
            const app = window.solarApp;
            if (!app || !app.chartToday) return null;
            const labels = app.chartToday.data.labels;
            const evDs = app.chartToday.data.datasets.find(d => d.label && d.label.includes('Omoda 7'));
            if (!evDs) return { error: 'EV dataset not found' };
            
            const activeSlots = [];
            evDs.data.forEach((val, idx) => {
                if (val !== null && val > 0) {
                    activeSlots.push({ slot: idx, time: labels[idx], power_kw: val });
                }
            });
            return { total_labels: labels.length, total_ev_points: evDs.data.length, activeSlots };
        }""")
        print("CHART EVALUATION RESULT:")
        import json
        print(json.dumps(chart_info, indent=2))
        await browser.close()

asyncio.run(main())
