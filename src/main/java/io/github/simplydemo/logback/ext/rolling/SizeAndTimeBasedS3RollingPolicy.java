package io.github.simplydemo.logback.ext.rolling;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.time.Instant;

public class SizeAndTimeBasedS3RollingPolicy<E> extends SizeAndTimeBasedRollingPolicy<E> {

    private String region;

    private String profile;

    private String bucket;

    private S3Client s3;

    private final S3Builder s3builder = new S3Builder();

    FileNamePattern fileNamePattern;

    public SizeAndTimeBasedS3RollingPolicy() {
        super();
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void start() {
        addInfo("AWS REGION: " + region);
        addInfo("AWS BUCKET: " + bucket);
        addInfo("AWS PROFILE: " + profile);
        try {
            this.fileNamePattern = new FileNamePattern("%d{yyyyMMdd}", context);
            this.s3 = s3builder.build(region, profile);
            // this.buckets = s3.listBuckets().buckets();
            super.start();
        } catch (SdkException se) {
            addError("Can not create S3Client instance.", se);
            super.stop();
        }
    }

    public void stop() {
        // this.rollover();
        super.stop();
    }

    private void uploadFile(final File file, final String keyName) {
        System.out.printf("upload file '%s' to s3 bucket '%s/%s'.%n", file.toPath(), bucket, keyName);
        // addInfo("file.toPath(): " + file.toPath());
        // addInfo("file.exists(): " + file.exists());
        // addInfo("file.length(): " + file.length());
        // addInfo("filename: " + file.getName());
        // addInfo("file.getParent(): " + file.getParent());
        final PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(keyName).build();
        s3.putObject(request, file.toPath());
        // PutObjectResponse response = s3.putObject(request, file.toPath());
        // response.versionId()
        // response.eTag();
        // response.sdkHttpResponse().isSuccessful();
        // response.sdkHttpResponse().headers()
        // response.sdkHttpResponse().statusCode()
        // response.sdkHttpResponse().statusText().get()
        // addInfo(response.eTag());
    }

    @Override
    public void rollover() {
        super.rollover();

        final String elapsedPeriodsFileName = super.getTimeBasedFileNamingAndTriggeringPolicy().getElapsedPeriodsFileName();
        final File file = new File(elapsedPeriodsFileName);

        if (file.exists()) {
            try {
                Instant now = Instant.ofEpochMilli(super.getTimeBasedFileNamingAndTriggeringPolicy().getCurrentTime());
                DateTokenConverter<Object> dtc = fileNamePattern.getPrimaryDateTokenConverter();
                final String keyName = "collector/" +
                        dtc.convert(now) +
                        "/" +
                        // s3builder.generateS3KeyPrefix() +
                        // "-" +
                        file.getName();
                // addInfo("keyName: " + keyName);
                uploadFile(file, keyName);
            } catch (S3Exception | SdkClientException se) {
                addInfo("Caught an SdkClientException, " + "which means the client encountered " + "an internal error while trying to communicate with S3, " + "such as not being able to access the network.");
                addInfo("Error Message: " + se.getMessage());
            } catch (AwsServiceException ase) {
                addInfo("Caught an AwsServiceException, " + "which means your request made it " + "to Amazon S3, but was rejected with an error response " + "for some reason.");
                addInfo("AWS Error Code:   " + ase.awsErrorDetails().errorCode());
                addInfo("Error Message:    " + ase.getMessage());
                addInfo("HTTP Status Code: " + ase.statusCode());
                addInfo("Request ID:       " + ase.requestId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            addInfo("File not found. " + file.toPath());
        }

    }
}
