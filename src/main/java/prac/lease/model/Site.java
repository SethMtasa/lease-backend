package prac.lease.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Audited(targetAuditMode = NOT_AUDITED, withModifiedFlag = true)
@Getter
@Setter
public class Site extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String siteName;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String zone;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Lease> leases;

    // No-args constructor for JPA
    public Site() {
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

    public List<Lease> getLeases() {
        return leases;
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

    public void setLeases(List<Lease> leases) {
        this.leases = leases;
    }
}