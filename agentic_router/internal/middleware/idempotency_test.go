package middleware_test

import (
	"net/http"
	"net/http/httptest"
	"testing"
	"antigravity/agentic_router/internal/middleware"
)

func TestIdempotencyAndFinOps(t *testing.T) {
	handler := middleware.TracingAndIdempotencyMiddleware(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
	}))

	// Test 1: FinOps Violation (SELECT *)
	reqFinOps := httptest.NewRequest(http.MethodGet, "/skill?bq_query=SELECT%20*", nil)
	rrFinOps := httptest.NewRecorder()
	handler.ServeHTTP(rrFinOps, reqFinOps)

	if status := rrFinOps.Code; status != http.StatusPaymentRequired {
		t.Errorf("El filtro FinOps falló. Código esperado %v, obtenido %v", http.StatusPaymentRequired, status)
	}

	// Test 2: Valid Request
	reqValid := httptest.NewRequest(http.MethodPost, "/skill/execute", nil)
	rrValid := httptest.NewRecorder()
	handler.ServeHTTP(rrValid, reqValid)

	if status := rrValid.Code; status != http.StatusOK {
		t.Errorf("El ruteo válido falló. Código esperado %v, obtenido %v", http.StatusOK, status)
	}
}
