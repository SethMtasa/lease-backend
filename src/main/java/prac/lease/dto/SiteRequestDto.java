package prac.lease.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteRequestDto {

    private String siteName;
    private String province;
    private String district;
    private String zone;

    // No-args constructor for deserialization
    public SiteRequestDto() {
    }

    // Getters
    public String getSiteName() {
        return siteName;
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public String getZone() {
        return zone;
    }

    // Setters
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}