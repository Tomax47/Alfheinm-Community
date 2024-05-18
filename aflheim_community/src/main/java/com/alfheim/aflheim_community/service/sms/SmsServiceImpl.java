package com.alfheim.aflheim_community.service.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Component
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${sms.ru.url}")
    private String smsUrl;

    @Value("${sms.ru.api_id}")
    private String apiId;

    @Value("${sms.ru.msg}")
    private String messageUrl;

    @Value("${sms.ru.json}")
    private String messageJson;
    private RestTemplate restTemplate = new RestTemplate();
    @Override
    public String sendWelcomeMsg(String phone) {

        String text = generateWelcomeMsg();

        String url = smsUrl + apiId + phone + messageUrl + text + messageJson;
        log.info(url);
        String result = restTemplate.getForObject(url, String.class);
        log.info(result);

        // Returning the code
        return text;
    }

    private String generateWelcomeMsg() {

        return " Welcome to the newsletter! You have subscribed to the community's newsletter. Here we will be sharing the latest updates and news.";
    }
}
