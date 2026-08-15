# Kata 04: Go Race Detector

## Objetivo
Garantizar concurrencia sin Data Races en Golang workers.

## Reglas
- Siempre correr tests con `go test -race`.
- Evitar mutabilidad compartida. Utilizar canales o sync.Mutex.
