package com.revature.Flumblr.services;

import com.revature.Flumblr.dtos.requests.NewLoginRequest;
import com.revature.Flumblr.dtos.requests.NewUserRequest;
import com.revature.Flumblr.dtos.responses.Principal;
import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.repositories.ProfileRepository;
import com.revature.Flumblr.repositories.UserRepository;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ThemeService themeService;

    @BeforeEach
    public void setup() {
        // userService = new UserService(userRepository, roleService, profileRepository, null, null, themeService);
    }

    @Test
    public void registerUserTest() {

        // Create a mock for NewUserRequest
        NewUserRequest newUserRequest = new NewUserRequest();
        newUserRequest.setUsername("testUser");
        newUserRequest.setPassword("password");
        newUserRequest.setEmail("test@example.com");

        // Create a mock for the dependencies
        Role userRole = new Role();
        Theme defaultTheme = new Theme("default", null, null);
        User savedUser = new User("testUser", "hashedPassword", "test@example.com", userRole);
        Profile savedProfile = new Profile(savedUser, "", "", defaultTheme);

        // Mock the behavior of the dependencies
        when(roleService.getByName("USER")).thenReturn(userRole);
        when(themeService.findByName("default")).thenReturn(defaultTheme);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(profileRepository.save(any(Profile.class))).thenReturn(savedProfile);

        // Call the method under test
        User createdUser = userService.registerUser(newUserRequest);

        // Assert the results
        assertEquals(savedUser, createdUser);
        verify(userRepository, times(1)).save(any(User.class));
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    public void loginTest() {

        // Mock user data
        String username = "testuser";
        String password = "testpassword";
        Role userRole = new Role();
        User user = new User(username, BCrypt.hashpw(password, BCrypt.gensalt()), "testemail@gmail", userRole);

        // Create a login request with valid username and password
        NewLoginRequest request = new NewLoginRequest(username, password);

        // Mock the userRepository behavior
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Call the login method
        Principal result = userService.login(request);

        // Verify that the userRepository.findByUsername method was called
        verify(userRepository, times(1)).findByUsername(username);

        // Verify that the returned Principal contains the expected User
        assertEquals(user.getUsername(), result.getUsername());

    }

    @Test
    public void testFindById() {
        // Mock user data
        Role userRole = new Role();
        User user = new User("testUsername", "testPassword", "testpassword", userRole);

        // Mock the userRepository behavior
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Call the findById method
        User result = userService.findById(user.getId());

        // Verify that the userRepository.findById method was called
        verify(userRepository, times(1)).findById(user.getId());

        // Verify that the returned User matches the expected user
        assertEquals(user, result);
    }

    @Test
    public void testFindByUsername() {
        Role userRole = new Role();
        String username = "testUsername";
        User user = new User(username, "testPassword", "testpassword", userRole);

        // Mock the userRepository behavior
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Call the findById method
        User result = userService.findByUsername(username);

        // Verify that the userRepository.findById method was called
        verify(userRepository, times(1)).findByUsername(username);

        // Verify that the returned User matches the expected user
        assertEquals(user, result);
    }

    @Test
    public void isValidUsernameTest() {
        String validUsername = "test_user123";
        String invalidUsername = "test user";

        assertTrue(userService.isValidUsername(validUsername));
        assertFalse(userService.isValidUsername(invalidUsername));

    }

    @Test
    public void isValidPasswordTest() {
        String validPassword = "Password123";
        String invalidPassword = "password";

        assertTrue(userService.isValidPassword(validPassword));
        assertFalse(userService.isValidPassword(invalidPassword));
    }

    @Test
    public void isSamePasswordTest() {
        String password = "Password123";
        String confirmPassword = "Password123";
        String incorrectPassword = "Password12";

        assertTrue(userService.isSamePassword(password, confirmPassword));
        assertFalse(userService.isSamePassword(password, incorrectPassword));
    }

    @Test
    public void uniqueUsernameTest() {
        String username = "testUsername";

        // Mock the userRepository behavior
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Call the isUniqueUsername method
        boolean result = userService.isUniqueUsername(username);

        // Verify that the userRepository.findByUsername method was called
        verify(userRepository, times(1)).findByUsername(username);

        // Verify the expected result
        assertTrue(result);
    }

    @Test
    public void usernameExistsTest() {
        Role userRole = new Role();
        String username = "testUsername";
        User user = new User(username, "testPassword", "testpassword", userRole);

        // Mock the userRepository behavior
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.usernameExists(username);

        // Verify that the userRepository.findByUsername method was called
        verify(userRepository, times(1)).findByUsername(username);

        assertTrue(result);
    }

}