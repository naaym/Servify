package com.servify.provider.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProviderProfileResponse {
  Long id;
  String name;
  String email;
  String phone;
  String governorate;
  String delegation;
  Integer age;
  String serviceCategory;
  Double basePrice;
  String description;
  String profileImageUrl;
}
