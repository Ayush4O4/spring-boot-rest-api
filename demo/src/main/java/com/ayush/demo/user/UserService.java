package com.ayush.demo.user;

import com.ayush.demo.Dto.UserRequestDto;
import com.ayush.demo.Dto.UserResponseDto;
import com.ayush.demo.error.ResourceNotFoundException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto dto){

        User user=new User();
        user.setName(dto.getName());
        user.setAge(dto.getAge());
         User saved=userRepository.save(user);
        return new UserResponseDto(saved.getId(), saved.getName());
    }

    public Page<UserResponseDto> getAllUsers(Pageable pageable){

        Page<User> users=userRepository.findAll(pageable);
        return users.map(user -> new UserResponseDto(user.getId(),user.getName()));
    }

    @Cacheable(value = "users",key = "#id")
    public UserResponseDto getUserById(Long id) throws AccessDeniedException {
        String currentUserName= extractUsernameFromAuthenticatedUser();
        User existing =userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user with id "+id+" not found"));
        String username=existing.getName();
        if(!currentUserName.equals(username)){
            throw new AccessDeniedException("Acess Denied");
        }
        return new UserResponseDto(existing.getId(),existing.getName());
    }

    @CacheEvict(value = "users",key = "#id")
    public void deleteUserById(Long id) throws AccessDeniedException {
        String currentUserName=extractUsernameFromAuthenticatedUser();

        User existing=userRepository.findById(id)
                        .orElseThrow(()->new ResourceNotFoundException("user with id "+id+" not found"));
        String username= existing.getName();
        if(!currentUserName.equals(username)){
            throw new AccessDeniedException("Acess Denied");
        }
        userRepository.deleteById(id);
    }


    @CachePut(value = "users",key = "#id")
    public UserResponseDto updateUser( Long id,  UserRequestDto updatedUser) throws AccessDeniedException {
        String currentUsername=extractUsernameFromAuthenticatedUser();
        User existing =userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with id "+ id+" not found" ));
        if(!currentUsername.equals(existing.getName())){
            throw new AccessDeniedException("Access Denied");
        }
        existing.setName(updatedUser.getName());
        existing.setAge(updatedUser.getAge());
        User saved=userRepository.save(existing);

        return new UserResponseDto(saved.getId(),saved.getName());
    }


    public Page<UserResponseDto> findUserByAge(int id,Pageable pageable){
        Page<User>users=userRepository.findByAge(id,pageable);
        return users.map(user -> new UserResponseDto(user.getId(),user.getName()));
    }

    public String uploadUserProfilePicture(Long id, MultipartFile file) throws IOException {
        User existing=userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user with id "+id+" not found"));
        Path uploadDir= Paths.get("uploads");
        Files.createDirectories(uploadDir);

        String fileName=existing.getName()+"_"+file.getOriginalFilename();
        Path filePath=uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(),filePath);

        return filePath.toString();
    }

    public String extractUsernameFromAuthenticatedUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
