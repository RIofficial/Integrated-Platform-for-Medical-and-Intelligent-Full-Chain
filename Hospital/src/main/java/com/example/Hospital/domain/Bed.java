package com.example.Hospital.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class Bed {
    @Schema(description = "床位ID", example = "1")
    private String bid;
    @Schema(description = "床位状态", example = "空闲")
    private String status;
}
