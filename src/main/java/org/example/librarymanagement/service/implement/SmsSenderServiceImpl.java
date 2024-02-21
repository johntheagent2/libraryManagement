package org.example.librarymanagement.service.implement;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.ResourceBundle;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class SmsSenderServiceImpl implements org.example.librarymanagement.service.SmsSenderService {

    @Value("${account-sid}")
    private String accountSid;

    @Value("${auth-token}")
    private String authToken;

    @Value("${sms-sender}")
    private String sender;

    private final ResourceBundle resourceBundle;

    @Override
    public void sendSms(String receiver, String otp) {
        Twilio.init(accountSid, authToken);
        Message.creator(
                new PhoneNumber(resourceBundle.getString("phone.phone-country-code") + receiver),
                new PhoneNumber(resourceBundle.getString("phone.phone-country-code") + sender),
                "Your verification code is: " + otp).create();
    }
}
