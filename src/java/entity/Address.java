/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "addres")
public class Address implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstname", length = 45, nullable = false)
    private String firstname;
    @Column(name = "lastname", length = 45, nullable = false)
    private String lastname;
    @Column(name = "line1", nullable = false)
    private String line1;
    @Column(name = "line2", nullable = false)
    private String line2;
    @Column(name = "postalcode", length = 10, nullable = false)
    private String postal_code;
    @Column(name = "mobile", length = 10, nullable = false)
    private String mobile;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the line1
     */
    public String getLine1() {
        return line1;
    }

    /**
     * @param line1 the line1 to set
     */
    public void setLine1(String line1) {
        this.line1 = line1;
    }

    /**
     * @return the line2
     */
    public String getLine2() {
        return line2;
    }

    /**
     * @param line2 the line2 to set
     */
    public void setLine2(String line2) {
        this.line2 = line2;
    }

    /**
     * @return the postal_code
     */
    public String getPostal_code() {
        return postal_code;
    }

    /**
     * @param postal_code the postal_code to set
     */
    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the first_name
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirstname(String first_name) {
        this.firstname = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLastname(String last_name) {
        this.lastname = last_name;
    }

    /**
     * @return the city
     */
    public City getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

}
