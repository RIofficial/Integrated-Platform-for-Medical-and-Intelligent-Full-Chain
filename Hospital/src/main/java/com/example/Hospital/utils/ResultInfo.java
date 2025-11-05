package com.example.Hospital.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultInfo<T> {
    private Integer status;
    private String errorMsg;
    private T data;

    public static <T> ResultInfo<T> success(T data) {
        return new ResultInfo<>(200, null, data);
    }

    public static <T> ResultInfo<T> fail(String errorMsg) {
        return new ResultInfo<>(500, errorMsg, null);
    }

    public static <T> ResultInfo<T> fail(T data, String errorMsg) {
        return new ResultInfo<>(500, errorMsg, data);
    }
}
