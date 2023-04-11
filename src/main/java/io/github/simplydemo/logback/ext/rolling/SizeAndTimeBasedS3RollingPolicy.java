package io.github.simplydemo.logback.ext.rolling;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.time.Instant;
import java.util.List;


public class SizeAndTimeBasedS3RollingPolicy extends SizeAndTimeBasedRollingPolicy {

    private boolean bucketExists = false;
    private boolean createBucketIfNotExists = false;

    private String region;

    private String profile;

    private String bucket;

    private S3Client s3;

    private List<Bucket> buckets;

    private final S3Builder s3builder = new S3Builder();

    FileNamePattern fileNamePattern;

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
        try {
            this.fileNamePattern = new FileNamePattern("%d{yyyyMMdd}", context);
            this.s3 = s3builder.build(region, profile);
            this.buckets = s3.listBuckets().buckets();
            super.start();
        } catch (SdkException se) {
            System.err.println("Can not create S3Client instance.");
            se.printStackTrace();
            super.stop();
        }
    }

    public void stop() {
        // this.rollover();
        super.stop();
    }

    private void createBucket(String bucket) {
        final CreateBucketRequest request = CreateBucketRequest.builder().bucket(bucket).build();
        final CreateBucketResponse response = s3.createBucket(request);
        if (response.sdkHttpResponse().isSuccessful()) {
            System.out.println("Created bucket " + this.bucket);
        }
    }

    private void uploadFile(final File file, final String keyName) {
        System.out.println(String.format("upload file '%s' to s3 bucket '%s/%s'.", file.toPath(), bucket, keyName));
        // System.out.println("file.toPath(): " + file.toPath());
        // System.out.println("file.exists(): " + file.exists());
        // System.out.println("file.length(): " + file.length());
        // System.out.println("filename: " + file.getName());
        // System.out.println("file.getParent(): " + file.getParent());
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
        // System.out.println(response.eTag());
    }

    @Override
    public void rollover() {
        super.rollover();
        for (final Bucket buk : buckets) {
            if (buk.name().equals(bucket)) {
                bucketExists = true;
            }
        }
        // System.out.println("bucketExists: " + bucketExists);
        // System.out.println("bucket: " + bucket);

        if (createBucketIfNotExists && !bucketExists) {
            createBucket(bucket);
        }

        final String elapsedPeriodsFileName = super.getTimeBasedFileNamingAndTriggeringPolicy().getElapsedPeriodsFileName();
        final File file = new File(elapsedPeriodsFileName);

        if (file.exists()) {
            try {
                Instant now = Instant.ofEpochMilli(super.getTimeBasedFileNamingAndTriggeringPolicy().getCurrentTime());
                DateTokenConverter<Object> dtc = fileNamePattern.getPrimaryDateTokenConverter();
                final String keyName = new StringBuilder().append("collector/")
                        .append(dtc.convert(now))
                        .append("/")
                        .append(s3builder.generateS3KeyPrefix())
                        .append("-")
                        .append(file.getName())
                        .toString();
                // System.out.println("keyName: " + keyName);
                uploadFile(file, keyName);
            } catch (S3Exception se) {
                System.out.println("Caught an SdkClientException, " + "which means the client encountered " + "an internal error while trying to communicate with S3, " + "such as not being able to access the network.");
                System.out.println("Error Message: " + se.getMessage());
            } catch (AwsServiceException ase) {
                System.out.println("Caught an AwsServiceException, " + "which means your request made it " + "to Amazon S3, but was rejected with an error response " + "for some reason.");
                System.out.println("AWS Error Code:   " + ase.awsErrorDetails().errorCode());
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.statusCode());
                System.out.println("Request ID:       " + ase.requestId());
            } catch (SdkClientException ace) {
                System.out.println("Caught an SdkClientException, " + "which means the client encountered " + "an internal error while trying to communicate with S3, " + "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found. " + file.toPath());
        }

    }
}
