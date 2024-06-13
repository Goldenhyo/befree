package com.project.befree.service;

import com.project.befree.dto.EmailMsgDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public Map<String, String> sendMail(EmailMsgDTO emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage(); // MimeMessage 객체 생성
        try {
            // MimeMessageHelper를 사용하여 보다 쉽게 MimeMessage를 구성할 수 있다.
            MimeMessageHelper mimeMessageHelper = null;
            try {
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            // 이메일 수신자 설정
            mimeMessageHelper.setTo(emailMessage.getTo());

            // 이메일 제목 설정
            mimeMessageHelper.setSubject(emailMessage.getSubject());

            // 본문 내용 설정, false는 HTML 형식의 메세지를 사용하지 않음을 나타낸다.
            StringBuilder key = new StringBuilder(); // 인증번호 담을 String key 변수 생성
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                int numIndex = random.nextInt(10);
                key.append(numIndex);
            }
            mimeMessageHelper.setText("인증번호는 " + key + " 입니다.");

            // 이메일 발신자 설정
            mimeMessageHelper.setFrom(new InternetAddress(from));

            // 이메일 보내기
            javaMailSender.send(mimeMessage);
        return Map.of("key", key.toString());

        } catch (MessagingException e) {
            throw  new RuntimeException(e);
        }
    }
}
