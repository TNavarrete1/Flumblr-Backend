package com.revature.Flumblr.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import com.revature.Flumblr.services.TokenService;
import com.revature.Flumblr.services.UserService;

public class UserControllerTest {
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;


    @BeforeEach
    public void setup() {
    userController = new UserController(userService, tokenService);
}

// @Test
// public void registerUserTest() {

// }

// @Test
// public void loginUserTest() {
    
// }

}