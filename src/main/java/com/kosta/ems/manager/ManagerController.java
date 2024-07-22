package com.kosta.ems.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
    @Value("${security.level}")
    private String SECURITY_LEVEL;
    private final ManagerService managerService;
    // s3 파일 업로드
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.s3.upload-temp}")
    private String tempPath;

    @GetMapping
    public Map currentUser() {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();

        return Map.of("result", managerService.fintByManagerId(managerId));
    }

    @ResponseBody
    @PostMapping("/upload")
    public Map<String, Object> attendanceFileUpload(MultipartFile profileImage) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        String orgFileName = profileImage.getOriginalFilename();
        log.info("🎃 tempPath: " + tempPath);
        String bucketKey = tempPath + "managerProfile/" + orgFileName;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(profileImage.getContentType());
        objectMetadata.setContentLength(profileImage.getSize());

        log.info("🎃 bucketName: " + bucketName);
        log.info("🎃 bucketKey: " + bucketKey);
        log.info("🎃 document.getInputStream(): " + profileImage.getInputStream());
        log.info("🎃 ObjectMetadata - contentType: " + objectMetadata.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, bucketKey, profileImage.getInputStream(), objectMetadata);
        log.info("🎃 putObjectRequest: " + putObjectRequest);
        log.info("🎃🎃 bucketName: " + putObjectRequest.getBucketName());
        log.info("🎃🎃 bucketKey: " + putObjectRequest.getKey());

        amazonS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        log.info(amazonS3Client.getUrl(bucketName, bucketKey).toString().substring(6));
        String fileURL = "http:"+amazonS3Client.getUrl(bucketName, bucketKey).toString().substring(6);

        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();

        managerService.updateProfileImage(managerId, fileURL);
        result.put("data", fileURL);

        return result;
    }

    private ManagerDTO getLoginUser() {
        ManagerDTO loginUser;
        if (SECURITY_LEVEL.equals("OFF")) {
            loginUser = managerService.findByEmployeeNumber("EMP0001");
        } else {
            loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return loginUser;
    }

}

