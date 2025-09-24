package com.zonbeozon.config;

import com.zonbeozon.image.ImageS3Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@Profile("mocks3")
public class MockImageS3Config {
    @Value("${mock.aws.s3.image.host.docker}")
    private String dockerHost;

    @Value("${mock.aws.s3.image.host.external}")
    private String externHost;

    @Value("${mock.aws.s3.image.bucket}")
    private String bucket;

    @Value("${mock.aws.s3.image.region}")
    private String region;

    @Value("${mock.aws.s3.image.credentials.access-key}")
    private String accessKey;

    @Value("${mock.aws.s3.image.credentials.secret-key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .forcePathStyle(true)
                .endpointOverride(URI.create(String.format("http://" + dockerHost)))
                .build();
    }

    @Bean
    public ImageS3Properties imageS3Properties() {
        return new ImageS3Properties() {
            @Override
            public String getBucket() {
                return bucket;
            }

            @Override
            public String getEndpoint() {
                return String.format("http://%s/%s", externHost, bucket);
            }

            @Override
            public String getHost() {
                return externHost;
            }

            @Override
            public String getRegion() {
                return region;
            }
        };
    }
}

