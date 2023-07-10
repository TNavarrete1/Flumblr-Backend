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

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class VerificationService {

    private JavaMailSender javaMailSender;
    
     @Autowired
    public VerificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${KickboxKEY}")
    private String apiKey;

    
    @Value("${KickboxURL}")
    private String baseURL;

    

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
    

    public boolean isValidEmailAddress(String email) {

        boolean bool = true;

        RestTemplate restTemplate = new RestTemplate();

        String apiURL = baseURL + email + "&apikey=" + apiKey;

        ResponseEntity<ExternalAPIResponse> response = restTemplate.getForEntity(apiURL, ExternalAPIResponse.class);

        ExternalAPIResponse externalAPIResponse = response.getBody();


        if(externalAPIResponse != null){
            
        if (externalAPIResponse.getDisposable().equalsIgnoreCase("true")) {
            bool = false;
            throw new ResourceConflictException("Disposable email is not allowed!");
        }
        
        if (externalAPIResponse.getResult().equalsIgnoreCase("false")) {
            bool = false;
            throw new ResourceConflictException("email is not allowed!  " + externalAPIResponse.getReason());
        }

        if (externalAPIResponse.getSuccess().equalsIgnoreCase("false")) {
            bool = false;
            throw new ResourceConflictException("email is not allowed!  " + "did you mean " + externalAPIResponse.getDid_you_mean());
        }     
        
        if (externalAPIResponse.getSuccess().equalsIgnoreCase("false")) {
            bool = false;
            throw new ResourceConflictException("email is not allowed!  " + "did you mean " + externalAPIResponse.getDid_you_mean());
        } 

        }else{
            bool = false;
            throw new ResourceConflictException("Email is not allowed!"); 
        }
        
        return bool;
    }

}
