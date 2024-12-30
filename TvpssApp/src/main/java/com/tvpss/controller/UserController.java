package com.tvpss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tvpss.model.User;
import com.tvpss.service.UserService;

@Controller
@RequestMapping("/superadmin")
public class UserController {

    @Autowired
    private UserService userService;

    // Super Admin Dashboard
    @GetMapping("/dashboard")
    public String showSuperAdminDashboard(Model model) {
        // Get statistics related to SuperAdmin
        long stateAdminCount = userService.getUserCountByRole(3);  // Role 3: State Admin
        long ppdAdminCount = userService.getUserCountByRole(2);    // Role 2: PPD Admin
        long schoolAdminCount = userService.getUserCountByRole(4); // Role 4: School Admin

        model.addAttribute("stateAdminCount", stateAdminCount);
        model.addAttribute("ppdAdminCount", ppdAdminCount);
        model.addAttribute("schoolAdminCount", schoolAdminCount);

        return "superadmin/dashboard"; // Return SuperAdmin dashboard page
    }

    // View all users (manage users page)
    @GetMapping("/manageUsers")
    public String manageUsers(Model model) {
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("users", allUsers);
        return "superadmin/manageUsers"; // Return SuperAdmin manage users page
    }

    // Show Add User Form
    @GetMapping("/addUser")
    public String showAddUserForm() {
        return "superadmin/addUser";
    }

    // Add a new user
    @PostMapping("/addUser")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("email") String email,
                          @RequestParam("role") int role,
                          @RequestParam("state") String state,
                          @RequestParam("password") String password,
                          @RequestParam("confirmPassword") String confirmPassword,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "superadmin/addUser"; // Return to the form with error and data
        }

        // Check if the username exists
        if (userService.isUsernameExists(username)) {
            model.addAttribute("error", "Username already exists.");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("role", role);
            model.addAttribute("state", state);
            return "superadmin/addUser"; // Return to the form with error and data
        }

        // Add the new user to the database
        userService.addUser(username, email, role, state, password);
        redirectAttributes.addFlashAttribute("success", "New user was successfully added.");
        return "redirect:/superadmin/manageUsers?success=true"; // Redirect to Manage Users page
    }

    // Show edit user form
    @GetMapping("/editUser")
    public String editUser(@RequestParam("username") String username, Model model) {
        User user = userService.findAllUsers().stream()
                               .filter(u -> u.getUsername().equals(username))
                               .findFirst()
                               .orElse(null);
        model.addAttribute("user", user);
        return "superadmin/editUser"; 
    }

    // Update user
    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("username") String username,
                             @RequestParam("email") String email,
                             @RequestParam("role") int role,
                             @RequestParam("state") String state,
                             RedirectAttributes redirectAttributes) {
        userService.updateUser(username, email, role, state);
        redirectAttributes.addFlashAttribute("success", "User updated successfully.");
        return "redirect:/superadmin/manageUsers?success=true";
    }

    // Delete a user
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        userService.deleteUser(username);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully!"); 
        return "redirect:/superadmin/manageUsers?success=true"; 
    }
}
