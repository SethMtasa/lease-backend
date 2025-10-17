package prac.lease.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.SiteRequestDto;
import prac.lease.dto.SiteResponseDto;
import prac.lease.exception.ResourceNotFoundException;
import prac.lease.model.Site;
import prac.lease.repository.SiteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;

    public SiteServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    @Transactional
    public ApiResponse<SiteResponseDto> createSite(SiteRequestDto siteRequest) {
//        log.info("Creating new site: {}", siteRequest.getSiteName());
        try {
            // Manual conversion from DTO to Entity
            Site site = new Site();
            site.setSiteName(siteRequest.getSiteName());
            site.setProvince(siteRequest.getProvince());
            site.setDistrict(siteRequest.getDistrict());
            site.setZone(siteRequest.getZone());

            Site savedSite = siteRepository.save(site);

            // Manual conversion from Entity to DTO
            SiteResponseDto responseDto = new SiteResponseDto(savedSite);

            return new ApiResponse<>(true,  "Site created successfully.", responseDto);
        } catch (Exception e) {
//            log.error("Error creating site: {}", siteRequest.getSiteName(), e);
            return new ApiResponse<>(false,  "Error creating site.", null);
        }
    }

    @Override
    public ApiResponse<SiteResponseDto> getSiteById(Long id) {
//        log.info("Fetching site with ID: {}", id);
        try {
            Site site = siteRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Site not found with ID: " + id));

            // Manual conversion from Entity to DTO
            SiteResponseDto responseDto = new SiteResponseDto(site);

            return new ApiResponse<>(true, "Site retrieved successfully.", responseDto);
        } catch (ResourceNotFoundException e) {
//            log.error(e.getMessage());
            return new ApiResponse<>(false,  e.getMessage(), null);
        } catch (Exception e) {
//            log.error("Error fetching site with ID: {}", id, e);
            return new ApiResponse<>(false,  "Error fetching site.", null);
        }
    }

    @Override
    public ApiResponse<List<SiteResponseDto>> getAllSites() {
//        log.info("Fetching all sites");
        try {
            List<Site> sites = siteRepository.findAll();

            // Manual conversion from List of Entities to List of DTOs
            List<SiteResponseDto> responseDtos = sites.stream()
                    .map(SiteResponseDto::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true,  "Sites retrieved successfully.", responseDtos);
        } catch (Exception e) {
//            log.error("Error fetching all sites", e);
            return new ApiResponse<>(false, "Error fetching sites.", null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<SiteResponseDto> updateSite(Long id, SiteRequestDto updateRequest) {
//        log.info("Updating site with ID: {}", id);
        try {
            Site existingSite = siteRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Site not found with ID: " + id));

            // Update all fields from the request DTO
            existingSite.setSiteName(updateRequest.getSiteName());
            existingSite.setProvince(updateRequest.getProvince());
            existingSite.setDistrict(updateRequest.getDistrict());
            existingSite.setZone(updateRequest.getZone());

            Site savedSite = siteRepository.save(existingSite);

            // Manual conversion from updated Entity to DTO
            SiteResponseDto responseDto = new SiteResponseDto(savedSite);

            return new ApiResponse<>(true, "Site updated successfully.", responseDto);
        } catch (ResourceNotFoundException e) {
//            log.error(e.getMessage());
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {
//            log.error("Error updating site with ID: {}", id, e);
            return new ApiResponse<>(false,  "Error updating site.", null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteSite(Long id) {
//        log.info("Deleting site with ID: {}", id);
        try {
            if (!siteRepository.existsById(id)) {
                throw new ResourceNotFoundException("Site not found with ID: " + id);
            }
            siteRepository.deleteById(id);
            return new ApiResponse<>(true,  "Site deleted successfully.", "Site with ID " + id + " has been deleted.");
        } catch (ResourceNotFoundException e) {
//            log.error(e.getMessage());
            return new ApiResponse<>(false,  e.getMessage(), null);
        } catch (Exception e) {
//            log.error("Error deleting site with ID: {}", id, e);
            return new ApiResponse<>(false,  "Error deleting site.", null);
        }
    }
}