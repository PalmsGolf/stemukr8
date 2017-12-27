package com.mathpar.web.controller;

import com.mathpar.web.db.dao.DbUser;
import com.mathpar.web.db.entity.User;
import com.mathpar.web.db.entity.UserRole;
import com.mathpar.web.exceptions.AuthException;
import com.mathpar.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


@Controller
@RequestMapping(value = "/login")
public class Login {

    @Autowired
    private DbUser dbUser;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("username") String login, @ModelAttribute("password") String password) {
        try {
            User user = userService.getUser(login, password);
            UserRole userRole = user.getRole();
            long userId = user.getId();

            if (userRole.equals(UserRole.TEACHER))
                return new ModelAndView("redirect:/view/teacher/" + userId);
            else if (userRole.equals(UserRole.SUPERADMIN))
                return new ModelAndView("redirect:/admin/" + userId);
            else
                return new ModelAndView("redirect:login");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (AuthException e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:login");
    }
}