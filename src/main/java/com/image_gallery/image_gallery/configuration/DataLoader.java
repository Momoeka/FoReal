package com.image_gallery.image_gallery.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.image_gallery.image_gallery.entity.RolesModel;
import com.image_gallery.image_gallery.repo.RoleRepo;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<RolesModel> rolesModel = roleRepo.findByRoleName("ROLE_USER");
        if (rolesModel.isEmpty()) {
            RolesModel rolesModel2 = new RolesModel();
            rolesModel2.setRoleName("ROLE_USER");
            roleRepo.save(rolesModel2);

        }
    }

}
