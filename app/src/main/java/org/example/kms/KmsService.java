package org.example.kms;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Base64;

@Service
public class KmsService {

    private final AWSKMS kmsClient;

    @Value("${aws.kms.key-arn}")
    private String keyArn;

    public KmsService(AWSKMS kmsClient) {
        this.kmsClient = kmsClient;
    }

    public String encrypt(String plainText) {
        EncryptRequest req = new EncryptRequest()
                .withKeyId(keyArn)
                .withPlaintext(ByteBuffer.wrap(plainText.getBytes()));
        ByteBuffer cipherBlob = kmsClient.encrypt(req).getCiphertextBlob();
        String cipherText = Base64.getEncoder().encodeToString(cipherBlob.array());
        return cipherText;
    }

    public String decrypt(String cipherText){
        ByteBuffer cipherBlob = ByteBuffer.wrap(Base64.getDecoder().decode(cipherText));
        DecryptRequest req = new DecryptRequest()
                .withCiphertextBlob(cipherBlob);
        String plainText = new String(kmsClient.decrypt(req).getPlaintext().array());
        return plainText;
    }

}
