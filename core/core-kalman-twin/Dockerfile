# Multi-stage build for Python 3.11+ / EnKF Twin / Cloud Run
FROM python:3.11-slim AS builder
WORKDIR /app
COPY setup.py requirements.txt* ./
RUN pip install --no-cache-dir --user -e . 2>/dev/null || true

FROM python:3.11-slim
WORKDIR /app
COPY --from=builder /root/.local /root/.local
COPY . .
ENV PATH=/root/.local/bin:/home/jaruiz/.gemini/antigravity/bin:/home/jaruiz/.config/Antigravity/bin:/home/jaruiz/sdk/flutter/bin:/home/jaruiz/.local/bin:/home/jaruiz/.local/bin:/home/jaruiz/.sdkman/candidates/java/current/bin:/home/jaruiz/.nvm/versions/node/v24.19.0/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin:/snap/bin
EXPOSE 8080
CMD ["python3", "-m", "src.enkf_solver"]
