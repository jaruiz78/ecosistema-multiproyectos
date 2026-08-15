package main

// 05_lab_token_bucket_rate_limiter.go
// -------------------------------------------------------------------------
// Laboratorio Práctico Feynman: Control de Tasa O(1) con Algoritmo Token Bucket
// Implementación pura y sin temporizadores en segundo plano (Zero-Thread-Pinning)
// utilizada en la protección de cuotas de APIs de GCP y TaxiCaller.
// -------------------------------------------------------------------------

import (
	"fmt"
	"math"
	"sync"
	"time"
)

type TokenBucket struct {
	mu         sync.Mutex
	capacity   float64   // Capacidad máxima de tokens (Ráfaga)
	refillRate float64   // Tokens añadidos por segundo
	tokens     float64   // Tokens actuales en el cubo
	lastRefill time.Time // Última vez que se recalcularon los tokens
}

func NewTokenBucket(capacity float64, refillRate float64) *TokenBucket {
	return &TokenBucket{
		capacity:   capacity,
		refillRate: refillRate,
		tokens:     capacity,
		lastRefill: time.Now(),
	}
}

// Allow evalúa en O(1) tiempo constante si la petición puede pasar, recalculando
// los tokens matemáticamente sin necesidad de un hilo/goroutine en bucle.
func (tb *TokenBucket) Allow(cost float64) bool {
	tb.mu.Lock()
	defer tb.mu.Unlock()

	now := time.Now()
	elapsed := now.Sub(tb.lastRefill).Seconds()
	tb.lastRefill = now

	// 1. Regeneración matemática de tokens: tokens = min(capacity, tokens + elapsed * rate)
	tb.tokens = math.Min(tb.capacity, tb.tokens+(elapsed*tb.refillRate))

	// 2. Consumo de tokens
	if tb.tokens >= cost {
		tb.tokens -= cost
		return true
	}
	return false
}

func main() {
	fmt.Println("====================================================================")
	fmt.Println("  🧪 LAB FEYNMAN 05: CONTROL DE TASA O(1) TOKEN BUCKET RATE LIMITER")
	fmt.Println("====================================================================")

	// Capacidad: 5 peticiones de ráfaga, Tasa: 2 peticiones por segundo
	limiter := NewTokenBucket(5.0, 2.0)

	fmt.Println("🌊 Disparando ráfaga instantánea de 7 peticiones:")
	for i := 1; i <= 7; i++ {
		allowed := limiter.Allow(1.0)
		status := "🟢 PERMITIDA (200 OK)"
		if !allowed {
			status = "🔴 BLOQUEADA (429 Too Many Requests)"
		}
		fmt.Printf("   Petición #%d -> %s\n", i, status)
	}

	fmt.Println("\n⏳ Esperando 1.5 segundos para regenerar tokens...")
	time.Sleep(1500 * time.Millisecond)

	fmt.Println("🌊 Disparando nueva petición tras recarga:")
	for i := 8; i <= 10; i++ {
		allowed := limiter.Allow(1.0)
		status := "🟢 PERMITIDA (200 OK)"
		if !allowed {
			status = "🔴 BLOQUEADA (429 Too Many Requests)"
		}
		fmt.Printf("   Petición #%d -> %s\n", i, status)
	}

	fmt.Println("--------------------------------------------------------------------")
	fmt.Println("✓ Algoritmo Token Bucket evaluado en O(1) y cero consumo de hilos.")
	fmt.Println("🧠 Explicación Feynman: Un cubo con un grifo que gotea fichas constantemente.")
	fmt.Println("   Para pasar por la puerta necesitas una ficha. Si el cubo se vacía, debes")
	fmt.Println("   esperar a que caiga la siguiente gota.")
	fmt.Println("====================================================================")
}
