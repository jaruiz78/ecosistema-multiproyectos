# 1. Build Stage
FROM golang:1.24-alpine AS builder

# Configurar CGO_ENABLED=0 para asegurar binario estático y Distroless compatibility
ENV CGO_ENABLED=0 \
    GOOS=linux \
    GOARCH=amd64

WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN go build -ldflags="-w -s" -o agentic-router ./cmd/router/main.go

# 2. Runtime Stage (Distroless / Scale-to-Zero optimized)
FROM gcr.io/distroless/static-debian11

WORKDIR /
COPY --from=builder /app/agentic-router /agentic-router

EXPOSE 8080
USER nonroot:nonroot

CMD ["/agentic-router"]
