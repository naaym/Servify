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

    validateFile(file);

    try {
      Map<String, Object> options = new HashMap<>();
      options.put("folder", folder);
      options.put("resource_type", "raw");
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

  private void validateFile(MultipartFile file) {
    String contentType = file.getContentType();
    if (!contentType.equals("application/pdf")) {
      throw new IllegalArgumentException(" Only PDF files supported ");
    }
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    long maxSize = 5 * 1024 * 1024;
    if (file.getSize() > maxSize) {
      throw new IllegalArgumentException("File too large");
    }
  }
}
