package com.sky.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.sky.properties.AlyProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类
 * 用于将文件上传到阿里云对象存储服务
 */
@Component
public class AlyOSSUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM");
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".pdf", ".doc", ".docx"
    ));
    private static final String PATH_SEPARATOR = "/";

    private final AlyProperties OSSProperties;

    public AlyOSSUtils(AlyProperties OSSProperties) {
        this.OSSProperties = OSSProperties;
    }

    /**
     * 将文件上传到阿里云 OSS 存储
     *
     * @param content          文件内容字节数组
     * @param originalFilename 原始文件名，用于提取文件扩展名
     * @return 上传成功后文件的访问 URL 地址
     * @throws IllegalArgumentException 当参数无效或文件类型不允许时抛出
     * @throws Exception                上传过程中可能抛出的异常
     */
    public String upload(byte[] content, String originalFilename) throws Exception {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("文件内容不能为空");
        }
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        String endpoint = OSSProperties.getEndpoint();
        String bucketName = OSSProperties.getBucketName();
        String region = OSSProperties.getRegion();

        String extension = extractFileExtension(originalFilename);
        validateFileExtension(extension);

        String dir = LocalDate.now().format(DATE_FORMATTER);
        String newFileName = UUID.randomUUID() + extension;
        String objectName = dir + PATH_SEPARATOR + newFileName;

        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        } finally {
            ossClient.shutdown();
        }

        return buildAccessUrl(endpoint, bucketName, objectName);
    }


    /**
     * 提取文件扩展名
     */
    private String extractFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            throw new IllegalArgumentException("无效的文件名：" + filename);
        }
        return filename.substring(lastDotIndex).toLowerCase();
    }

    /**
     * 验证文件扩展名是否允许
     */
    private void validateFileExtension(String extension) {
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("不允许的文件类型：" + extension);
        }
    }

    /**
     * 构建访问 URL
     */
    private String buildAccessUrl(String endpoint, String bucketName, String objectName) {
        int protocolEndIndex = endpoint.indexOf("://");
        if (protocolEndIndex == -1) {
            throw new IllegalArgumentException("无效的 endpoint 格式：" + endpoint);
        }

        String protocol = endpoint.substring(0, protocolEndIndex + 3);
        String domain = endpoint.substring(protocolEndIndex + 3);

        return protocol + bucketName + "." + domain + PATH_SEPARATOR + objectName;
    }

}
