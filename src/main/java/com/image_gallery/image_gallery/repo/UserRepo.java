package com.image_gallery.image_gallery.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.image_gallery.image_gallery.entity.UserModel;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Integer> {

    UserModel findByEmail(String email);
}
