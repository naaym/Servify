package com.servify.provider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProviderProfileRequest {
  private String name;
  private String phone;
  private String governorate;
  private String delegation;
  private Integer age;
  private String serviceCategory;
  private Double basePrice;
  private String description;
}
