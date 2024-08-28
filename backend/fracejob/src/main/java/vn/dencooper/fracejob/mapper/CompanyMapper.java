package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.dencooper.fracejob.domain.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    @Mapping(target = "id", ignore = true)
    void toCompany(@MappingTarget Company company, Company request);
}
