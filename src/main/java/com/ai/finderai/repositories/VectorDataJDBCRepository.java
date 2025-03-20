package com.ai.finderai.repositories;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ai.finderai.dto.VectorDataDTO;
import com.ai.finderai.utils.DatabaseUtils;

@Repository
public class VectorDataJDBCRepository {

    private JdbcTemplate jdbcTemplate;
    private final DatabaseUtils databaseUtils;

    public VectorDataJDBCRepository(JdbcTemplate jdbcTemplate, DatabaseUtils databaseUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseUtils = databaseUtils;
    }

    /**
     * Find the top N closest records to the given embedding using cosine
     * similarity.
     *
     * @param embedding The input embedding as a comma-separated string (e.g.,
     *                  "-0.06277176,0.12345678").
     * @param limit     The number of closest records to return.
     * @return A list of the closest VectorDataDTO records.
     */
    public List<VectorDataDTO> getClosestRecords(String embedding, int limit) {
        String sql = """
                SELECT id, provider, model, text, metadata, created_at
                FROM vector_data
                ORDER BY embedding <=> string_to_array(?, ',')::float[]::vector
                LIMIT ?
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new VectorDataDTO(
                        rs.getLong("id"),
                        rs.getString("provider"),
                        rs.getString("model"),
                        rs.getString("text"), null,
                        databaseUtils.parseJsonbToMap(rs.getString("metadata")),
                        rs.getTimestamp("created_at").toLocalDateTime()),
                embedding, limit);
    }
}
