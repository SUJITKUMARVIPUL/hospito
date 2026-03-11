package com.hospito.dto;

import lombok.*;


@Getter
@Setter
@Builder
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public ApiResponse(int status,String message,T data){
        this.status=status;
        this.message=message;
        this.data=data;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>( 200, message, data);
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>( 201, message, data);
    }

    public static ApiResponse<String> error(String message) {
        return new ApiResponse<>( 201, "error occured",message);
    }
}
