package com.ayush.demo;

import com.ayush.demo.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    @WithMockUser
    void getAllUser_returnsOK() throws Exception{
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void register_ReturnCreatedUSer() throws Exception{
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"testuser\",\"age\":20,\"password\":\"test123\"}")

        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testuser"));

    }

    @AfterEach
    void clearUp(){
        userRepository.findByName("testuser")
                .ifPresent(user -> userRepository.delete(user));
    }

}
