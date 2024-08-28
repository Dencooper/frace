package vn.dencooper.fracejob.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
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

    public List<Company> fetchAllCompany() {
        return companyRepository.findAll();
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
