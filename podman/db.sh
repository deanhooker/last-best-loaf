#!/usr/bin/env bash
set -e

if podman container exists bakery-db; then
  echo "DB container exists"
else
  podman volume create postgres_data || true

  podman run -d \
    --name bakery-db \
    --pod bakery \
    -e POSTGRES_USER=bakery \
    -e POSTGRES_PASSWORD=bakery \
    -e POSTGRES_DB=bakery \
    -v postgres_data:/var/lib/postgresql \
    docker.io/library/postgres:18
fi
