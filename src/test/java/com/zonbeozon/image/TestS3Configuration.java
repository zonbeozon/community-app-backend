package com.zonbeozon.image;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Configuration
public class TestS3Configuration {
    @Bean
    public S3Client stubS3Client() {
        return new S3Client() {

            @Override
            public PutObjectResponse putObject(PutObjectRequest putObjectRequest, RequestBody requestBody) throws AwsServiceException, SdkClientException, S3Exception {
                return PutObjectResponse.builder().build();
            }

            @Override
            public String serviceName() {
                return "test-s3-service";
            }

            @Override
            public void close() {

            }
        };
    }

    @Bean
    public S3Properties s3Properties() {
        return new S3Properties() {
            @Override
            public String getBucket() {
                return "test-bucket";
            }

            @Override
            public String getEndpoint() {
                return "http://localhost:9090/test-bucket";
            }

            @Override
            public String getHost() {
                return "localhost:9090";
            }

            @Override
            public String getRegion() {
                return "us-east-1";
            }
        };
    }
}
