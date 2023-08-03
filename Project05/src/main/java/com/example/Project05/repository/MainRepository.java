package com.example.Project05.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project05.model.TModel;

public interface MainRepository extends JpaRepository<TModel,Long>{
    
}