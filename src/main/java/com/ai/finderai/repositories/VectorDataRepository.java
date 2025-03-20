package com.ai.finderai.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.finderai.models.VectorData;

@Repository
public interface VectorDataRepository extends JpaRepository<VectorData, Long> {
}