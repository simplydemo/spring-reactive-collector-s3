package io.github.simplydemo.logback.ext.rolling;

import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.auth.credentials.internal.LazyAwsCredentialsProvider;
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
            builder.credentialsProvider(credentialsProvider(false, true));
        }
        final S3Client s3 = builder.build();
        return s3;
    }

    private AwsCredentialsProvider credentialsProvider(boolean asyncCredentialUpdateEnabled, boolean reuseLastProviderEnabled) {
        return LazyAwsCredentialsProvider.create(() -> {
            final AwsCredentialsProvider[] credentialsProviders = new AwsCredentialsProvider[]{EnvironmentVariableCredentialsProvider.create(), WebIdentityTokenFileCredentialsProvider.create(), ((ContainerCredentialsProvider.Builder) ContainerCredentialsProvider.builder().asyncCredentialUpdateEnabled(asyncCredentialUpdateEnabled)).build(), ((InstanceProfileCredentialsProvider.Builder) InstanceProfileCredentialsProvider.builder().asyncCredentialUpdateEnabled(asyncCredentialUpdateEnabled)).build()};
            return AwsCredentialsProviderChain.builder().reuseLastProviderEnabled(reuseLastProviderEnabled).credentialsProviders(credentialsProviders).build();
        });

    }

}
