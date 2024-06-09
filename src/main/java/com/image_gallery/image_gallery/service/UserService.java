package com.image_gallery.image_gallery.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.image_gallery.image_gallery.entity.ImageModel;
import com.image_gallery.image_gallery.entity.UserModel;

public interface UserService extends UserDetailsService {

    UserModel saveUser(UserModel userModel);

    void removeSessionMsg();

    UserModel getUserByEmail(String userEmail);

    UserModel getuserbyID(int userId);

    UserModel updateUser(UserModel updatedUser);

    List<UserModel> getAllUsers();

    void deleteUserById(int userId);

}
