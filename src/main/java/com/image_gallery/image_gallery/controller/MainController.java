package com.image_gallery.image_gallery.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.image_gallery.image_gallery.entity.ImageModel;
import com.image_gallery.image_gallery.entity.UserModel;
import com.image_gallery.image_gallery.service.ImageService;
import com.image_gallery.image_gallery.service.UserService;
import com.image_gallery.image_gallery.service.S3Service;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MainController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private S3Service s3Service;

    @GetMapping("/user")
    public String getMethodName() {
        return "index";
    }

    @GetMapping("/user/postform")
    public String getMethodName(Model model, HttpSession session) {
        model.addAttribute("imageObj", new ImageModel());
        session.setAttribute("imageid", 0);
        session.setAttribute("imageTitle", "");
        return "user-form";
    }

    @PostMapping("/user/postform")
    public String processImageForm(@Valid @ModelAttribute("imageObj") ImageModel imageModel,
            BindingResult bindingResult, @RequestParam("imageData") MultipartFile file,
            HttpSession session)
            throws IOException {
        if (bindingResult.hasErrors()) {
            return "user-form";
        } else {
            UserModel sessionUserModel = (UserModel) session.getAttribute("user");
            String imageTitle = "";

            int sessionimageid = (int) session.getAttribute("imageid");
            String sessionimagetitle = (String) session.getAttribute("imageTitle");

            if (!file.isEmpty()) {
                if (sessionimageid != 0) { // update
                    s3Service.deleteFile(sessionimagetitle); // delete existing image from S3
                }
                imageTitle = file.getOriginalFilename();
                s3Service.uploadFile(file); // upload new image to S3
            } else {
                imageTitle = sessionimagetitle;
            }

            imageModel.setImageTitle(imageTitle);
            imageModel.setUserModel(sessionUserModel);
            imageModel.setImageid(sessionimageid);

            ImageModel savedImage = imageService.saveImage(imageModel);
            if (savedImage != null) {
                if (sessionimageid != 0) {
                    session.setAttribute("msg", "Image has been updated successfully");
                } else {
                    session.setAttribute("msg", "Image has been saved successfully");
                }
            } else {
                session.setAttribute("msg", "Something Went Wrong");
            }
            return "redirect:/user/postform/action";
        }
    }

    @GetMapping("/user/postform/action")
    public String getAll(Model model, HttpSession session) {
        UserModel sessionUser = (UserModel) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/user/login"; // Redirect to login if user is not in session
        }
        List<ImageModel> allImages = imageService.getAllImages(sessionUser);
        model.addAttribute("allImages", allImages);
        return "action";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteImage(@PathVariable int id, HttpSession session) throws Exception {
        ImageModel dbImage = imageService.getImageById(id);
        UserModel sessionUser = (UserModel) session.getAttribute("user");

        if (dbImage != null) {
            UserModel userImageModel = dbImage.getUserModel();
            if (userImageModel.getUserId() == sessionUser.getUserId()) {
                boolean result = imageService.deleteImage(dbImage);
                String existingTitle = dbImage.getImageTitle();
                if (result) {
                    s3Service.deleteFile(existingTitle); // delete image from S3
                    session.setAttribute("msg", "Deleted successfully");
                }
            } else {
                session.setAttribute("msg", "You are not authorized to delete other users records.");
                return "redirect:/user/postform/action";
            }
        } else {
            session.setAttribute("msg", "Image with id = " + id + " does not exist in our records!!");
        }

        return "redirect:/user/postform/action";
    }

    @GetMapping("/user/update/{id}")
    public String updateImage(@PathVariable int id, HttpSession session, Model model) throws Exception {
        ImageModel dbImage = imageService.getImageById(id);
        if (dbImage != null) {
            UserModel userImageModel = dbImage.getUserModel();
            UserModel sessionUser = (UserModel) session.getAttribute("user");
            if (userImageModel.getUserId() == sessionUser.getUserId()) {
                session.setAttribute("imageid", id);
                session.setAttribute("imageTitle", dbImage.getImageTitle());
                model.addAttribute("imageObj", dbImage);
                return "user-form";
            } else {
                session.setAttribute("msg", "You are not authorized to update other users records.");
                return "redirect:/user/postform/action";
            }
        } else {
            session.setAttribute("msg", "Image does not exist.");
            return "redirect:/user/postform/action";
        }
    }
}

// package com.image_gallery.image_gallery.controller;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.autoconfigure.security.SecurityProperties.User;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.image_gallery.image_gallery.entity.ImageModel;
// import com.image_gallery.image_gallery.entity.UserModel;
// import com.image_gallery.image_gallery.service.ImageService;
// import com.image_gallery.image_gallery.service.UserService;

// import jakarta.servlet.http.HttpSession;
// import jakarta.validation.Valid;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

// @Controller
// public class MainController {

// @Autowired
// public ImageService imageService;

// @Autowired
// public UserService userService;

