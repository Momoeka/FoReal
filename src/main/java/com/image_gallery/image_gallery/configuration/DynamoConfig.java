package com.image_gallery.image_gallery.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Configuration
public class DynamoConfig {

    public static final String Service_ENdpoint = "dynamodb.us-east-1.amazonaws.com";
public static final String Region = "YOUR REGION;
    public static final String Accesskey = "YOUR ID";
    public static final String SecretKey = "YOUR SECRET KEY";

    @Bean
    public DynamoDBMapper mapper() {
        return new DynamoDBMapper(amazondynamodbconfig());
    }

    private AmazonDynamoDB amazondynamodbconfig() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(Service_ENdpoint, Region))

                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(Accesskey, SecretKey)))

                .build();

    }

}
