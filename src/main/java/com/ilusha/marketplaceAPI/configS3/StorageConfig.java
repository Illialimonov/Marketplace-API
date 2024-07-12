package com.ilusha.marketplaceAPI.configS3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${spring.aws.accessKey}")
    private String accessKey;

    @Value("${spring.aws.secretKey}")
    private String accessSecret;

    @Value("${spring.aws.region}")
    private String region;


    @Bean
    public AmazonS3 s3Client(){
        System.out.println(accessKey);
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,accessSecret);
        return AmazonS3ClientBuilder.standard().
                withCredentials(new AWSStaticCredentialsProvider(credentials)).
                withRegion(region).build();

    }

}
