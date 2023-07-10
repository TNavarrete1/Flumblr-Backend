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


    public SimpleMailMessage composeVerification(String email, String verificationToken){

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : " + apiUrl1 +  verificationToken);

        return mailMessage;
    }

    public SimpleMailMessage composeResetPassword(String email, String verificationToken){
        
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        
        String emailContent = "We have received a request to reset the password associated with your account. As a security measure, we are sending you this confirmation email to ensure that it was indeed you who initiated the password reset process.\n\n";
                emailContent += "If you did not request this change, please disregard this message and take the necessary steps to secure your account.\n\n";
                emailContent += "To complete the password reset, please click on the following link:\\n";
                emailContent += "To reset your password, please click on the following link:\n\n";

        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Password Reset!");
        mailMessage.setText(emailContent + apiUrl2 +  verificationToken);

        return mailMessage;
    }

     public SimpleMailMessage composeConfirmation(String email){
        
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        
        String emailContent = "This email is to confirm that the password associated with your account has been successfully changed. We are sending you this notification to ensure that you are aware of the recent password update.\n\n";
                emailContent += "If you did not initiate this change or if you believe your account security has been compromised, please contact our support team immediately to take the necessary actions.\n\n";
                emailContent += "If you have any further questions or need assistance, feel free to reach out to our support team at flumblr.suport@gmail.com.\n\n";
                emailContent += "Thank you for your attention to this matter.\n\n";
                emailContent += "Best regards,\n";
                emailContent += "Flumblr";

        mailMessage.setTo(email);
        mailMessage.setSubject("Password changed successfully!");
        mailMessage.setText(emailContent);

        return mailMessage;
    }

}
