package ro.siit.tripscatalog.model;

/**
 * Entity which maps an user object with user table from database.
 *
 * @author Radu Popescu
 *
 */

import org.hibernate.annotations.Type;
import ro.siit.tripscatalog.service.ValidPassword;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "user")

public class User {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 200, message = "Valid first name is required")
    private String first_name;

    @Column(name = "LAST_NAME", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 200, message = "Valid last name is required")
    private String last_name;

    @Column(name = "USER_NAME", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 3, max = 200, message = "Username must have at least 3 characters")
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    @Type(type = "string")
    @ValidPassword
    private String password;

    @Transient
    private String confirm_password;

    @Column(name = "CITY", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 200, message = "The city name must have at least 1 character")
    private String city;

    @Column(name = "ADDRESS", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 1, max = 200, message = "The address must have at least 1 character")
    private String address;

    @Column(name = "PHONE", nullable = false)
    @Type(type = "string")
    @NotBlank
    @Size(min = 10, max = 10, message = "Phone number must have 10 characters")
    private String phone;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}