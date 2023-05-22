package com.example.demo.repository;

import com.example.demo.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MenuRepository extends JpaRepository<Menu,Long> {
    @Override
    List<Menu> findAll();
    List<Menu> findAllByParentIsNull(Sort sort);
    @Query("select parentMenu from Menu parentMenu" +
            " left join parentMenu.children child " +
            " where parentMenu.parent is null" +
            " order by parentMenu.listOrder asc, child.listOrder asc")
    List<Menu> findAllWithJpql();
}
