package ons.group8.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

//import javax.persistence.Entity;
import java.util.List;

@Data
@AllArgsConstructor
//@Entity
public class User {
    private Integer id;
    private String email;
    private String fname;
    private String lname;
    private String password;
    private List<String> roles;

    public User(String theEmail, String thePassword, String theFname, String theLname){
//        this.id = null;
        this.email = theEmail;
        this.password = thePassword;
        this.fname = theFname;
        this.lname = theLname;
    }
}
