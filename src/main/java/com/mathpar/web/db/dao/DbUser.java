package com.mathpar.web.db.dao;

import com.mathpar.web.db.entity.User;
import com.mathpar.web.db.entity.UserRole;
import com.mathpar.web.db.entity.mappers.UserMapper;
import com.mathpar.web.db.util.SecurityUtil;
import com.mathpar.web.exceptions.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class)
public class DbUser {
    private static final Logger LOG = LoggerFactory.getLogger(DbUser.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private NamedParameterJdbcTemplate jdbcTpl;
    private UserMapper userMapper = new UserMapper();

    private static final String INSERT_USER = "" +
            "INSERT INTO mathpar_users (email, username, password, salt, registration_date, user_role_id) " +
            "VALUES (:email, :username, :password, 'test', :regDate, :userRoleId)";
    private static final String INSERT_USER_TO_STUDENTS = "" +
            "INSERT INTO students (id_user, id_group) " +
            "VALUES (:idUser, :idGroup) ;";
    private static final String GET_USER_BY_EMAIL = "" +
            "SELECT id, username, email, password, salt, registration_date, user_role_id " +
            "FROM mathpar_users WHERE email = :email";
    private static final String UPDATE_USER = "UPDATE mathpar_users SET username = :username, email = :email, password = :password WHERE id = :userId";

    @Autowired
    public void setJdbcTpl(NamedParameterJdbcTemplate jdbcTpl) {
        this.jdbcTpl = jdbcTpl;
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public long save(String email, String username, String password, int userRole) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTpl.update(INSERT_USER, new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("username", username)
                .addValue("password", bCryptPasswordEncoder.encode(password))
                .addValue("regDate", new Date())
                .addValue("userRoleId", userRole), keyHolder);
        return (Long) keyHolder.getKeys().get("SCOPE_IDENTITY()");
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public void update(String email, String username, String password, long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        jdbcTpl.update(UPDATE_USER, new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("username", username)
                .addValue("password", bCryptPasswordEncoder.encode(password))
                .addValue("userId", userId), keyHolder);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public User getUser(String email, String password) throws IOException,
            NoSuchAlgorithmException, AuthException {
        User user = jdbcTpl.queryForObject(GET_USER_BY_EMAIL,
                    new MapSqlParameterSource().addValue("email", email), userMapper);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(password, user.getPassword()))
            throw new AuthException();
        return user;
    }

    public void insertIntoStudents(long userId, long idGroup) {
        jdbcTpl.update(INSERT_USER_TO_STUDENTS, new MapSqlParameterSource()
                .addValue("idUser", userId)
                .addValue("idGroup", idGroup));
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public long getStudentIdByUserId(long userId) {
        List<Long> tmp = jdbcTpl.queryForList(
                "SELECT id FROM students WHERE id_user = :userId",
                new MapSqlParameterSource("userId", userId), Long.class);
        if (tmp != null && tmp.size() == 1) {
            return tmp.get(0);
        } else {
            return -1;
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public String getUsernameByUserId(long userId) {
        //System.out.println("User " + jdbcTpl.queryForObject(
        //        "SELECT username FROM mathpar_users WHERE id = :userId",
        //        new MapSqlParameterSource("userId", userId), String.class));

        return jdbcTpl.queryForObject(
                "SELECT username FROM mathpar_users WHERE id = :userId",
                new MapSqlParameterSource("userId", userId), String.class);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public String getUsernameByStudentId(long studentId) {
        return jdbcTpl.queryForObject("" +
                        "SELECT username FROM mathpar_users u " +
                        "JOIN (SELECT id_user FROM students WHERE id = :studentId) s ON (s.id_user = u.id)",
                new MapSqlParameterSource("studentId", studentId), String.class);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<User> getStudentsByGroup(long groupId) {
        String sql = "SELECT * FROM mathpar_users u JOIN (SELECT id_user FROM students WHERE ID_GROUP = :groupId) s ON (s.id_user = u.id)";
        return jdbcTpl.query(sql, new MapSqlParameterSource("groupId", groupId), userMapper);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public User getUserById(long userId) {
        String sql = "SELECT * FROM mathpar_users WHERE ID = :userId";
        return jdbcTpl.queryForObject(sql, new MapSqlParameterSource("userId", userId), userMapper);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<User> getAllTeachers() {
        String sql = "SELECT * FROM mathpar_users WHERE user_role_id = 3";
        return jdbcTpl.query(sql, userMapper);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<User> getUserByType(UserRole userRole) {
        String sql = "SELECT * FROM mathpar_users WHERE user_role_id = :userRole";
        return jdbcTpl.query(sql, new MapSqlParameterSource().addValue("userRole", userRole.ordinal()), userMapper);
    }
}