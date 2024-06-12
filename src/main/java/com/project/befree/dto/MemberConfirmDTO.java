package com.project.befree.dto;

import lombok.Data;

@Data
public class MemberConfirmDTO {
    private String password;
    private String result;
    private String accessToken;
    private String refreshToken;
}
