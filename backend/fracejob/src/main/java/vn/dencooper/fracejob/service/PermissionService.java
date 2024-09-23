package vn.dencooper.fracejob.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Permission;
import vn.dencooper.fracejob.domain.dto.response.Meta;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.mapper.PermissionMapper;
import vn.dencooper.fracejob.repository.PermissionRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public Permission handleCreatePermission(Permission req) throws AppException {
        if (permissionRepository.existsByApiPathAndMethodAndModule(req.getApiPath(), req.getMethod(),
                req.getModule())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        return permissionRepository.save(req);
    }

    public Permission handleUpdatePermission(Permission req) throws AppException {
        Permission permission = permissionRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOTFOUND));

        if (permissionRepository.existsByApiPathAndMethodAndModule(req.getApiPath(), req.getMethod(),
                req.getModule())) {
            if (permission.getName().equals(req.getName())) {
                throw new AppException(ErrorCode.PERMISSION_EXISTED);
            }
            permissionMapper.toPermission(req, permission);
        }

        return permissionRepository.save(permission);
    }

    public PaginationResponse fetchAllPermission(
            Specification<Permission> spec,
            Pageable pageable) {
        Page<Permission> pagePermissions = permissionRepository.findAll(spec, pageable);
        PaginationResponse res = new PaginationResponse();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pagePermissions.getTotalPages())
                .total(pagePermissions.getTotalElements())
                .build();
        res.setMeta(meta);
        res.setResult(pagePermissions.getContent());
        return res;
    }

    public void handleDeletePermission(Permission req) {
        Permission permission = permissionRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOTFOUND));
        permission.getRoles().forEach((role) -> role.getPermissions().remove(role));
        permissionRepository.delete(permission);
    }
}
