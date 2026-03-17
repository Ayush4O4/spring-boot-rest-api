package com.ayush.demo;

import com.ayush.demo.Dto.LoginReqDto;
import com.ayush.demo.Dto.UserRequestDto;
import com.ayush.demo.auth.AuthService;
import com.ayush.demo.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthService authService;

    @BeforeEach
    void setup(){
        authService.register(new UserRequestDto("Ayush",20,"test123"));
    }

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

    @Test
    void registerUserWith_duplicateUsername_throwsError() throws Exception {
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ayush\",\"age\":20,\"password\":\"test123\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void login_ReturnJWTtoken() throws Exception{
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ayush\",\"password\":\"test123\"}")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

    }

    @Test
    void login_returnException() throws Exception{
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ayush\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserById_usingJWT_returnUser() throws Exception {
        String token=authService.userLogin(new LoginReqDto("Ayush","test123"));
        Long userId=userRepository.findByName("Ayush").get().getId();
        mockMvc.perform(get("/users/{id}",userId)
                .header("Authorization","Bearer "+token)

                )
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_WithoutJWTToken_throwsError() throws Exception {
        Long userId=userRepository.findByName("Ayush").get().getId();
        mockMvc.perform(get("/users/{id}",userId)
        ).andExpect(status().isForbidden());
    }

    @Test
    void deleteUserById_whenUserExist() throws Exception {
        String token=authService.userLogin(new LoginReqDto("Ayush","test123"));
        Long userId=userRepository.findByName("Ayush").get().getId();
        mockMvc.perform(delete("/users/{id}",userId)
                .header("Authorization","Bearer "+token))
                .andExpect(status().isOk());
    }



    @AfterEach
    void clearUp(){
        userRepository.findByName("Ayush")
                .ifPresent(user -> userRepository.delete(user));

        userRepository.findByName("testuser")
                .ifPresent(user->userRepository.delete(user));
    }



}
