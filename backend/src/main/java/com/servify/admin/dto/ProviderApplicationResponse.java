package com.servify.admin.dto;

import com.servify.provider.model.ProviderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ProviderApplicationResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String governorate;
    private String delegation;
    private int age;
    private ProviderStatus status;
    private String cinUrl;
    private String cvUrl;
    private String diplomeUrl;
    private Instant createdAt;
    private String cinName;
    private String cvName;
    private String diplomeName;
}
