package com.example.Hospital.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class Treatment {
    @Schema(description = "诊疗单号", example = "1")
    private String tid;
    @Schema(description = "患者ID", example = "1")
    private String rid;
    @Schema(description = "医护ID", example = "1")
    private String uid;
}
