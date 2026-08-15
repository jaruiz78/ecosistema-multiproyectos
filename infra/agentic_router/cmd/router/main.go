// Arquitectura y especificación formal para main.go.
//
// Referencias de Ingeniería:
//   - ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md
//   - Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_software_engineering/01_arquitectura_hexagonal_ddd_puro.md
//   - Referencia Académica: Martin (2017) Clean Architecture & DDD Pure Domain Standard
package main

import (
	"log"
	"net/http"
	"antigravity/agentic_router/internal/middleware"
)

func main() {
	mux := http.NewServeMux()

	// Handler base que simula la invocación de la Skill
	skillHandler := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("Skill executed successfully within limits.\n"))
	})

	// Wrap con el Middleware
	mux.Handle("/skill", middleware.TracingAndIdempotencyMiddleware(skillHandler))

	log.Println("🚀 Agentic Router (CSP Pure) escuchando en :8080...")
	if err := http.ListenAndServe(":8080", mux); err != nil {
		log.Fatalf("Error iniciando servidor: %v", err)
	}
}
