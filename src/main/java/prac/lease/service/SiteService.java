package prac.lease.service;

import prac.lease.dto.ApiResponse;
import prac.lease.dto.SiteRequestDto;
import prac.lease.dto.SiteResponseDto;
import java.util.List;

/**
 * Service interface for managing site-related operations.
 */
public interface SiteService {
    /**
     * Creates a new site record.
     * @param siteRequest The DTO containing the site details.
     * @return ApiResponse containing the newly created SiteResponseDto.
     */
    ApiResponse<SiteResponseDto> createSite(SiteRequestDto siteRequest);

    /**
     * Retrieves a site by its ID.
     * @param id The ID of the site.
     * @return ApiResponse containing the SiteResponseDto.
     */
    ApiResponse<SiteResponseDto> getSiteById(Long id);

    /**
     * Retrieves a list of all sites.
     * @return ApiResponse containing a list of all SiteResponseDto.
     */
    ApiResponse<List<SiteResponseDto>> getAllSites();

    /**
     * Updates an existing site's details.
     * @param id The ID of the site to update.
     * @param updateRequest The DTO with the updated site details.
     * @return ApiResponse containing the updated SiteResponseDto.
     */
    ApiResponse<SiteResponseDto> updateSite(Long id, SiteRequestDto updateRequest);

    /**
     * Deletes a site by its ID.
     * @param id The ID of the site to delete.
     * @return ApiResponse indicating success or failure.
     */
    ApiResponse<String> deleteSite(Long id);
}