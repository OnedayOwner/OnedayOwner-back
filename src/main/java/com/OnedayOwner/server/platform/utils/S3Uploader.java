package com.OnedayOwner.server.platform.utils;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class S3Uploader {

    private static final Logger log = LoggerFactory.getLogger(S3Uploader.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    private AmazonS3 s3Client;

    @PostConstruct
    public void initializeS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String uploadFileAndReturnUrl(File file, String type, Long id) {
        String s3Key = type + "/" + id + "/" + file.getName();

        try {
            s3Client.putObject(new PutObjectRequest(bucket, s3Key, file));
        } catch (Exception e) {
            log.error("Failed to upload {} to S3: {}", file.getName(), e.getMessage());
            throw new BusinessException(ErrorCode.S3_UPLOAD_FAILED);
        }
        String url = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + s3Key;
        System.out.println("url = " + url);
        return url;
    }
}