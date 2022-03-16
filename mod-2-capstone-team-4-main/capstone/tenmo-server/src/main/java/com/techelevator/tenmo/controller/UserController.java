package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
//@PreAuthorize("isAuthenticated()")
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> listAllUsers(Principal principal) {
        return userDao.findAll();
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public User get(Principal principal) { return userDao.findByUsername(principal.getName()); }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable("userId") int userId){return userDao.findByUserId(userId);}

}
