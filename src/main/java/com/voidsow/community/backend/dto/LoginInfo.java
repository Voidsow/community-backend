package com.voidsow.community.backend.dto;

import lombok.Data;

@Data
public class LoginInfo {
    String username;
    String psw;
    String captcha;
    boolean longTerm = false;
}
