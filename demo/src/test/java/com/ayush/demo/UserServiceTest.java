package com.ayush.demo;

import com.ayush.demo.Dto.UserRequestDto;
import com.ayush.demo.Dto.UserResponseDto;
import com.ayush.demo.error.ResourceNotFoundException;
import com.ayush.demo.user.User;
import com.ayush.demo.user.UserRepository;
import com.ayush.demo.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;




    @InjectMocks
    UserService userService;

    @Test
    void getUserById_WhenUserExists_ReturnsUserResponseDto() throws AccessDeniedException {
        //AAA method is used here, arrange,act,assert
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Ayush");
        user.setAge(20);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("Ayush", null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
        //Act
        UserResponseDto result=userService.getUserById(1L);

        //Assert
        assertEquals("Ayush",result.getName());
    }
    @Test
    void getUserById_WhenUserDoesNotExist_ThrowsResoursceNotFoundException(){
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("Ayush", null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertThrows(ResourceNotFoundException.class,()->{
            userService.getUserById(999L);
        });
    }



    @Test
    void createUser_ReturnUserResponseDto(){
        UserRequestDto userRequestDto=new UserRequestDto("ABC",20,"ABC");

        User user=new User();
        user.setName(userRequestDto.getName());
        user.setAge(userRequestDto.getAge());
        user.setPassword(userRequestDto.getPassword());
        when(userRepository.save(any())).thenReturn(user);
         UserResponseDto result=userService.createUser(userRequestDto);

         assertEquals("ABC",result.getName());
    }

    @Test
    void createUser_WhenUserAlreadyExist_throwsDataIntegrityViolationException(){
        UserRequestDto userRequestDto=new UserRequestDto("A",30,"A");
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
        assertThrows(DataIntegrityViolationException.class,()->{
            userService.createUser(userRequestDto);
        });
    }

    @Test
    void deleteUser_WhenUserIsAvailable_returnNothing() throws AccessDeniedException {
        User user=new User();
        user.setAge(20);
        user.setName("A");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("A", null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
        userService.deleteUserById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void getAllUsers_returns_ListOfUsers(){
        User user1=new User();
        user1.setId(1L);
        user1.setName("a");
        user1.setAge(21);

        User user2 =new User();
        user2.setId(2L);
        user2.setName("B");
        user2.setAge(22);
        List<User> user=new ArrayList<>();
        user.add(user1);
        user.add(user2);
        Pageable pageable= PageRequest.of(0,10);
        Page<User> page = new PageImpl<>(user);
        when(userRepository.findAll(pageable)).thenReturn(page);
        Page<UserResponseDto> result = userService.getAllUsers(pageable);
        assertEquals(2, result.getContent().size());
        assertEquals("a", result.getContent().get(0).getName());
    }

    @Test
    void deleteUser_doesntExist_throwsResourceNotFoundException(){
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("Ayush", null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertThrows(ResourceNotFoundException.class,()->{
            userService.deleteUserById(999L);
        });
        verify(userRepository,never()).deleteById(any());
    }

}
