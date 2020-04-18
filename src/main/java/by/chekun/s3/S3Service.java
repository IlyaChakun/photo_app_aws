package by.chekun.s3;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;

public interface S3Service {

    PutObjectResult uploadFile(String key, InputStream imageInputStream, ObjectMetadata metadata);

    S3Object downloadFile(String key);

}
