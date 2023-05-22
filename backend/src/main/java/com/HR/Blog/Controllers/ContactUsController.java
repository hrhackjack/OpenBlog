package com.HR.Blog.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api")
public class ContactUsController {

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/contact-us")
    public ResponseEntity<?> resetPassword(@RequestBody String details) throws Exception {

        Pattern pattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3}");
        Matcher matcher = pattern.matcher(details);
        String email = "hrhackjackop@gmail.com";
        if (matcher.find()) {
            System.out.println(matcher.group());
        } else {
            System.out.println("Email not found...");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Contact Details:\n\n");
        sb.append("Name: ").append(getValueFromJson(details, "name")).append("\n");
        sb.append("Email ID: ").append(getValueFromJson(details, "email")).append("\n");
        sb.append("Subject: ").append(getValueFromJson(details, "subject")).append("\n");
        sb.append("Message: ").append(getValueFromJson(details, "message")).append("\n");

        String subject = "Someone Contacted from OpenBlog !!";
        String text = sb.toString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);

        return ResponseEntity.ok().build();
    }

    private String getValueFromJson(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }
}
