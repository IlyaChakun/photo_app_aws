package by.chekun.s3.impl;

import by.chekun.s3.S3Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3ServiceImpl implements S3Service {

    @Value("${aws.services.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3Client;

    @Autowired
    public S3ServiceImpl(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public PutObjectResult uploadFile(String key, InputStream imageInputStream, ObjectMetadata metadata) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, imageInputStream, metadata);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        return this.amazonS3Client.putObject(putObjectRequest);
    }

    @Override
    public S3Object downloadFile(String key) {
        return amazonS3Client.getObject(bucketName, key);
    }

}