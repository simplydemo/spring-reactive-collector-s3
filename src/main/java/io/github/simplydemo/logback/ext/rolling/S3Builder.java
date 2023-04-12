package io.github.simplydemo.logback.ext.rolling;

import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.util.Random;

public class S3Builder {

    private static final Random RAND = new Random();

    public String generateS3KeyPrefix() {
        int idx1 = RAND.nextInt(97, 122);
        int idx2 = RAND.nextInt(97, 122);
        int idx3 = RAND.nextInt(97, 122);
        return new StringBuilder(3)
                .append((char) idx1)
                .append((char) idx2)
                .append((char) idx3)
                .toString();
    }

    public S3Client build(final String region) {
        return build(region, null);
    }

    public S3Client build(final String regionName, final String profile) {
        final Region region = Region.of(regionName);
        final AwsClientBuilder<S3ClientBuilder, S3Client> builder = S3Client.builder().region(region);
        if (StringUtils.hasText(profile)) {
            builder.credentialsProvider(ProfileCredentialsProvider.create());
        } else {
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }
        return builder.build();
    }

}
