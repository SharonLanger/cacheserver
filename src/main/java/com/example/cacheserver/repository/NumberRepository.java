package com.example.cacheserver.repository;

import com.example.cacheserver.entity.Numbers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRepository  extends JpaRepository<Numbers, Integer> {
}
