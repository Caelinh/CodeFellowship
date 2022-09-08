package com.codeFellowship.codeFellowship.controllers;

import com.codeFellowship.codeFellowship.model.AppUser;
import com.codeFellowship.codeFellowship.repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    AppUserRepo AppUserRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;


    @GetMapping("/")
    public String getHomePage(Principal p, Model m)
    {
        if (p != null)
        {
            String username = p.getName();
            AppUser newInitiate = AppUserRepo.findByUsername(username);

            m.addAttribute("username", username);
            m.addAttribute("firstName", newInitiate.getFirstName());
        }

        return "/";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/home")
    public String getHomePage(){
        return "home";
    }

    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup";
    }


    @PostMapping("/signup")
    public RedirectView createUser(String username, String password, String firstName, String lastName, String dateOfBirth, String bio){
        String hashedPassword = passwordEncoder.encode(password);
        AppUser newUser = new AppUser(username,hashedPassword,firstName,lastName,dateOfBirth,bio);
        AppUserRepo.save(newUser);
        // pre auth with HttpServletReq
        authWithHttpServletRequest(username, password);
        return new RedirectView("/");
    }

    @PostMapping("/login")
    public RedirectView loginUser(String username, String password) {
        authWithHttpServletRequest(username,password);
        return new RedirectView("/");
    }

    @PutMapping("/users/{id}")
    public RedirectView editUserInfo(Model m, Principal p, @PathVariable Long id, String username, String nickname, RedirectAttributes redir){
        if(p != null && p.getName().equals(username)){
            AppUser newUser = AppUserRepo.findById(id).orElseThrow();
            newUser.setUsername(username);
            AppUserRepo.save(newUser);
        } else {
            redir.addFlashAttribute("errorMessage", "Cannot edit another user's info");
        }
        return new RedirectView("/users/" + id);

    }


    public void authWithHttpServletRequest(String username, String password){
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }




}
