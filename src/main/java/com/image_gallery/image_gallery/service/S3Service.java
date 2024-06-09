package com.image_gallery.image_gallery.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;

@Service
public class S3Service {

        private final S3Client s3Client;
        private final String bucketName;

        public S3Service(@Value("${cloud.aws.credentials.access-key}") String accessKey,
                        @Value("${cloud.aws.credentials.secret-key}") String secretKey,
                        @Value("${cloud.aws.region.static}") String region,
                        @Value("${cloud.aws.s3.bucket}") String bucketName) {
                this.bucketName = bucketName;
                AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey,
                                secretKey);
                this.s3Client = S3Client.builder()
                                .region(Region.of(region))
                                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                                .build();
        }

        public void uploadFile(MultipartFile file) throws IOException {
                String key = "images/" + file.getOriginalFilename();
                s3Client.putObject(
                                PutObjectRequest.builder()
                                                .bucket(bucketName)
                                                .key(key)
                                                .build(),
                                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

                // Set ACL to public-read
                s3Client.putObjectAcl(
                                PutObjectAclRequest.builder()
                                                .bucket(bucketName)
                                                .key(key)
                                                .acl("public-read")
                                                .build());
        }

        public void deleteFile(String fileName) {
                String key = "images/" + fileName;
                s3Client.deleteObject(DeleteObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .build());
        }

        public URL getFileUrl(String fileName) {
                String key = "images/" + fileName;
                return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key));
        }

}
