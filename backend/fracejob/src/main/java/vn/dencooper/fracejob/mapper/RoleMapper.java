package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.dencooper.fracejob.domain.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void toRole(Role source, @MappingTarget Role target);
}