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
