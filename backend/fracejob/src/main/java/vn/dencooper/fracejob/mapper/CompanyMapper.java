package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;

import vn.dencooper.fracejob.domain.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    Company toCompany(Company company);
}
