package com.image_gallery.image_gallery.entity;

import java.util.List;

import org.hibernate.mapping.Collection;

import com.image_gallery.image_gallery.service.ImageService;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "Please Enter your Email Address")
    @Email(message = "Incorrect Email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.REMOVE)
    private List<ImageModel> imageModel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private java.util.Collection<RolesModel> roles;

}
