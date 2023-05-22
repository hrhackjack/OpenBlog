package com.HR.Blog.Controllers;

import com.HR.Blog.Entities.User;
import com.HR.Blog.Repositories.UserRepo;
import com.HR.Blog.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@RestController
@RequestMapping("/api/reset-password")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping
    public ResponseEntity<?> resetPassword(@RequestParam String email) throws Exception {
//        String email = request.getParameter("email");
        User user = userRepo.findByEmail(email).get();

        if (user == null) {
            return ResponseEntity.ok().body("Could not find user with email " + email);
        }

        String token = UUID.randomUUID().toString();

        userService.createPRTForUser(user, token);

        // send email with reset URL containing token
        String resetUrl = "http://localhost:7989/api/reset-password?token=" + token;
        String subject = "Password Reset Link";
        String text = "To reset your password, click the link below:\n\n" + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ModelAndView showResetPasswordForm(@RequestParam String token, Model model) {
        User user = userService.getFromPRT(token).getUser();

        if (user == null) {
            new ModelAndView("error-page");
        }

        model.addAttribute("token", token);
        return new ModelAndView( "reset-password");
    }

    @PostMapping("/submit")
    public ResponseEntity<?> resetPasswordSubmit(HttpServletRequest request) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String url = "http://localhost:7989/api/reset-password/submit?token=" + token + "&password=" + password;
        User user = userService.getFromPRT(token).getUser();
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid Password Reset Token !!");
        }

        user.setPassword(this.passwordEncoder.encode(password));
        userRepo.save(user);
        userService.deletePRTForUser(user);

        return ResponseEntity.ok().body("Password Successfully Reset !!");
    }
}

