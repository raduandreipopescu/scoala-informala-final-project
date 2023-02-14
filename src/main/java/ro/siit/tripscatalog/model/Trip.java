package ro.siit.tripscatalog.model;

/**
 * Entity which maps a trip object with trip table from database.
 *
 * @author Radu Popescu
 *
 */

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "trip")
public class Trip {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @Column(name = "TRIP_NAME", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 50, message = "Trip name must be between 1 and 50 characters")
    private String trip_name;

    @Column(name = "DATE_FROM", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @PastOrPresent
    private LocalDate date_from;

    @Column(name = "DATE_TO", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @PastOrPresent
    private LocalDate date_to;

    @Column(name = "TRIP_IMPRESSIONS", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 500, message = "Trip impressions must be between 1 and 500 characters")
    private String trip_impressions;

    @Column(name = "PHOTO1_PATH", nullable = false)
    @Type(type = "string")
    private String photo1_path;

    @Column(name = "PHOTO1_IMPRESSIONS", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 500, message = "Photo 1 impressions must be between 1 and 500 characters")
    private String photo1_impressions;

    @Column(name = "PHOTO2_PATH", nullable = false)
    @Type(type = "string")
    private String photo2_path;

    @Column(name = "PHOTO2_IMPRESSIONS", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 500, message = "Photo 2 impressions must be between 1 and 500 characters")
    private String photo2_impressions;

    @Column(name = "LOCATION", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 500, message = "Trip location must be between 1 and 500 characters")
    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public LocalDate getDate_from() {
        return date_from;
    }

    public void setDate_from(LocalDate date_from) {
        this.date_from = date_from;
    }

    public LocalDate getDate_to() {
        return date_to;
    }

    public void setDate_to(LocalDate date_to) {
        this.date_to = date_to;
    }

    public String getTrip_impressions() {
        return trip_impressions;
    }

    public void setTrip_impressions(String trip_impressions) {
        this.trip_impressions = trip_impressions;
    }

    public String getPhoto1_path() {
        return photo1_path;
    }

    public void setPhoto1_path(String photo1_path) {
        this.photo1_path = photo1_path;
    }

    public String getPhoto1_impressions() {
        return photo1_impressions;
    }

    public void setPhoto1_impressions(String photo1_impressions) {
        this.photo1_impressions = photo1_impressions;
    }

    public String getPhoto2_path() {
        return photo2_path;
    }

    public void setPhoto2_path(String photo2_path) {
        this.photo2_path = photo2_path;
    }

    public String getPhoto2_impressions() {
        return photo2_impressions;
    }

    public void setPhoto2_impressions(String photo2_impressions) {
        this.photo2_impressions = photo2_impressions;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}