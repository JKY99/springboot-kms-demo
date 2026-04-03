package org.example.config;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KmsConfig {
    @Bean
    public AWSKMS kmsClient(){
        return AWSKMSClientBuilder.standard()
                .withRegion("ap-northeast-2")
                .build();
    }
}
