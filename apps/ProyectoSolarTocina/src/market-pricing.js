/**
 * Integración con Precios Dinámicos del Mercado Eléctrico Español (OMIE / PVPC / ESIOS REE)
 * Derivado de ProyectoVPP para arbitraje económico de baterías y modulación solar.
 */

export class MarketPricingService {
  constructor() {
    this.cacheKey = 'pvpc_market_prices';
    this.cachedPrices = null;
  }

  /**
   * Obtiene los precios horarios reales del mercado regulado (PVPC / OMIE) para España Peninsular
   */
  async fetchHourlyPrices() {
    try {
      const resp = await fetch('https://api.preciodelaluz.online/v1/prices/all?zone=PCB');
      if (resp.ok) {
        const data = await resp.json();
        const prices = this.parsePvpcApiResponse(data);
        if (prices.length === 24) {
          this.cachedPrices = prices;
          return prices;
        }
      }
    } catch (e) {
      console.warn('API PVPC online no disponible, utilizando curva estacional calibrada de OMIE:', e);
    }

    return this.getFallbackOmiePrices();
  }

  parsePvpcApiResponse(data) {
    const hourly = [];
    for (let h = 0; h < 24; h++) {
      const key = `${h.toString().padStart(2, '0')}-${(h+1).toString().padStart(2, '0')}`;
      const item = data[key];
      if (item && item.price !== undefined) {
        // La API devuelve precio en €/MWh o €/kWh
        const priceEurKwh = item.price > 1 ? item.price / 1000.0 : item.price;
        hourly.push({
          hour: h,
          priceEurKwh: Math.max(0.01, priceEurKwh),
          isCheap: item['is-cheap'] || false,
          isUnderAvg: item['is-under-avg'] || false,
          source: 'OMIE / PVPC Live API'
        });
      }
    }
    return hourly;
  }

  getFallbackOmiePrices() {
    // Curva típica horaria del mercado ibérico (horas solares 11:00-17:00 muy baratas, picos noche 20:00-22:00)
    const baseCurve = [
      0.085, 0.078, 0.072, 0.070, 0.071, 0.079,
      0.098, 0.135, 0.148, 0.142, 0.095, 0.065,
      0.045, 0.038, 0.035, 0.040, 0.055, 0.085,
      0.140, 0.195, 0.225, 0.210, 0.155, 0.105
    ];

    return baseCurve.map((price, h) => ({
      hour: h,
      priceEurKwh: price,
      isCheap: price < 0.075,
      isUnderAvg: price < 0.110,
      source: 'OMIE Climatología de Precios'
    }));
  }

  getBestHoursSummary(prices) {
    if (!prices || !prices.length) return null;
    const sorted = [...prices].sort((a, b) => a.priceEurKwh - b.priceEurKwh);
    const cheapest = sorted.slice(0, 3);
    const mostExpensive = sorted.slice(-3).reverse();
    const avgPrice = prices.reduce((acc, p) => acc + p.priceEurKwh, 0) / prices.length;

    return {
      cheapestHours: cheapest.map(p => `${p.hour.toString().padStart(2, '0')}:00 (${(p.priceEurKwh*100).toFixed(1)} c€/kWh)`).join(', '),
      expensiveHours: mostExpensive.map(p => `${p.hour.toString().padStart(2, '0')}:00 (${(p.priceEurKwh*100).toFixed(1)} c€/kWh)`).join(', '),
      averagePriceEurKwh: avgPrice,
      poolSpreadEurKwh: mostExpensive[0].priceEurKwh - cheapest[0].priceEurKwh
    };
  }
}
