package prac.lease.service;

import prac.lease.dto.ApiResponse;
import prac.lease.dto.LandlordRequestDto;
import prac.lease.dto.LandlordResponseDto;
import java.util.List;

/**
 * Service interface for managing landlord-related operations.
 */
public interface LandlordService {
    /**
     * Creates a new landlord record.
     * @param landlordRequest The DTO containing the landlord details.
     * @return ApiResponse containing the newly created LandlordResponseDto.
     */
    ApiResponse<LandlordResponseDto> createLandlord(LandlordRequestDto landlordRequest);

    /**
     * Retrieves a landlord by their ID.
     * @param id The ID of the landlord.
     * @return ApiResponse containing the LandlordResponseDto.
     */
    ApiResponse<LandlordResponseDto> getLandlordById(Long id);

    /**
     * Retrieves a list of all landlords.
     * @return ApiResponse containing a list of all LandlordResponseDto.
     */
    ApiResponse<List<LandlordResponseDto>> getAllLandlords();

    /**
     * Updates an existing landlord's details.
     * @param id The ID of the landlord to update.
     * @param updateRequest The DTO with the updated landlord details.
     * @return ApiResponse containing the updated LandlordResponseDto.
     */
    ApiResponse<LandlordResponseDto> updateLandlord(Long id, LandlordRequestDto updateRequest);

    /**
     * Deletes a landlord by their ID.
     * @param id The ID of the landlord to delete.
     * @return ApiResponse indicating success or failure.
     */
    ApiResponse<String> deleteLandlord(Long id);
}