package com.ayush.demo.auth;

import com.ayush.demo.Dto.LoginReqDto;
import com.ayush.demo.Dto.UserRequestDto;
import com.ayush.demo.Dto.UserResponseDto;
import com.ayush.demo.error.ResourceNotFoundException;
import com.ayush.demo.jwt.JWTservice;
import com.ayush.demo.user.User;
import com.ayush.demo.user.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTservice jwTservice;

    public AuthService(BCryptPasswordEncoder passwordEncoder,UserRepository userRepository,JWTservice jwTservice) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository=userRepository;
        this.jwTservice=jwTservice;
    }

    public UserResponseDto register(UserRequestDto userRequestDto){
        String pass=userRequestDto.getPassword();
        User user=new User();
        user.setAge(userRequestDto.getAge());
        user.setName(userRequestDto.getName());

        String hashedPassword=passwordEncoder.encode(pass);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        return new UserResponseDto(user.getId(),user.getName());
    }
    public String userLogin(LoginReqDto loginReqDto){
        User existing =userRepository.findByName(loginReqDto.getName())
                .orElseThrow(()->new ResourceNotFoundException("User with name "+loginReqDto.getName()+" not found"));
        String password=existing.getPassword();
        if(!passwordEncoder.matches(loginReqDto.getPassword(),password)){
            throw new BadCredentialsException("Invalid Password");
        }
        return jwTservice.generateToken(existing.getName());

    }

}
