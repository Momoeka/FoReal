package com.image_gallery.image_gallery.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.image_gallery.image_gallery.entity.ImageModel;
import com.image_gallery.image_gallery.entity.UserModel;

@org.springframework.stereotype.Repository
public interface Repo extends JpaRepository<ImageModel, Integer> {

    List<ImageModel> findByUserModel(UserModel userModel);
}
