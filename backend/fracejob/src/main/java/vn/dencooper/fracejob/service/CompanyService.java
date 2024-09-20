package vn.dencooper.fracejob.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.dto.request.company.CompanyCreationRequest;
import vn.dencooper.fracejob.domain.dto.response.Meta;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.mapper.CompanyMapper;
import vn.dencooper.fracejob.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyService {
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;

    public Company handleCreateCompany(CompanyCreationRequest request) {
        Company company = companyMapper.toCompany(request);
        return companyRepository.save(company);
    }

    public Company fetchCompanyById(long id) {
        return companyRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOTFOUND));
    }

    public PaginationResponse fetchAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompanies = companyRepository.findAll(spec, pageable);

        PaginationResponse res = new PaginationResponse();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageCompanies.getTotalPages())
                .total(pageCompanies.getTotalElements())
                .build();
        res.setMeta(meta);
        res.setResult(pageCompanies.getContent());

        return res;
    }

    public Company handleUpdateCompany(long id, Company request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOTFOUND));
        companyMapper.toCompany(company, request);
        return companyRepository.save(company);
    }

    public void handleDeleteCompany(long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOTFOUND));
        companyRepository.delete(company);
    }
}
