package com.servify.provider.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StorageFilesServiceImpl implements StorageFilesService {
  private final Cloudinary cloudinary;

  @Override
  public String store(MultipartFile file, String folder) {
    validatePdf(file);
    return uploadFile(file, folder, "raw");
  }

  @Override
  public String storeImage(MultipartFile file, String folder) {
    validateImage(file);
    return uploadFile(file, folder, "image");
  }

  private String uploadFile(MultipartFile file, String folder, String resourceType) {
    try {
      Map<String, Object> options = new HashMap<>();
      options.put("folder", folder);
      options.put("resource_type", resourceType);
      options.put("access_mode", "public");

      Map<?, ?> uploadResult = cloudinary.uploader().upload(
              file.getBytes(),
              options
      );

      return uploadResult.get("secure_url").toString();

    } catch (Exception e) {
      throw new RuntimeException("Cloudinary upload failed", e);
    }
  }

  private void validatePdf(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    String contentType = file.getContentType();
    if (!"application/pdf".equals(contentType)) {
      throw new IllegalArgumentException(" Only PDF files supported ");
    }

    long maxSize = 5 * 1024 * 1024;
    if (file.getSize() > maxSize) {
      throw new IllegalArgumentException("File too large");
    }
  }

  private void validateImage(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    String contentType = file.getContentType();
    if (contentType == null ||
            (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
      throw new IllegalArgumentException("Only JPEG or PNG images supported");
    }

    long maxSize = 5 * 1024 * 1024;
    if (file.getSize() > maxSize) {
      throw new IllegalArgumentException("File too large");
    }
  }
}
