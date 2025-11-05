package com.example.Hospital.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class Drug {
    @Schema(description = "药品ID", example = "1")
    private String did;
    @Schema(description = "药品名称", example = "1")
    private String drugname;
    @Schema(description = "药品数量", example = "1")
    private String stock;
    @Schema(description = "药品价格", example = "1")
    private String price;
}
