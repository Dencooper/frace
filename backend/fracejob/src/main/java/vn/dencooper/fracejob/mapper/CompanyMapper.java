package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.dto.request.company.CompanyCreationRequest;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void toCompany(@MappingTarget Company company, Company request);

    Company toCompany(CompanyCreationRequest request);

}
