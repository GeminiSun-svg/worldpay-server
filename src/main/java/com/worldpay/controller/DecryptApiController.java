package com.worldpay.controller;

import com.alibaba.fastjson2.JSONObject;
import com.worldpay.service.DecryptController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/card/api/sign")
@Tag(name = "Decrypt")
public class DecryptApiController {

    @Autowired
    private DecryptController decryptService;

    @PostMapping("/encryption")
    @Operation(summary = "数据加密")
    public Map<String, Object> encryption(@RequestBody Map<String, Object> requestBody) {
        return decryptService.encryption(requestBody);
    }

    @PostMapping("/decryption")
    @Operation(summary = "数据解密")
    public JSONObject Decryption(@RequestBody Map<String, Object> requestBody) {
        return decryptService.Decryption(requestBody);
    }
}
