package com.psvgs.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.psvgs.models.ImmutableUser;
import com.psvgs.models.User;

@Repository
public class UserDAO implements DAO<User> {

    private static final RowMapper<User> ROW_MAPPER = new RowMapper<User>() {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return ImmutableUser.builder().id(rs.getString("id")).username(rs.getString("username"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime()).build();
        }

    };

    private static final String SELECT_BY_ID = "select * from users where id = ?";

    private static final String INSERT = "insert into users values (?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(String id) {
        try {
            return Optional.of(
                    jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { Objects.requireNonNull(id) }, ROW_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        LocalDateTime timestamp = LocalDateTime.now();
        ImmutableUser clone = ImmutableUser.builder().from(user).id(UUID.randomUUID().toString()).createdAt(timestamp)
                .updatedAt(timestamp).build();
        jdbcTemplate.update(INSERT, new Object[] { clone.getId(), clone.getUsername(), timestamp, timestamp });
        return clone;
    }

    @Override
    public User update(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User deleteById(String id) {
        throw new UnsupportedOperationException();
    }

}
