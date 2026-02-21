#!/usr/bin/env bash
set -e

POD_NAME=bakery

if podman pod exists $POD_NAME; then
  echo "Pod already exists"
else
  podman pod create \
    --name $POD_NAME \
    -p 5432:5432 \
    -p 3000:3000
fi
