package by.chekun.service;

import by.chekun.payload.DownloadImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String upload(MultipartFile file);

    DownloadImageResponse download(String key);

    DownloadImageResponse downloadRandom();
}
