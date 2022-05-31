package com.voidsow.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import static com.voidsow.community.backend.constant.Constant.SUCCESS;

@Data
@AllArgsConstructor
public class Result {
    int code;
    String message;
    Object data;

    public static Result getSuccess(Object data) {
        return new Result(SUCCESS, "ok", data);
    }
}