package prac.lease.dto;

import prac.lease.model.Site;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SiteResponseDto {

    private Long id;
    private String siteName;
    private String province;
    private String district;
    private String zone;
    private List<LeaseResponseDto> leases;

    public SiteResponseDto() {
    }

    /**
     * Constructs a DTO from a Site entity.
     * This constructor maps the associated leases to LeaseResponseDto objects.
     * @param site The Site entity to convert.
     */
    public SiteResponseDto(Site site) {
        this.id = site.getId();
        this.siteName = site.getSiteName();
        this.province = site.getProvince();
        this.district = site.getDistrict();
        this.zone = site.getZone();
        this.leases = site.getLeases() != null ?
                site.getLeases().stream()
                        .map(LeaseResponseDto::new)
                        .collect(Collectors.toList()) : null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.setZone(zone);
    }

    public List<LeaseResponseDto> getLeases() {
        return leases;
    }

    public void setLeases(List<LeaseResponseDto> leases) {
        this.leases = leases;
    }
}