#!/bin/bash

sudo docker run -it -p 9090:9090 -e COM_ADOBE_TESTING_S3MOCK_STORE_INITIAL_BUCKETS="mock-bucket" adobe/s3mock bash