package com.zonbeozon.config;

import com.zonbeozon.global.image.S3Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

@Configuration
@Profile({"local", "test"})
public class MockS3Config {
    @Value("${mock.aws.host}")
    private String host;

    @Value("${mock.aws.s3.bucket}")
    private String bucket;

    @Value("${mock.aws.region}")
    private String region;

    @Value("${mock.aws.credentials.access-key}")
    private String accessKey;

    @Value("${mock.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .forcePathStyle(true)
                .endpointOverride(URI.create(String.format("http://%s/%s",host, bucket)))
                .build();
    }

    @Bean
    public S3Properties mockS3Properties() {
        return new S3Properties() {
            @Override
            public String getBucket() {
                return bucket;
            }

            @Override
            public String getEndpoint() {
                return String.format("http://%s/%s", host, bucket);
            }

            @Override
            public String getHost() {
                return host;
            }

            @Override
            public String getRegion() {
                return region;
            }
        };
    }
}

