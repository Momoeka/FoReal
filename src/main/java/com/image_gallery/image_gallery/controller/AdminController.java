package com.image_gallery.image_gallery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.image_gallery.image_gallery.entity.UserModel;
import com.image_gallery.image_gallery.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String getallUsers(Model model) {
        List<UserModel> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "admin-dashboard";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") int userId) {
        // Call the service method to delete the user by ID
        userService.deleteUserById(userId);

        // Redirect to a page after deletion
        return "redirect:/user/list";
    }

    @GetMapping("/summary")
    public String getMethodName() {
        return "summary";
    }

}
