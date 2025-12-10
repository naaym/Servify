package com.servify.provider.controller;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderRequest;
import com.servify.provider.dto.ProviderResponse;
import com.servify.provider.service.ProviderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponse>> getProviders() {
        return ResponseEntity.ok(providerService.findAll());
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProviderRegistrationResponse> register(
            @RequestParam("name") @NotBlank @Size(max = 100) String name,
            @RequestParam("email") @NotBlank @Email @Size(max = 255) String email,
            @RequestParam("password") @NotBlank @Size(min = 6, max = 255) String password,
            @RequestParam("phone") @NotBlank @Size(max = 20) String phone,
            @RequestParam("governorate") @NotBlank @Size(max = 100) String governorate,
            @RequestParam("delegation") @NotBlank @Size(max = 100) String delegation,
            @RequestParam("age") @NotNull @Min(18) Integer age,
            @RequestPart("cin") @NotNull MultipartFile cin,
            @RequestPart("cv") @NotNull MultipartFile cv,
            @RequestPart("diplome") @NotNull MultipartFile diplome
    ) {
        ProviderRegistrationRequest request = new ProviderRegistrationRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setPhone(phone);
        request.setGovernorate(governorate);
        request.setDelegation(delegation);
        request.setAge(age);
        request.setCin(cin);
        request.setCv(cv);
        request.setDiplome(diplome);

        ProviderRegistrationResponse response = providerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponse> getProvider(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProviderResponse> createProvider(@Valid @RequestBody ProviderRequest request) {
        ProviderResponse response = providerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> updateProvider(@PathVariable Long id, @Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(providerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        providerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
