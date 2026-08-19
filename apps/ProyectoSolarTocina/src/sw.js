const CACHE_NAME = 'solar-tocina-v3.3';
const STATIC_ASSETS = [
  '/',
  '/index.html',
  '/styles.css',
  '/app.js',
  '/battery-soh-diagnostic.js',
  '/icp-power-optimizer-ui.js',
  '/valley-charge-scheduler-ui.js',
  '/manifest.json',
  '/vendor/marked.min.js',
  '/vendor/katex.min.css',
  '/vendor/katex.min.js',
  '/vendor/mermaid.min.js',
  '/solar-engine.js',
  '/weather-api.js',
  '/kalman-filter.js',
  '/mpc-optimizer.js',
  '/virtual-battery.js',
  '/appliance-recommender.js',
  '/market-pricing.js',
  '/h3-spatial-grid.js',
  '/green-ledger.js',
  '/mobility-planner.js',
  '/power-flow-canvas.js',
  '/what-if-simulator.js',
  '/solar-dialog-assistant.js',
  '/thermal-precooling-engine.js',
  '/tariff-contract-comparator.js',
  '/mobility-sync-appviajes.js',
  '/green-passport-crypto.js',
  '/solar-push-notifications.js',
  '/historical-analytics.js'
];

self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(cache => {
      return cache.addAll(STATIC_ASSETS).catch(err => {
        console.warn('[SW] Cache prefetch warn:', err);
      });
    })
  );
  self.skipWaiting();
});

self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(keys => {
      return Promise.all(
        keys.filter(k => k !== CACHE_NAME).map(k => caches.delete(k))
      );
    })
  );
  self.clients.claim();
});

self.addEventListener('fetch', event => {
  const url = new URL(event.request.url);
  
  // No cachear endpoints dinámicos de API ni eventos SSE
  if (url.pathname.startsWith('/api/') || url.pathname === '/events' || event.request.method !== 'GET') {
    return;
  }

  event.respondWith(
    caches.match(event.request).then(cached => {
      const fetchPromise = fetch(event.request).then(networkResponse => {
        if (networkResponse && networkResponse.status === 200) {
          const resClone = networkResponse.clone();
          caches.open(CACHE_NAME).then(cache => cache.put(event.request, resClone));
        }
        return networkResponse;
      }).catch(() => cached);

      return cached || fetchPromise;
    })
  );
});
