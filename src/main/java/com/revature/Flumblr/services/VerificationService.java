package com.revature.Flumblr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.revature.Flumblr.dtos.responses.ExternalAPIResponse;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;


@Service
public class VerificationService {

    private JavaMailSender javaMailSender;

    @Value("${KickboxKEY}")
    private String apiKey;

    
    @Value("${KickboxURL}")
    private String baseURL;

    @Autowired
    public VerificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${apiURL1}")
    private String apiUrl1;

    @Value("${apiURL2}")
    private String apiUrl2;


    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
    

    public boolean isValidEmailAddress(String email) {

        RestTemplate restTemplate = new RestTemplate();

        String apiURL = baseURL + email + "&apikey=" + apiKey;

        System.out.println(apiURL);

        ResponseEntity<ExternalAPIResponse> response = restTemplate.getForEntity(apiURL, ExternalAPIResponse.class);

        ExternalAPIResponse externalAPIResponse = response.getBody();

        System.out.println("here");
        System.out.println(externalAPIResponse);

        if(externalAPIResponse != null){
            
        if (externalAPIResponse.getDisposable().equalsIgnoreCase("true")) {
            System.out.println(externalAPIResponse.getDisposable());
            throw new ResourceConflictException("Disposable email is not allowed!");
        }
        
        if (externalAPIResponse.getResult().equalsIgnoreCase("false")) {
            System.out.println(externalAPIResponse.getResult());
            throw new ResourceConflictException("email is not allowed!  " + externalAPIResponse.getReason());
        }

        if (externalAPIResponse.getSuccess().equalsIgnoreCase("false")) {
            System.out.println(externalAPIResponse.getSuccess());
            throw new ResourceConflictException("email is not allowed!  " + "did you mean " + externalAPIResponse.getDid_you_mean());
        }     
        
        if (externalAPIResponse.getSuccess().equalsIgnoreCase("false")) {
            System.out.println(externalAPIResponse.getSuccess());
            throw new ResourceConflictException("email is not allowed!  " + "did you mean " + externalAPIResponse.getDid_you_mean());
        } 

        }else{
            throw new ResourceConflictException("Email is not allowed!"); 
        }
        
        return true;
    }


    public SimpleMailMessage composeVerification(String email, String verificationToken){

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : " + apiUrl1 +  verificationToken);

        return mailMessage;
    }

    public SimpleMailMessage composeResetPassword(String email, String verificationToken){
        
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Password Reset!");
        mailMessage.setText("To reset our Password, please click here : " + apiUrl2 +  verificationToken);

        return mailMessage;
    }

}
