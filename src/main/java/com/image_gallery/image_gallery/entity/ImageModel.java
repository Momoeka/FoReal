package com.image_gallery.image_gallery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageid;

    @NotBlank(message = "Caption cannot be empty")
    private String imageCaption;

    private String imageTitle;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserModel userModel;

}
