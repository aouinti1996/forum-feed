package com.training.redditclone.services;

import com.training.redditclone.dto.SmsRequest;

public interface SmsSender {

    void sendSms(SmsRequest smsRequest);
}
