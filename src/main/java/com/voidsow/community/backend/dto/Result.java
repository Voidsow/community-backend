package com.voidsow.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import static com.voidsow.community.backend.constant.Constant.*;

@Data
@AllArgsConstructor
public class Result {
    int code;
    String message;
    Object data;

    public static Result getSuccess(Object data) {
        return new Result(SUCCESS, "ok", data);
    }

    public static Result resourceNotFound() {
        return new Result(RESOURCE_NOT_FOUND, "resource not found", null);
    }

    public static Result notSupport() {
        return new Result(NOT_SUPPORT, "request not support", null);
    }

    public static Result illegalAccess() {
        return new Result(ILLEGAL, "illegal access", null);
    }
}
