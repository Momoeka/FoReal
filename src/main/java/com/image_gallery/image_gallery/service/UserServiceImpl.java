package com.image_gallery.image_gallery.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.image_gallery.image_gallery.entity.RolesModel;
import com.image_gallery.image_gallery.entity.UserModel;
import com.image_gallery.image_gallery.repo.RoleRepo;
import com.image_gallery.image_gallery.repo.UserRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserModel saveUser(UserModel userModel) {
        String encodedPassword = bCryptPasswordEncoder.encode(userModel.getPassword());
        userModel.setPassword(encodedPassword);

        RolesModel rolesModel = roleRepo.findByRoleName("ROLE_USER").get(0);
        userModel.setRoles(Arrays.asList(rolesModel));
        UserModel savedUser = userRepo.save(userModel);
        return savedUser;
    }

    @Override
    public UserModel updateUser(UserModel updatedUser) {
        UserModel existingUser = userRepo.findById(updatedUser.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));

        return userRepo.save(existingUser);
    }

    @Override
    public UserModel getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel dbUserModel = userRepo.findByEmail(username);
        if (dbUserModel != null) {
            // SimpleGrantedAuthority role = new SimpleGrantedAuthority("USER");
            Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(dbUserModel.getRoles());
            return new User(dbUserModel.getEmail(), dbUserModel.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("Invalid Username or Password");
        }
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<RolesModel> roles) {

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (RolesModel tempRole : roles) {
            // System.out.println(tempRole.getRoleName());
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getRoleName());
            authorities.add(tempAuthority);

        }
        return authorities;
    }

    @Override
    public void deleteUserById(int userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void removeSessionMsg() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpServletRequest request = attr.getRequest();
        HttpSession session = request.getSession();

        session.removeAttribute("msg");
    }

    @Override
    public UserModel getuserbyID(int userId) {
        Optional<UserModel> dbModel = userRepo.findById(userId);
        if (dbModel.isPresent()) {
            return dbModel.get();
        } else
            return null;
    }
}
