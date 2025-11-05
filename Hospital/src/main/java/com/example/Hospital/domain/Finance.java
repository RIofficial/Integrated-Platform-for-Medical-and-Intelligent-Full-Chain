package com.example.Hospital.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class Finance {
    @Schema(description = "财务ID", example = "1")
    private String fid;
    @Schema(description = "患者ID", example = "1")
    private String rid;
    @Schema(description = "消费账单", example = "1")
    private String bill;
}
