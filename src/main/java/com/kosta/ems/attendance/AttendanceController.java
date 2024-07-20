package com.kosta.ems.attendance;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kosta.ems.attendance.dto.RequestAcknowledgeDTO;
import com.kosta.ems.attendance.dto.RequestStudentAttendanceDTO;
import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.kosta.ems.student.NoSuchDataException;
import com.kosta.ems.student.dto.PageResponseDTO;
import com.kosta.ems.student.ResCode;
import com.kosta.ems.student.dto.UpdateDeleteResultDTO;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequestMapping("/attendances")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final ManagerService managerService;

    // s3 파일 업로드
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.s3.upload-temp}")
    private String tempPath;


    @Value("OFF")
    private String SECURITY_LEVEL;

    // [출결] - 수강생 출석 조회 목록 조회
    @PostMapping("/student-list")
    @ResponseBody
    public Map<String, Object> getStudentAttendanceList(@RequestParam(name = "page", required = false, defaultValue = "1") int page, @RequestBody RequestStudentAttendanceDTO dto) {
        Map<String, Object> result = new HashMap<String, Object>();
        int size = 10;
        int totalCount = 0;
        String academyLocation = getAcademyOfLoginUser();

        totalCount = attendanceService.getAttendanceIntegratedListAmount(dto.getName(), dto.getCourseNumber(), academyLocation);
        result.put("attendanceList", attendanceService.getAttendanceIntegratedList(dto.getName(), dto.getCourseNumber(), academyLocation, page, size));
        result.put("amount", totalCount);
        result.put("searchCourseNumber", dto.getCourseNumber());
        result.put("searchStudentName", dto.getName());

        // 페이징 response
        int totalPage = (totalCount / size) + 1;
        int currentPage = 1;
        int prevPage = 0;
        int nextPage = 0;
        if (currentPage > 1 && currentPage < totalPage) {
            prevPage = currentPage - 1;
            nextPage = currentPage + 1;
        } else if (currentPage == totalPage) {
            prevPage = currentPage - 1;
        } else if (currentPage == 1) {
            nextPage = currentPage + 1;
        }

        PageResponseDTO pageInfo = PageResponseDTO.builder().totalCount(totalCount).totalPage(totalPage).currentPage(currentPage).prevPage(prevPage).nextPage(nextPage).build();
        result.put("pageInfo", pageInfo);

        log.info(">> -- pageInfo: " + result.get("pageInfo").toString());

        return result;
    }

    // [출결] - 출결 검색(조건: 날짜, 기수, 수강생명) 데이터 목록 가져오기
    @PostMapping("/search-list")
    public Map<String, Object> getFilteredAttendanceList(@RequestParam(name = "page", required = false, defaultValue = "1") int page, @RequestBody RequestStudentAttendanceDTO dto) {
        Map<String, Object> result = new HashMap<String, Object>();

        int size = 10;

        int courseNumber = dto.getCourseNumber();
        String name = dto.getName();
        int totalCount = 0;
        String academyLocation = getAcademyOfLoginUser();

        totalCount = attendanceService.getAttendanceStatusListAmount(dto.getAttendanceDate(), academyLocation, name, courseNumber);
        result.put("amount", totalCount);
        result.put("attendanceList", attendanceService.getAttendanceStatusList(dto.getAttendanceDate(), academyLocation, name, courseNumber, page, size));

        // 페이징 response
        int totalPage = (totalCount / size) + 1;
        int currentPage = 1;
        int prevPage = 0;
        int nextPage = 0;
        if (currentPage > 1 && currentPage < totalPage) {
            prevPage = currentPage - 1;
            nextPage = currentPage + 1;
        } else if (currentPage == totalPage) {
            prevPage = currentPage - 1;
        } else if (currentPage == 1) {
            nextPage = currentPage + 1;
        }

        PageResponseDTO pageInfo = PageResponseDTO.builder().totalCount(totalCount).totalPage(totalPage).currentPage(currentPage).prevPage(prevPage).nextPage(nextPage).build();
        result.put("pageInfo", pageInfo);
        log.info(">> pageInfo: " + pageInfo.toString());

        return result;
    }

    // [출결] - 선택한 수강생의 출석 상태 수정, 입력
    @PutMapping("/attendance-status")
    public UpdateDeleteResultDTO updateStudentAttendance(@RequestBody List<RequestStudentAttendanceDTO> request) {
        UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();

        try {
            log.info("🚀 request 확인");
            log.info(">> request.length: " + request.size());
            log.info(">> request: " + request.toString());
            attendanceService.updateStudentAttendance(request);
            dto.setCode(ResCode.SUCCESS.value());
        } catch (NoSuchDataException e) {
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: updateStudentAttendance");
        } catch (Exception e) {
            log.error("[StudentController updateStudentAttendance]", e);
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: updateSelectedStudentInfo");
        }
        return dto;
    }

    // [출결 입력] - 특정일의 출결 상태가 등록되지 않은 수강생 목록 데이터 가져오기
    @GetMapping("/no-attendance-list")
    public Map<String, Object> getNoAttendanceStatusList(@RequestParam(name = "attendanceDate") String attendanceDate) {
        Map<String, Object> result = new HashMap<String, Object>();
        String academyLocation = getAcademyOfLoginUser();

        result.put("studentList", attendanceService.getNoAttendanceStatusStudentList(attendanceDate, academyLocation));

        return result;
    }

    @PostMapping("/attendance-status")
    public Map<String, Object> setStudentAttendance(@RequestBody List<RequestStudentAttendanceDTO> request) {
        Map<String, Object> result = new HashMap<String, Object>();
        UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
        String managerId = getManagerIdOfLoginUser();

        try {
            log.info("🚀 request 확인");
            log.info(">> request.length: " + request.size());
            log.info(">> request: " + request.toString());
            for (int i = 0; i < request.size(); i++) {
                attendanceService.setAttendanceStatus(request.get(i).getAttendanceStatus(), request.get(i).getAttendanceDate(), request.get(i).getStudentCourseSeq(), managerId);
            }
            dto.setCode(ResCode.SUCCESS.value());
            dto.setMessage("Success: setStudentAttendance");
            result.put("code", dto.getCode());
            result.put("message", dto.getMessage());
        } catch (NoSuchDataException e) {
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: setStudentAttendance");
            result.put("code", dto.getCode());
            result.put("message", dto.getMessage());
        } catch (Exception e) {
            log.error("[AttendanceController setStudentAttendance]", e);
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: setStudentAttendance");
            result.put("code", dto.getCode());
            result.put("message", dto.getMessage());
        }
        return result;
    }

    // [출석인정 및 증빙서류 설정 모달]
    // 출석인정 항목 데이터 가져오기
    @GetMapping("/acknowledge-category")
    public Map<String, Object> getAcknowledgeCategoryList() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", attendanceService.getAcknowledgeCategoryList(1));
        return result;
    }

    // 출결 증빙 서류 파일 S3 업로드
    @ResponseBody
    @PostMapping("/upload")
    public Map<String, Object> attendanceFileUpload(MultipartFile evidentialDocument) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        String orgFileName = evidentialDocument.getOriginalFilename();
        String bucketKey = tempPath + orgFileName;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(evidentialDocument.getContentType());
        objectMetadata.setContentLength(evidentialDocument.getSize());

        log.info("🎃 bucketName: " + bucketName);
        log.info("🎃 bucketKey: " + bucketKey);
        log.info("🎃 document.getInputStream(): " + evidentialDocument.getInputStream());
        log.info("🎃 ObjectMetadata - contentType: " + objectMetadata.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, bucketKey, evidentialDocument.getInputStream(), objectMetadata);
        log.info("🎃 putObjectRequest: " + putObjectRequest);
        log.info("🎃🎃 bucketName: " + putObjectRequest.getBucketName());
        log.info("🎃🎃 bucketKey: " + putObjectRequest.getKey());

        amazonS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        log.info("출석인정 서류 파일 업로드 완료!!");
        log.info(amazonS3Client.getUrl(bucketName, bucketKey).toString().substring(6));
        String fileURL = "http:" + amazonS3Client.getUrl(bucketName, bucketKey).toString().substring(6);

        result.put("data", fileURL);
        return result;
    }

    // 출석인정 항목*인정일수 적용하여 출결 상태 반영
    @PostMapping("/reflect-acknowledge-documents")
    public Map<String, Object> reflectAcknowledgeAttendanceStatus(@RequestBody RequestAcknowledgeDTO request) {
        Map<String, Object> result = new HashMap<String, Object>();
        UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();

        try {
            log.info("🚀 request 확인");
            log.info(">> request: " + request.toString());
            attendanceService.reflectAcknowledgeAttendanceStatus(request);

            dto.setCode(ResCode.SUCCESS.value());
            dto.setMessage("Success: setStudentAttendance");
            result.put("code", dto.getCode());
            result.put("message", dto.getMessage());
        } catch (NoSuchDataException e) {
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: setStudentAttendance");
            result.put("code", dto.getCode());
            result.put("message", dto.getMessage());
        } catch (Exception e) {
            log.error("[AttendanceController setStudentAttendance]", e);
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: setStudentAttendance");
            result.put("code", dto.getCode());
            result.put("message", dto.getMessage());
        }
        return result;
    }

    @GetMapping("/current-status")
    public ResponseEntity<Map<String, Object>> getTimeByAttendanceDate(@RequestParam LocalDate attendanceDate, @RequestParam int courseNumber) {
        return ResponseEntity.ok(Map.of("result", attendanceService.getTimeByAttendanceDate(attendanceDate, courseNumber)));
    }


    private String getAcademyOfLoginUser() {
        if (SECURITY_LEVEL.equals("OFF")) {
            return "가산";
        }
        ManagerDTO loginUser = getLoginUser();
        return loginUser.getAcademyLocation();
    }

    private String getManagerIdOfLoginUser() {
        if (SECURITY_LEVEL.equals("OFF")) {
            return "3ddf8577-3eaf-11ef-bd30-0206f94be675";
        }
        ManagerDTO loginUser = getLoginUser();
        return loginUser.getManagerId();
    }

    private ManagerDTO getLoginUser() {
        ManagerDTO loginUser;
        if (SECURITY_LEVEL.equals("OFF")) {
            loginUser = managerService.findByEmployeeNumber("EMP0002");
        } else {
            loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return loginUser;
    }
}

