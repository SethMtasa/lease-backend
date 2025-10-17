package prac.lease.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.SiteRequestDto;
import prac.lease.dto.SiteResponseDto;
import prac.lease.service.SiteService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SiteResponseDto>> createSite(@RequestBody SiteRequestDto siteRequest) {
        ApiResponse<SiteResponseDto> apiResponse = siteService.createSite(siteRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteResponseDto>> getSiteById(@PathVariable Long id) {
        ApiResponse<SiteResponseDto> apiResponse = siteService.getSiteById(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SiteResponseDto>>> getAllSites() {
        ApiResponse<List<SiteResponseDto>> apiResponse = siteService.getAllSites();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteResponseDto>> updateSite(@PathVariable Long id, @RequestBody SiteRequestDto updateRequest) {
        ApiResponse<SiteResponseDto> apiResponse = siteService.updateSite(id, updateRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSite(@PathVariable Long id) {
        ApiResponse<String> apiResponse = siteService.deleteSite(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}