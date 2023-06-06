package ru.ecommerce.highstylewear.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import ru.ecommerce.highstylewear.config.jwt.JWTTokenUtil;
import ru.ecommerce.highstylewear.service.userdetails.CustomUserDetailsService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public abstract class CommonRestTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected JWTTokenUtil jwtTokenUtil;
    @Autowired
    protected CustomUserDetailsService customUserDetailsService;

    protected String token = "";

    protected HttpHeaders headers = new HttpHeaders();
    protected ObjectMapper objectMapper = new ObjectMapper();

    private String generateToken(final String username){
        return jwtTokenUtil.generateToken(customUserDetailsService.loadUserByUsername(username));
    }

    @BeforeAll
    public void prepare() {
        token = generateToken("admin");
        headers.add("Authorization", "Bearer " + token);
        objectMapper.registerModule(new JavaTimeModule());
    }

    protected abstract void createObject() throws Exception;

    protected abstract void updateObject() throws Exception;

    protected abstract void deleteObject() throws Exception;

    protected abstract void getAll() throws Exception;

    protected String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
