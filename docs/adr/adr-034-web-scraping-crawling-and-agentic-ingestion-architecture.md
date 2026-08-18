# ADR-034: Arquitectura Unificada de Web Scraping, Crawling Masivo, Evasión Anti-Bot y Extracción Documental

## Estado
Aprobado

## Contexto
El ecosistema corporativo requiere ingestar continuamente grandes volúmenes de datos no estructurados y semi-estructurados:
1. **Conocimiento Académico y Normativo (12 Facultades)**: Papers arXiv/ACM/IEEE en PDF, estándares RFC, documentación web y cuadernos pedagógicos Feynman.
2. **Fuentes Externas de Negocio para Verticales**: Precios de casación marginal en pools eléctricos (OMIE), telemetría agroclimática (AEMET/Copernicus), licitaciones públicas (B2G) y fletes logísticos.
3. **Validación E2E y Pruebas Autónomas de Interfaces**: Verificación de flujos complejos en SPAs (`SaaSRegantes`) y aplicaciones móviles (`AppViajes`).

Depender de servicios propietarios en la nube (APIs de pago por llamada) viola la política FinOps ($< 0.015$ USD/MAU/mes). Asimismo, los cambios frecuentes en el DOM y la protección WAF (Cloudflare/Akamai/Datadome) causan fallos en scrapers convencionales.

## Decisión
Adoptar de forma nativa e integrada el ecosistema de 10 herramientas especializadas de código abierto:

1. **Ingesta LLM & RAG**: `Crawl4AI` y `Firecrawl` para convertir URLs completas a Markdown limpio sin ruido.
2. **Parseo Universal Multiformato**: `MarkItDown` de Microsoft para extracción directa de PDFs, documentos Office, HTML y metadatos hacia el pipeline RAG de las 12 Facultades.
3. **Evasión de WAFs y Bloqueos de Red**: `curl-impersonate` para emulación precisa de handshakes TLS/JA3/JA4 y cabeceras HTTP/2 de Chrome/Firefox, evitando detección a nivel de transporte.
4. **Resiliencia ante Cambios de DOM**: `Scrapling` y `AutoScraper` para inferencia automática y selectores adaptativos.
5. **Crawling Masivo $O(N)$**: `Scrapy` y `Crawlee` para pipelines paralelos out-of-core.
6. **Agentes de Navegación y UI Testing**: `Browser-Use` con Playwright para pruebas autónomas de flujos de usuario complejos.
7. **Control y Telemetría Móvil**: `scrcpy` con ADB para automatización y recolección de métricas en dispositivos/emuladores Android locales.

## Consecuencias
* **Positivas**: Coste por página $0 USD; bypass determinista de bloqueos anti-bot en fuentes públicas; ingesta RAG multiformato de alta fidelidad; pruebas E2E autónomas sin flakiness.
* **Negativas / Mitigaciones**: Requiere emulación controlada de cabeceras y respeto estricto de tasas de peticiones (rate limiting ético con jitter exponencial).
