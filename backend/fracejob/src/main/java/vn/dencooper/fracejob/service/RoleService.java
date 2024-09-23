package vn.dencooper.fracejob.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Role;
import vn.dencooper.fracejob.domain.dto.response.Meta;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.mapper.RoleMapper;
import vn.dencooper.fracejob.repository.PermissionRepository;
import vn.dencooper.fracejob.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public Role handleCreateRole(@Valid @RequestBody Role req) throws AppException {
        if (roleRepository.existsByName(req.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        if (req.getPermissions() != null) {
            List<Long> listIdPermissions = req.getPermissions()
                    .stream()
                    .map((permission) -> permission.getId())
                    .toList();
            req.setPermissions(permissionRepository.findByIdIn(listIdPermissions));
        }
        return roleRepository.save(req);
    }

    public Role handleUpdateRole(@Valid @RequestBody Role req) throws AppException {
        Role res = new Role();
        res = roleRepository.findById(req.getId()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOTFOUND));
        if (req.getPermissions() != null) {
            List<Long> listIdPermissions = req.getPermissions()
                    .stream()
                    .map((permission) -> permission.getId())
                    .toList();
            req.setPermissions(permissionRepository.findByIdIn(listIdPermissions));
        }
        roleMapper.toRole(req, res);
        return roleRepository.save(res);
    }

    public PaginationResponse fetchAllRoles(
            Specification<Role> spec,
            Pageable pageable) {
        Page<Role> pageRoles = roleRepository.findAll(spec, pageable);

        PaginationResponse res = new PaginationResponse();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .pages(pageRoles.getTotalPages())
                .total(pageRoles.getTotalElements())
                .build();

        res.setMeta(meta);
        res.setResult(pageRoles.getContent());
        return res;
    }

    public void handleDeleteRole(Role req) {
        roleRepository.delete(req);
    }
}
