package com.cs2404.tablebuddy.common.response;


public class ApiResponse<T> {
    private final ResultType result;

    private final T data;

    public ApiResponse(ResultType result, T data) {
        this.result = result;
        this.data = data;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data);
    }

    public static <T> ApiResponse<T> error(T data) {
        return new ApiResponse<>(ResultType.ERROR, data);
    }
}
