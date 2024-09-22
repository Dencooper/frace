package vn.dencooper.fracejob.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Permission;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.service.PermissionService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission req) {
        Permission res = permissionService.handleCreatePermission(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission req) {
        Permission res = permissionService.handleUpdatePermission(req);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    @ApiMessage("Get all permissions")
    public ResponseEntity<PaginationResponse> getAllPermissions(
            @Filter Specification<Permission> spec,
            Pageable pageable) {
        PaginationResponse res = permissionService.fetchAllPermission(spec, pageable);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(Permission req) {
        permissionService.handleDeletePermission(req);
        return ResponseEntity.ok().body(null);
    }

}
