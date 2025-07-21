#!/bin/bash

SCRIPT_DIR=$(dirname "$0")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

sudo docker buildx build \
  --platform linux/amd64 \
  -t aaasssk/zonbeozon:latest \
  -f "${PROJECT_ROOT}/Dockerfile" \
  "${PROJECT_ROOT}"

sudo docker push aaasssk/zonbeozon:latest