package ua.com.brdo.business.constructor.entity;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;
    @Column(name = "password", length = 100, nullable = false)
    private String
            password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_id")}, inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    protected User() {
    }

    public User(String name, String pass) {
        username = name;
        password = pass;
    }

    @Override
    public String toString() {
        String strRoles = "Roles: ";
        for (Role role : roles) {
            strRoles += role.getRole() + " ";
        }
        return String.format("User[username='%s']", username) + strRoles;
    }

}
