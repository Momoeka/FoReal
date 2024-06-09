package com.image_gallery.image_gallery.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.image_gallery.image_gallery.entity.UserModel;
import com.image_gallery.image_gallery.service.UserService;
import com.razorpay.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/user")
public class RegisterModel {
    private UserService userService;

    public RegisterModel(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getMethodName(Model model) {
        model.addAttribute("userObj", new UserModel());
        return "register-form";
    }

    @PostMapping("/signup")
    public String postMethodName(@Valid @ModelAttribute("userObj") UserModel userModel, BindingResult result,
            HttpSession session) {
        if (result.hasErrors()) {
            session.setAttribute("msg", "something went wrong");
            return "register-form";
        } else {
            userService.saveUser(userModel);
            session.setAttribute("msg", "User Saved Successfully");

        }
        return "redirect:/user/register-form";
    }

    @PostMapping("/update")
    public String updateUserDetails(@Valid @ModelAttribute("userObj") UserModel updatedUser, BindingResult result,
            HttpSession session) {
        if (result.hasErrors()) {
            session.setAttribute("msg", "Something went wrong");
            return "info";
        } else {
            UserModel sessionUser = (UserModel) session.getAttribute("user");
            if (sessionUser == null) {
                session.setAttribute("msg", "User session not found");
                return "info";
            }

            updatedUser.setUserId(sessionUser.getUserId());

            userService.updateUser(updatedUser);
            session.setAttribute("msg", "User details updated successfully");
            session.setAttribute("user", updatedUser);
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId, HttpSession session, Model model) {
        userService.deleteUserById(userId);
        session.invalidate();
        model.addAttribute("msg", "User Deleted Successfully");
        return "redirect:/user/login";
    }

    @PostMapping("/deletebyAdmin/{userId}")
    public String deletebyAdmin(@PathVariable("userId") int userId, HttpSession session, Model model) {
        userService.deleteUserById(userId);

        model.addAttribute("msg", "User Deleted Successfully");
        session.setAttribute("msg", "User has been Deleted Successfully");
        return "redirect:/admin/list";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        UserModel user = (UserModel) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("userObj", user);
        return "info"; // Adjusted to point to the correct template name
    }

    // login starts here
    // =================================================================================================================
    @GetMapping("/login")
    public String getMethodName() {
        return "login";
    }

    @GetMapping("/info")
    public String getFullinfo() {
        return "info";
    }

    // =================================================================================================================

    @PostMapping("/create_order")
    @ResponseBody
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
        System.out.println("Hey, order function executed");

        int amt = Integer.parseInt(data.get("amount").toString());

        RazorpayClient client = new RazorpayClient("rzp_test_qo8SLCpZJLXOIZ", "Mmr6jhMgA4EQvMfcPD3E6DFd");

        JSONObject ob = new JSONObject();
        ob.put("amount", amt * 100);
        ob.put("currency", "INR");
        ob.put("receipt", "txn_123456");

        Order order = client.orders.create(ob);
        System.out.println(order);

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        response.put("receipt", order.get("receipt"));

        return response;
    }
}
