package com.ai.finderai.repositories;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ai.finderai.dto.VectorDataDTO;
import com.ai.finderai.enums.EmbeddingType;
import com.ai.finderai.utils.DatabaseUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Repository for executing raw SQL queries for vector data retrieval.
 */
@Repository
@Schema(description = "Repository for performing direct SQL operations on vector data")
public class VectorDataJDBCRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseUtils databaseUtils;

    public VectorDataJDBCRepository(JdbcTemplate jdbcTemplate, DatabaseUtils databaseUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseUtils = databaseUtils;
    }

    /**
     * Finds the top N closest records to the given embedding using cosine
     * similarity.
     *
     * @param embedding The input embedding as a comma-separated string (e.g.,
     *                  "-0.06277176,0.12345678").
     * @param limit     The number of closest records to return.
     * @return A list of the closest VectorDataDTO records.
     */
    public List<VectorDataDTO> getClosestRecords(String embedding, int limit) {
        String sql = """
                SELECT id, embedding_type, embedding, metadata, model, provider, source, created_at
                FROM vector_data
                ORDER BY embedding <=> string_to_array(?, ',')::float[]::vector
                LIMIT ?
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    // Return the DTO using the values from the result set
                    return new VectorDataDTO(
                            rs.getLong("id"),
                            rs.getString("provider"),
                            rs.getString("model"),
                            EmbeddingType.valueOf(rs.getString("embedding_type")),
                            rs.getString("source"),
                            null,
                            databaseUtils.parseJsonbToMap(rs.getString("metadata")),
                            rs.getTimestamp("created_at").toLocalDateTime());
                },
                embedding, limit);

    }
}
