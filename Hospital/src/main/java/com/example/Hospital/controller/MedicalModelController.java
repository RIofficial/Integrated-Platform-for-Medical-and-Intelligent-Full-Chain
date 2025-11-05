package com.example.Hospital.controller;

import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

@RestController
@RequestMapping("/medicalmodel")
@Tag(name="大模型问诊",description="通过ollama进行GPT-3问诊")
@CrossOrigin(origins = "http://localhost:5173")
public class MedicalModelController {

    @PostMapping("/chat")
    @Operation(summary = "对话问诊",description = "通过对话进行问诊")
    public ResultInfo<String> chatWithMedicalModel(@Parameter(description = "对话",required = true)  @RequestBody MedicalChatRequest request) {
        String prompt = request.getPrompt();
        if (prompt == null || prompt.trim().isEmpty()) {
            return ResultInfo.fail("Prompt 不能为空");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ollama", "run", "gemma:2b");
            Process process = processBuilder.start();

            OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(prompt + "\n");
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder ollamaResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                ollamaResponse.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return ResultInfo.success(ollamaResponse.toString().trim());
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                System.err.println("Ollama 进程退出码: " + exitCode);
                System.err.println("Ollama 错误输出: " + errorOutput.toString());
                return ResultInfo.fail("与大模型交互失败，Ollama 进程退出码: " + exitCode + ", 错误信息: " + errorOutput.toString().trim());
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("调用 Ollama 失败: " + e.getMessage());
            e.printStackTrace();
            return ResultInfo.fail("调用大模型服务失败: " + e.getMessage());
        }
    }

    public static class MedicalChatRequest {
        private String prompt;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
    }
}
