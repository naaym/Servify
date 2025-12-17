package com.servify.search.controller;

import com.servify.search.dto.OptionItemDto;
import com.servify.search.service.SearchOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/options")
public class SearchOptionsController {

    private final SearchOptionsService searchOptionsService;

    @GetMapping("/services")
    public ResponseEntity<List<OptionItemDto>> getServices() {
        return ResponseEntity.ok(searchOptionsService.getAvailableServices());
    }

    @GetMapping("/governorates")
    public ResponseEntity<List<OptionItemDto>> getGovernorates(@RequestParam(name = "serviceId", required = false) Long serviceId) {
        return ResponseEntity.ok(searchOptionsService.getAvailableGovernorates(serviceId));
    }
}
