package com.servify.provider.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProviderRegistrationRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotBlank
    @Size(max = 100)
    private String governorate;

    @NotBlank
    @Size(max = 100)
    private String delegation;

    @NotNull
    @Min(18)
    private Integer age;

    @NotNull
    private MultipartFile cin;

    @NotNull
    private MultipartFile cv;

    @NotNull
    private MultipartFile diplome;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getDelegation() {
        return delegation;
    }

    public void setDelegation(String delegation) {
        this.delegation = delegation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public MultipartFile getCin() {
        return cin;
    }

    public void setCin(MultipartFile cin) {
        this.cin = cin;
    }

    public MultipartFile getCv() {
        return cv;
    }

    public void setCv(MultipartFile cv) {
        this.cv = cv;
    }

    public MultipartFile getDiplome() {
        return diplome;
    }

    public void setDiplome(MultipartFile diplome) {
        this.diplome = diplome;
    }
}
