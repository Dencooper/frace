package vn.dencooper.fracejob.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.dto.ApiResponse;
import vn.dencooper.fracejob.service.CompanyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;

    @PostMapping
    public ResponseEntity<ApiResponse<Company>> createCompany(@Valid @RequestBody Company request) {
        return companyService.handleCreateCompany(request);
    }

}
