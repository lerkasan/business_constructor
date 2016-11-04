package ua.com.brdo.business.constructor.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.com.brdo.business.constructor.model.dto.RoleDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    Set<User> users;

    public Role() {
    }

    public Role(RoleDto roleDto) {
        title = roleDto.getTitle();
    }

    public Role(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<User> getUsers() {
        return new HashSet<>(users);
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
