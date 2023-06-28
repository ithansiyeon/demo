package com.example.demo.repository.menu;

import com.example.demo.entity.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long>, MenuRepositoryCustom {
}