// public static String uploadDir = "src/main/resources/static/images";

// @GetMapping("/user")
// public String getMethodName() {
// return "index";
// }

// @GetMapping("/user/postform")
// public String getMethodName(Model model, HttpSession session) {
// model.addAttribute("imageObj", new ImageModel());
// session.setAttribute("imageid", 0);
// session.setAttribute("imageTitle", "");
// return "user-form";
// }

// @PostMapping("/user/postform")
// public String processImageForm(@Valid @ModelAttribute("imageObj") ImageModel
// imageModel,
// BindingResult bindingResult, @RequestParam("imageData") MultipartFile file,
// HttpSession session)
// throws Exception {
// if (bindingResult.hasErrors()) {
// return "/user-form";
// } else {
// UserModel sessionUserModel = (UserModel) session.getAttribute("user");
// String imageTitle = "";

// int sessionimageid = (int) session.getAttribute("imageid");
// String sessionimagetitle = (String) session.getAttribute("imageTitle");
// if (!file.isEmpty()) {
// if (sessionimageid != 0) {// update
// String existingTitle = sessionimagetitle;
// Path fileNameAndPath = Paths.get(uploadDir, existingTitle);
// System.out.println(fileNameAndPath);
// Files.deleteIfExists(fileNameAndPath);
// } // for save
// imageTitle = file.getOriginalFilename();
// Path fileNameAndPath = Paths.get(uploadDir, imageTitle);
// Files.write(fileNameAndPath, file.getBytes());
// // } else if (imageModel.getImageid() == 0) {
// // session.setAttribute("msg", "Please add image ");
// // return "user-form";}
// } else {
// System.out.println("Inside else of file" + imageModel.getImageTitle());
// // imageTitle = imageModel.getImageTitle();
// // imageModel.setImageTitle(imageTitle);
// imageTitle = sessionimagetitle;
// }

// imageModel.setImageTitle(imageTitle);
// // imageModel.setUserModel(dbModel);
// imageModel.setUserModel(sessionUserModel);
// imageModel.setImageid(sessionimageid);

// ImageModel savedImage = imageService.saveImage(imageModel);
// if (savedImage != null) {
// if (sessionimageid != 0) {
// session.setAttribute("msg", "Image has been updated successfully");
// } else {
// session.setAttribute("msg", "Image has been saved successfully");
// }
// } else {
// session.setAttribute("msg", "Something Went Wrong");
// }
// return "redirect:/user/postform/action";
// }

// }

// @GetMapping("/user/postform/action")
// public String getAll(Model model, HttpSession session) {
// UserModel sessionUser = (UserModel) session.getAttribute("user");
// if (sessionUser == null) {
// return "redirect:/user/login"; // Redirect to login if user is not in session
// }
// List<ImageModel> allImages = imageService.getAllImages(sessionUser);
// model.addAttribute("allImages", allImages);
// return "action";
// }

// @GetMapping("/user/delete/{id}")
// public String deleteImage(@PathVariable int id, HttpSession session) throws
// Exception {
// // System.out.println(id);
// ImageModel dbImage = imageService.getImageById(id);
// UserModel sessionuser = (UserModel) session.getAttribute("user");

// if (dbImage != null) {

// UserModel userImageModel = dbImage.getUserModel();
// if (userImageModel.getUserId() == sessionuser.getUserId()) {

// boolean result = false;
// result = imageService.deleteImage(dbImage);
// String existingTitle = dbImage.getImageTitle();
// if (result) {
// Path fileNameAndPath = Paths.get(uploadDir, existingTitle);
// System.out.println(fileNameAndPath);
// Files.deleteIfExists(fileNameAndPath);
// session.setAttribute("msg", "Deleted successfully");
// }
// } else {

// session.setAttribute("msg", "You are not authorized to Delete other user
// records.");
// return "redirect:/user/postform/action";
// }

// } else {
// session.setAttribute("msg", "Image with id = " + id + " doesnot exists in our
// records!!");
// }

// return "redirect:/user/postform/action";
// }

// @GetMapping("/user/update/{id}")
// public String UpdateImage(@PathVariable int id, HttpSession session, Model
// model) throws Exception {
// ImageModel dbImage = imageService.getImageById(id);
// if (dbImage != null) {
// UserModel userimageModel = dbImage.getUserModel();
// UserModel sessionUser = (UserModel) session.getAttribute("user");
// if (userimageModel.getUserId() == sessionUser.getUserId()) {
// session.setAttribute("imageid", id);
// session.setAttribute("imageTitle", dbImage.getImageTitle());
// model.addAttribute("imageObj", dbImage);
// return "user-form";
// } else {
// session.setAttribute("msg", "You are not authorised to Update other users
// record.");
// return "redirect:/user/postform/action";
// }
// } else {
// session.setAttribute("msg", "image does not exist.");
// return "redirect:/user/postform/action";
// }
// }

// }