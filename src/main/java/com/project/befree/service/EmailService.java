package com.project.befree.service;

import com.project.befree.dto.EmailMsgDTO;

import java.util.Map;

public interface EmailService {

    Map<String, String> sendMail(EmailMsgDTO emailMessage);
}
