package com.ai.finderai.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.finderai.enums.EmbeddingType;
import com.ai.finderai.models.VectorData;

/**
 * Repository interface for performing CRUD operations on the VectorData entity.
 */
@Repository
public interface VectorDataRepository extends JpaRepository<VectorData, Long> {
    // Find all embeddings of a specific type (TEXT, IMAGE, FILE, etc.)
    List<VectorData> findByEmbeddingType(EmbeddingType embeddingType);
}
