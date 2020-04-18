package by.chekun.service.impl;

import by.chekun.entity.ImageMetadata;
import by.chekun.exception.ServiceException;
import by.chekun.payload.DownloadImageResponse;
import by.chekun.repository.ImageMetadataRepository;
import by.chekun.s3.S3Service;
import by.chekun.service.ImageService;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageMetadataRepository imageMetadataRepository;
    private final S3Service s3Service;

    @Autowired
    public ImageServiceImpl(ImageMetadataRepository imageMetadataRepository,
                            S3Service s3Service) {
        this.imageMetadataRepository = imageMetadataRepository;
        this.s3Service = s3Service;
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            String key = multipartFile.getOriginalFilename();
            ObjectMetadata objectMetadata = getObjectMetadata(multipartFile);
            PutObjectResult putObjectResult = s3Service.uploadFile(key, multipartFile.getInputStream(), (ObjectMetadata) null);
            saveImageMetadata(key, objectMetadata, putObjectResult);
            return key;
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e, 400);
        }
    }

    private void saveImageMetadata(String key, ObjectMetadata objectMetadata, PutObjectResult putObjectResult) {
        ImageMetadata imageMetadata = imageMetadataRepository.findByKey(key).orElse(new ImageMetadata());
        imageMetadata.setKey(key);
        imageMetadata.setContentLength(objectMetadata.getContentLength());
        imageMetadata.setContentType(objectMetadata.getContentType());
        imageMetadata.seteTag(putObjectResult.getETag());
        imageMetadataRepository.save(imageMetadata);
    }

    public ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(multipartFile.getSize());
        metaData.setContentType(multipartFile.getContentType());
        return metaData;
    }

    @Override
    public DownloadImageResponse download(String key) {
        try {
            S3Object s3Object = s3Service.downloadFile(key);
            setImageMetadataToS3Object(key, s3Object);
            return getImageResponse(s3Object);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e, 500);
        }
    }

    private void setImageMetadataToS3Object(String key, S3Object s3Object) {
        ImageMetadata imageMetadata = imageMetadataRepository.findByKey(key)
                .orElseThrow(() -> new ServiceException("No item with key=" + key, 404));
        ObjectMetadata objectMetadata = s3Object.getObjectMetadata();
        objectMetadata.setContentLength(imageMetadata.getContentLength());
        objectMetadata.setContentType(imageMetadata.getContentType());
        objectMetadata.setLastModified(imageMetadata.getLastModified());
    }

    private DownloadImageResponse getImageResponse(S3Object s3Object) throws IOException {
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte[] content = StreamUtils.copyToByteArray(inputStream);
        String contentType = s3Object.getObjectMetadata().getContentType();
        return new DownloadImageResponse(s3Object.getKey(), content, contentType);
    }

    @Override
    public DownloadImageResponse downloadRandom() {
        long id = getRandomId();
        ImageMetadata imageMetadata = imageMetadataRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Random breaks", 500));
        return this.download(imageMetadata.getKey());
    }

    private Long getRandomId() {
        long countAll = imageMetadataRepository.count();
        long bound = countAll + 1;
        final long origin = 1;
        return ThreadLocalRandom.current().nextLong(origin, bound);
    }
}
