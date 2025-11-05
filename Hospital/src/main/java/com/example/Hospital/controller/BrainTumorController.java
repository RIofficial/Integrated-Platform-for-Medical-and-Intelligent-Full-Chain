package com.example.Hospital.controller;

import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate; // 用于发出 HTTP 请求

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/braintumor")
@Tag(name="脑瘤检测",description="上传图片检测脑肿瘤")
@CrossOrigin(origins = "http://localhost:5173")
public class BrainTumorController {

    private final RestTemplate restTemplate;
    public BrainTumorController() {
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/detect")
    @Operation(summary = "脑瘤检测",description = "根据图片进行脑瘤检测")
    public ResultInfo<String> detectTumor(@Parameter(description = "脑瘤图片",required = true) @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultInfo.fail("请上传图片");
        }

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(body, headers);

            String pythonServiceUrl = "http://localhost:5001/predict";
            ResponseEntity<String> pythonResponse = restTemplate.postForEntity(pythonServiceUrl, requestEntity, String.class);

            if (pythonResponse.getStatusCode().is2xxSuccessful()) {

                String detectionResult = pythonResponse.getBody();
                return ResultInfo.success(detectionResult);
            } else {
                String errorMsg = "Python 服务调用失败，状态码: " + pythonResponse.getStatusCode();
                if (pythonResponse.hasBody()) {
                    errorMsg += ", 响应: " + pythonResponse.getBody();
                }
                System.err.println(errorMsg);
                return ResultInfo.fail(errorMsg);
            }

        } catch (Exception e) {
            System.err.println("调用 Python 推理服务失败: " + e.getMessage());
            e.printStackTrace();
            return ResultInfo.fail("调用推理服务失败: " + e.getMessage());
        }
    }
}
