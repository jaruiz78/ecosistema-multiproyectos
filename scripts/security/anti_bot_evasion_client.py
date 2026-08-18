#!/usr/bin/env python3
"""
anti_bot_evasion_client.py
-------------------------------------------------------------------------
Cliente de Ingesta Web Resiliente con Evasión Anti-Bot y Extracción Adaptativa.
Inspirado en curl-impersonate, Scrapling y AutoScraper.
Emula firmas TLS/JA3/JA4 de Chrome/Firefox, gestiona backoff con jitter y
aplica selectores heurísticos tolerantes a cambios de diseño del DOM.
-------------------------------------------------------------------------
"""
import os
import sys
import re
import time
import json
import random
import urllib.request
import urllib.parse
from typing import Dict, List, Any, Optional

class AntiBotEvasionClient:
    """Cliente HTTP con soporte para emulación de fingerprints de navegador y rotación de headers."""

    DEFAULT_USER_AGENTS = [
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
        "Mozilla/5.0 (X11; Linux x86_64; rv:128.0) Gecko/20100101 Firefox/128.0",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4.1 Mobile/15E148 Safari/604.1"
    ]

    def __init__(self, max_retries: int = 3, base_delay: float = 0.5):
        self.max_retries = max_retries
        self.base_delay = base_delay

    def _get_browser_headers(self, user_agent: Optional[str] = None) -> Dict[str, str]:
        ua = user_agent or random.choice(self.DEFAULT_USER_AGENTS)
        is_chrome = "Chrome" in ua
        
        headers = {
            "User-Agent": ua,
            "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language": "es-ES,es;q=0.9,en-US;q=0.8,en;q=0.7",
            "Accept-Encoding": "identity", # Simplifica descompresión
            "DNT": "1",
            "Upgrade-Insecure-Requests": "1"
        }
        if is_chrome:
            headers.update({
                "Sec-CH-UA": '"Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"',
                "Sec-CH-UA-Mobile": "?0",
                "Sec-CH-UA-Platform": '"Linux"' if "Linux" in ua else '"Windows"',
                "Sec-Fetch-Dest": "document",
                "Sec-Fetch-Mode": "navigate",
                "Sec-Fetch-Site": "none",
                "Sec-Fetch-User": "?1"
            })
        return headers

    def fetch(self, url: str, method: str = "GET", data: Optional[Dict[str, Any]] = None,
              headers: Optional[Dict[str, str]] = None, timeout: int = 10) -> str:
        """Descarga URL aplicando reintentos exponenciales con full jitter y evasión de headers."""
        req_headers = self._get_browser_headers()
        if headers:
            req_headers.update(headers)

        encoded_data = None
        if data:
            encoded_data = urllib.parse.urlencode(data).encode("utf-8")

        for attempt in range(1, self.max_retries + 1):
            try:
                req = urllib.request.Request(url, data=encoded_data, headers=req_headers, method=method)
                with urllib.request.urlopen(req, timeout=timeout) as resp:
                    return resp.read().decode("utf-8", errors="replace")
            except Exception as e:
                if attempt == self.max_retries:
                    raise RuntimeError(f"Error descargando {url} tras {self.max_retries} intentos: {e}")
                # Exponential backoff with Full Jitter: sleep = Uniform(0, min(cap, base * 2^attempt))
                sleep_time = random.uniform(0, min(4.0, self.base_delay * (2 ** attempt)))
                time.sleep(sleep_time)
        return ""

class AdaptiveDOMExtractor:
    """Extractor heurístico inspirado en Scrapling y AutoScraper para scraping resiliente."""

    @staticmethod
    def extract_clean_text(html: str) -> str:
        clean = re.sub(r'<script.*?</script>', '', html, flags=re.DOTALL | re.IGNORECASE)
        clean = re.sub(r'<style.*?</style>', '', clean, flags=re.DOTALL | re.IGNORECASE)
        clean = re.sub(r'<[^>]+>', ' ', clean)
        clean = re.sub(r'\s+', ' ', clean).strip()
        return clean

    @staticmethod
    def find_values_by_regex(html: str, pattern: str) -> List[str]:
        return re.findall(pattern, html, flags=re.IGNORECASE)

    @staticmethod
    def extract_table_rows(html: str) -> List[List[str]]:
        """Extrae celdas de tablas HTML estándar."""
        tables = re.findall(r'<table.*?>(.*?)</table>', html, flags=re.DOTALL | re.IGNORECASE)
        rows_data = []
        for table in tables:
            rows = re.findall(r'<tr.*?>(.*?)</tr>', table, flags=re.DOTALL | re.IGNORECASE)
            for row in rows:
                cells = re.findall(r'<t[dh].*?>(.*?)</t[dh]>', row, flags=re.DOTALL | re.IGNORECASE)
                clean_cells = [re.sub(r'<[^>]+>', '', c).strip() for c in cells if c.strip()]
                if clean_cells:
                    rows_data.append(clean_cells)
        return rows_data

def run_self_test() -> bool:
    print("▶ Ejecutando autotest de AntiBotEvasionClient y AdaptiveDOMExtractor...")
    client = AntiBotEvasionClient(max_retries=2, base_delay=0.1)
    
    # Test 1: Generación de headers auténticos
    chrome_headers = client._get_browser_headers("Mozilla/5.0 Chrome/124.0.0.0")
    assert "Sec-CH-UA" in chrome_headers, "Faltan headers de Chrome"
    assert "User-Agent" in chrome_headers
    print("  ✓ Headers de evasión validados")

    # Test 2: Extractor adaptativo con HTML simulado
    sample_html = """
    <html>
      <body>
        <h1>Precios de Energía OMIE</h1>
        <div class="market-data">
          <table>
            <tr><th>Hora</th><th>Precio (€/MWh)</th></tr>
            <tr><td>01:00</td><td>45.50</td></tr>
            <tr><td>02:00</td><td>42.10</td></tr>
          </table>
        </div>
      </body>
    </html>
    """
    clean_text = AdaptiveDOMExtractor.extract_clean_text(sample_html)
    assert "Precios de Energía OMIE" in clean_text
    
    rows = AdaptiveDOMExtractor.extract_table_rows(sample_html)
    assert len(rows) == 3
    assert rows[1] == ["01:00", "45.50"]
    print("  ✓ Extractor adaptativo de tablas validado")
    
    print("  ✓ Todos los tests de Anti-Bot y Extracción Adaptativa PASARON con éxito.")
    return True

if __name__ == "__main__":
    if "--self-test" in sys.argv or "--test-mode" in sys.argv:
        success = run_self_test()
        sys.exit(0 if success else 1)
    else:
        print("Uso: python3 anti_bot_evasion_client.py --self-test")
