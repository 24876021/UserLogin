package com.example.userlogin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result2 {
    private Integer code;
    private String msg;
    private Object data;

    public static Result2 success() {
        return new Result2(200, "success", null);
    }

    public static Result2 success(String msg, Object data) {
        return new Result2(200, msg, data);
    }

    public static Result2 success(String msg) {
        return new Result2(200, msg, null);
    }

    public static Result2 success(Object data) {
        return new Result2(200, "success", data);
    }

    public static Result2 error(String msg) {
        return new Result2(-1, msg, null);
    }

    public static Result2 error(Integer code, String msg) {
        return new Result2(code, msg, null);
    }
}

