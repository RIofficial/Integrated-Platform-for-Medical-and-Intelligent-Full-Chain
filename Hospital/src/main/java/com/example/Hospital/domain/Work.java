package com.example.Hospital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema
public class Work {
    @Schema(description = "排班ID", example = "1")
    private String pid;
    @Schema(description = "医护姓名", example = "1")
    private String username;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @Schema(description = "开始日期", example = "1")
    private Date date;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @Schema(description = "截止日期", example = "1")
    private Date endtime;
    @Schema(description = "地点", example = "1")
    private String position;
}
