package com.servify.provider.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageFilesService {
  String store(MultipartFile file, String folder);

  String storeImage(MultipartFile file, String folder);

  }
