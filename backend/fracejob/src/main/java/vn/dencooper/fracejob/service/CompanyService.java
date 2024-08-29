package vn.dencooper.fracejob.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.dto.PaginationResponse;
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

    public Company handleCreateCompany(Company request) {
        return companyRepository.save(request);
    }

    public Company fetchCompanyById(long id) {
        return companyRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOTFOUND));
    }

    public PaginationResponse fetchAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = companyRepository.findAll(spec, pageable);

        if (pageCompany.getTotalElements() == 0) {
            throw new AppException(ErrorCode.COMPANY_NOTFOUND);
        }
        PaginationResponse res = new PaginationResponse();
        res.setPage(pageable.getPageNumber() + 1);
        res.setPageSize(pageable.getPageSize());
        res.setTotalPages(pageCompany.getTotalPages());
        res.setTotalItems(pageCompany.getTotalElements());
        res.setResutl(pageCompany.getContent());

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
