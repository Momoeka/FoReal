package com.image_gallery.image_gallery.service;

import java.util.List;
import java.util.Optional;

import org.aspectj.asm.AsmManager.ModelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelExtensionsKt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.image_gallery.image_gallery.entity.ImageModel;
import com.image_gallery.image_gallery.entity.UserModel;
import com.image_gallery.image_gallery.repo.Repo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Repo repo;

    @Override
    public void removeSessionMsg() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpServletRequest request = attr.getRequest();
        HttpSession session = request.getSession();

        session.removeAttribute("msg");

    }

    @Override
    public ImageModel saveImage(ImageModel imagemodel) {
        ImageModel savedImage = repo.save(imagemodel);
        return savedImage;
    }

    @Override
    public ImageModel getImageById(int id) {
        Optional<ImageModel> dbImage = repo.findById(id);
        if (dbImage.isPresent()) {
            return dbImage.get();
        }
        return null;
    }

    @Override
    public boolean deleteImage(ImageModel dbImage) {

        repo.delete(dbImage);

        return true;
    }

    @Override
    public List<ImageModel> getAllImages(UserModel userModel) {
        List<ImageModel> allImages = repo.findByUserModel(userModel);
        return allImages;
    }
    // @Override
    // public List<ImageModel> getUserbyuserId() {
    // List<ImageModel> allImages = repo.findAll();
    // return allImages;
    // }

}