package com.image_gallery.image_gallery.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.image_gallery.image_gallery.entity.RolesModel;

@Repository
public interface RoleRepo extends JpaRepository<RolesModel, Integer> {
    List<RolesModel> findByRoleName(String roleName);
}