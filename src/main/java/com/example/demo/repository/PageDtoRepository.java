package com.example.demo.repository;

import com.example.demo.entity.PageDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageDtoRepository extends JpaRepository<PageDTO,Long> {
    @Override
    List<PageDTO> findAll(Sort sort);
}
