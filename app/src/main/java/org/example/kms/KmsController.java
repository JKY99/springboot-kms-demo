package org.example.kms;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kms")
public class KmsController {

    private final KmsService kmsService;

    KmsController(KmsService kmsService){
        this.kmsService = kmsService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody String plainText){
        return ResponseEntity.ok(kmsService.encrypt(plainText));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody String cipherText){
        return ResponseEntity.ok(kmsService.decrypt(cipherText));
    }
}
