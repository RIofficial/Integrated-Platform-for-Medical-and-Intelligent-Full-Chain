package com.example.Hospital.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class Patient {
    @Schema(description = "患者ID", example = "1")
    private String rid;
    @Schema(description = "患者姓名", example = "1")
    private String username;
    @Schema(description = "患者年龄", example = "1")
    private String age;
    @Schema(description = "患者疾病", example = "1")
    private String disaster;
}
