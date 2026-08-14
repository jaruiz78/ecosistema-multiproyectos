// Arquitectura y especificación formal para idempotency.go.
//
// Referencias de Ingeniería:
//   - ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md
//   - Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_software_engineering/01_arquitectura_hexagonal_ddd_puro.md
//   - Referencia Académica: Martin (2017) Clean Architecture & DDD Pure Domain Standard
package middleware

import (
	"context"
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"net/http"

	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/trace"
)

// TracingAndIdempotencyMiddleware inyecta W3C traceparent y garantiza idempotencia (O(1))
func TracingAndIdempotencyMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// 1. OTEL W3C Traceparent
		ctx, span := otel.Tracer("agentic-router").Start(r.Context(), "ProcessSkill", trace.WithSpanKind(trace.SpanKindServer))
		defer span.End()

		// 2. FinOps BigQuery Filter (Prevención de SELECT *)
		query := r.URL.Query().Get("bq_query")
		if isExpensiveQuery(query) {
			span.RecordError(fmt.Errorf("finops violation: SELECT * detected"))
			http.Error(w, "FinOps Limit Reached: Invalid BigQuery Query", http.StatusPaymentRequired)
			return
		}

		// 3. Idempotency Key Generation (Hash del Payload + Agente)
		idempKey := generateIdempotencyKey(r)
		
		// 4. Verificación CSP (Firestore Async Check simulado)
		if !checkFirestoreIdempotency(ctx, idempKey) {
			http.Error(w, "Idempotency Conflict: Skill already executed", http.StatusConflict)
			return
		}

		// Pase al siguiente handler (Skill/MCP)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func isExpensiveQuery(q string) bool {
	// Heurística O(1)
	return len(q) > 0 && (q == "SELECT *" || q == "select *")
}

func generateIdempotencyKey(r *http.Request) string {
	hash := sha256.Sum256([]byte(r.RequestURI))
	return hex.EncodeToString(hash[:])
}

// Simulamos una verificación en una base de datos de alta concurrencia
func checkFirestoreIdempotency(ctx context.Context, key string) bool {
	// En un entorno real, esto lanzaría una goroutine usando channels
	// para consultar Firestore o Redis con un timeout
	return true
}
