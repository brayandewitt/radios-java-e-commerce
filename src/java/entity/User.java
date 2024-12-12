
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User implements Serializable{
    
    @Id 
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @Column(name = "firstname",length = 45,nullable = false)
    private String firstname;
    @Column(name = "lastname",length = 45,nullable = false)
    private String lsatname;
    @Column(name = "email",length = 45,nullable = false)
    private String email;
    @Column(name = "password",length = 45,nullable = false)
    private String password;
    @Column(name = "verification",length = 45,nullable = false)
    private String verification;

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLsatname() {
        return lsatname;
    }

    public void setLsatname(String lsatname) {
        this.lsatname = lsatname;
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

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
