package prac.lease.service;

import prac.lease.dto.ApiResponse;
import prac.lease.dto.RoleRequest;
import prac.lease.dto.RoleResponseDto; // Import the DTO
import java.util.List;

public interface RoleService {

    ApiResponse<RoleResponseDto> createRole(RoleRequest roleRequest);

    ApiResponse<List<RoleResponseDto>> getAllRoles();

    // New method to update a role
    ApiResponse<RoleResponseDto> updateRole(Long id, RoleRequest roleRequest);
}