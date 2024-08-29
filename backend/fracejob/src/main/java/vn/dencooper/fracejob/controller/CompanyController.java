package vn.dencooper.fracejob.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.PaginationResponse;
import vn.dencooper.fracejob.service.CompanyService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;

    @PostMapping
    @ApiMessage("Create Company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company request) {
        Company company = companyService.handleCreateCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch Company By ID")
    public ResponseEntity<Company> getCompany(@PathVariable("id") long id) {
        Company company = companyService.fetchCompanyById(id);
        return ResponseEntity.ok().body(company);
    }

    @GetMapping
    @ApiMessage("Fetch All Companies")
    public ResponseEntity<PaginationResponse> getAllCompanies(@Filter Specification<Company> spec,
            Pageable pageable) {
        PaginationResponse companies = companyService.fetchAllCompany(spec, pageable);
        return ResponseEntity.ok().body(companies);
    }

    @PutMapping("/{id}")
    @ApiMessage("Update Company")
    public ResponseEntity<Company> updateCompany(@PathVariable("id") long id, @Valid @RequestBody Company request) {
        Company company = companyService.handleUpdateCompany(id, request);
        return ResponseEntity.ok().body(company);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete Company")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

}
