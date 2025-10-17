package prac.lease.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.LandlordRequestDto;
import prac.lease.dto.LandlordResponseDto;
import prac.lease.service.LandlordService;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/landlords")
public class LandlordController {

    private final LandlordService landlordService;

    public LandlordController(LandlordService landlordService) {
        this.landlordService = landlordService;
    }

    /**
     * Creates a new landlord record.
     * @param landlordRequest The DTO containing the landlord details.
     * @return ResponseEntity with the created LandlordResponseDto.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<LandlordResponseDto>> createLandlord(@RequestBody LandlordRequestDto landlordRequest) {
        ApiResponse<LandlordResponseDto> apiResponse = landlordService.createLandlord(landlordRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves a landlord by their ID.
     * @param id The ID of the landlord.
     * @return ResponseEntity with the LandlordResponseDto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LandlordResponseDto>> getLandlordById(@PathVariable Long id) {
        ApiResponse<LandlordResponseDto> apiResponse = landlordService.getLandlordById(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Retrieves a list of all landlords.
     * @return ResponseEntity with a list of LandlordResponseDto.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LandlordResponseDto>>> getAllLandlords() {
        ApiResponse<List<LandlordResponseDto>> apiResponse = landlordService.getAllLandlords();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Updates an existing landlord's details.
     * @param id The ID of the landlord to update.
     * @param updateRequest The DTO with the updated landlord details.
     * @return ResponseEntity with the updated LandlordResponseDto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LandlordResponseDto>> updateLandlord(@PathVariable Long id, @RequestBody LandlordRequestDto updateRequest) {
        ApiResponse<LandlordResponseDto> apiResponse = landlordService.updateLandlord(id, updateRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /**
     * Deletes a landlord by their ID.
     * @param id The ID of the landlord to delete.
     * @return ResponseEntity indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteLandlord(@PathVariable Long id) {
        ApiResponse<String> apiResponse = landlordService.deleteLandlord(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}