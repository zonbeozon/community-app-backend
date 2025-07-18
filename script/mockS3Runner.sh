#!/bin/bash

# --- S3Mock Docker 컨테이너 환경 변수 설정 ---
# S3Mock이 사용할 포트
S3MOCK_HTTP_PORT=9090
S3MOCK_HTTPS_PORT=9191

# S3Mock이 시작될 때 자동으로 생성할 버킷 목록
INITIAL_S3_BUCKETS="mock-bucket"

# Docker 컨테이너 이름
CONTAINER_NAME="s3mock-dev-server"

# Docker 이미지 이름
DOCKER_IMAGE="adobe/s3mock"

echo "Stopping and removing existing Docker container if it exists..."
docker stop ${CONTAINER_NAME} > /dev/null 2>&1
docker rm ${CONTAINER_NAME} > /dev/null 2>&1

# --- S3Mock Docker 컨테이너 실행 ---
echo "Starting S3Mock Docker container '${CONTAINER_NAME}'..."
docker run \
  -p ${S3MOCK_HTTP_PORT}:${S3MOCK_HTTP_PORT} \
  -p ${S3MOCK_HTTPS_PORT}:${S3MOCK_HTTPS_PORT} \
  --name ${CONTAINER_NAME} \
  -e COM_ADOBE_TESTING_S3MOCK_STORE_INITIALBUCKETS="${INITIAL_S3_BUCKETS}" \
  -d \
  ${DOCKER_IMAGE}

echo "S3Mock container '${CONTAINER_NAME}' started."
echo "HTTP Endpoint: http://localhost:${S3MOCK_HTTP_PORT}"
echo "HTTPS Endpoint: https://localhost:${S3MOCK_HTTPS_PORT}"
echo "Initial buckets created: ${INITIAL_S3_BUCKETS}"

echo "Script finished."