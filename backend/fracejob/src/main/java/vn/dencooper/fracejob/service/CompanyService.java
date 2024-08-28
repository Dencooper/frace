package vn.dencooper.fracejob.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.dto.ApiResponse;
import vn.dencooper.fracejob.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyService {
    CompanyRepository companyRepository;

    public ResponseEntity<ApiResponse<Company>> handleCreateCompany(@Valid @RequestBody Company request) {
        Company company = companyRepository.save(request);
        ApiResponse<Company> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(201);
        apiResponse.setMessage("Create Company Successfully!");
        apiResponse.setData(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
