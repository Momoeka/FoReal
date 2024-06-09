package com.image_gallery.image_gallery.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.image_gallery.image_gallery.entity.UserModel;
import com.image_gallery.image_gallery.service.UserService;

@Component
public class CustomerAuthenticationSuccessHandler implements
        AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, Authentication authentication)
            throws IOException, jakarta.servlet.ServletException {
        // Get the authenticated user's email
        String userEmail = authentication.getName();

        // Fetch user details from the database using the email
        UserModel dbUser = userService.getUserByEmail(userEmail);

        // Create a session and store the authenticated user's details in it
        jakarta.servlet.http.HttpSession session = request.getSession();
        session.setAttribute("user", dbUser);

        // Redirect the user to the desired page after successful authentication
        response.sendRedirect(request.getContextPath() + "/user/postform/action");
    }
}
