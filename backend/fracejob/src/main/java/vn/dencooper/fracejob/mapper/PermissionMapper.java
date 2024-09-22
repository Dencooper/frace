package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.dencooper.fracejob.domain.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void toPermission(Permission source, @MappingTarget Permission target);
}