package com.HR.Blog.Repositories;


import com.HR.Blog.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepo  extends JpaRepository<Role, Integer>{

}
