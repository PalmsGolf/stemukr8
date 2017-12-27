package com.mathpar.web.controller;

import com.mathpar.web.db.dao.DbUser;
import com.mathpar.web.db.entity.User;
import com.mathpar.web.db.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private DbUser dbUser;

    @RequestMapping(value = "/{adminId}", method = RequestMethod.GET)
    public ModelAndView getAdminCabinet(@PathVariable("adminId") long adminId) {
        Map<String, Object> model = new HashMap<>();
        User admin = dbUser.getUserById(adminId);
        model.put("admin", admin);
        return new ModelAndView("admin-cabinet", model);
    }

    @RequestMapping(value = "/edit-teachers", method = RequestMethod.GET)
    public ModelAndView getEditTeachers() {//(@PathVariable("adminId") long adminId) {
        Map<String,Object> model = new HashMap<>();
        List<User> teacherList = dbUser.getUserByType(UserRole.TEACHER);
        model.put("teachersList", teacherList);
        return new ModelAndView("edit-teachers", model);
    }

    @RequestMapping(value = "/edit-teachers/{teacherId}", method = RequestMethod.GET)
    public ModelAndView getTeacherToEdit(@PathVariable("teacherId") long teacherId) {
        Map<String,Object> model = new HashMap<>();
        model.put("teacher", dbUser.getUserById(teacherId));
        return new ModelAndView("teacherToEdit");
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public ModelAndView getAllStudents() {
        Map<String, Object> model = new HashMap<>();
        List<User> studentsList = dbUser.getUserByType(UserRole.STUDENT);
        model.put("student_list", studentsList);
        return new ModelAndView("students", model);
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView getAllAdmins() {
        Map<String, Object> model = new HashMap<>();
        List<User> adminList = dbUser.getUserByType(UserRole.SUPERADMIN);
        model.put("admin_list", adminList);
        return new ModelAndView("admins", model);
    }

    @RequestMapping(value = "/edit-teachers/{teacherId}", method = RequestMethod.POST)
    public ModelAndView getTeacherToEdit(@ModelAttribute("email") String email,
                                         @ModelAttribute("username") String username,
                                         @ModelAttribute("password") String password,
                                         @PathVariable("teacherId") long teacherId) {

        dbUser.update(email, username, password, teacherId);
        return new ModelAndView("redirect:/admin/edit-teachers");
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.GET)
    public ModelAndView addTeacher() {
        return new ModelAndView("new_user");
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.POST)
    public ModelAndView addTeacher(@ModelAttribute("email") String email,
                                   @ModelAttribute("username") String username,
                                   @ModelAttribute("password") String password,
                                   @ModelAttribute("user_role") int userRole) {
        System.out.println("User role: " + userRole);
        dbUser.save(email, username, password, userRole);
        if (userRole == 3)
            return new ModelAndView("redirect:/admin/edit-teachers");
        else if (userRole == 2)
            return new ModelAndView("redirect:/admin/students");
        else if (userRole == 6)
            return new ModelAndView("redirect:/admin/admins");
        else
            return new ModelAndView("redirect:/new_user");
    }

}