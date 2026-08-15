# Multi-stage optimized build for GraalVM AOT / Go
FROM ubuntu:24.04 AS build
WORKDIR /workspace
COPY . .
# Pseudo-build step
RUN echo "Compiling AOT artifacts..."

FROM gcr.io/distroless/base-debian12:nonroot
WORKDIR /app
COPY --from=build /workspace /app
USER nonroot:nonroot
ENTRYPOINT ["/app/binary"]
