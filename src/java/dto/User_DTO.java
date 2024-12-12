package dto;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class User_DTO implements Serializable {
    @Expose
    private String firstname;
    @Expose
    private String lastname;
    @Expose
    private String email;
    @Expose(serialize = false,deserialize = true)
    private String password;

    public User_DTO() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
