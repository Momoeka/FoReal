package com.image_gallery.image_gallery.service;

import java.util.List;

import com.image_gallery.image_gallery.entity.ImageModel;
import com.image_gallery.image_gallery.entity.UserModel;

public interface ImageService {

    ImageModel saveImage(ImageModel imagemodel);

    void removeSessionMsg();

    ImageModel getImageById(int id);

    boolean deleteImage(ImageModel dbImage);

    List<ImageModel> getAllImages(UserModel userModel);
    // List<ImageModel> getAllImages();

}
