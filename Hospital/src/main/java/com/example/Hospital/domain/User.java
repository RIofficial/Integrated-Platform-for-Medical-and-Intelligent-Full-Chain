package com.example.Hospital.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class User {
    @Schema(description = "医护ID", example = "1")
    private String uid;
    @Schema(description = "医护姓名", example = "1")
    private String username;
    @Schema(description = "账号密码", example = "1")
    private String password;
    @Schema(description = "职责", example = "1")
    private String role;
}