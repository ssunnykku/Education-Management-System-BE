package com.kosta.ems.student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping({"/student", "/api"})
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    // [수강생 정보] - 수강생 정보 조회
    @PostMapping("/student-list")
    public Map<String, Object> getStudentsByNameOrCourseNumber(@RequestParam(name = "page", required = false, defaultValue = "1") int page, @RequestBody AddStudentBasicInfoDTO dto) {
        Map<String, Object> result = new HashMap<String, Object>();
        int size = 10;
        int courseNumber = dto.getCourseNumber();
        String name = dto.getName().equals("") ? "" : dto.getName();
        int isActive = 1;  // 임시

        int totalCount = studentService.getStudentInfoListCnt(isActive, name, courseNumber);
        result.put("amount", totalCount);
        result.put("studentList", studentService.getStudentInfoList(isActive, name, courseNumber, page, size));
        log.info("☄️result.studentList 1 :" + studentService.getStudentsByNameOrCourseNumberList(name, courseNumber, page, size).toString());

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

        log.info("☄️result.amount " + totalCount);
        log.info("☄️result.studentList " + studentService.getStudentsByNameOrCourseNumberList(name, courseNumber, page, size).toString());
        log.info("☄️result.pageInfo " + pageInfo.toString());

        return result;
    }

    // *0710 선택 수강생의 수강내역 조회
    @PostMapping("/student-course-history")
    public Map<String, Object> getStudentCourseHistory(@RequestBody StudentInfoDTO request) {
        Map<String, Object> result = new HashMap<String, Object>();
		/*
		log.info("🙃 request.studentId(): " + request.getStudentId());
		log.info("🙃 request.studentId() substring: " + request.getStudentId().substring(0, request.getStudentId().length()-1));
		result.put("studentCourseHistory", studentService.getStudentCourseHistory(request.getStudentId().substring(0, request.getStudentId().length()-1)));
		log.info("🙃 studentCourseHistory"+studentService.getStudentCourseHistory(request.getStudentId().substring(0, request.getStudentId().length()-1)).toString());
		*/

        log.info("🙃 request.studentId(): " + request.getStudentId());
        result.put("studentCourseHistory", studentService.getStudentCourseHistory(request.getStudentId()));
        log.info("🙃 studentCourseHistory" + studentService.getStudentCourseHistory(request.getStudentId()));
        return result;
    }

    // [수강생 정보] - 수강생 등록
    // 1. 등록된 hrdNetId인지 확인 _POSTMAN 확인 완료 -- 비동기
    @PostMapping("/valid-id")
    public Map<String, Object> findByHrdNetId(@RequestBody AddStudentBasicInfoDTO request) {
        Map<String, Object> result = new HashMap<String, Object>();
        String hrdNetId = request.getHrdNetId();

        boolean check = studentService.findByHrdNetId(request.getHrdNetId());
        // check: true - 등록 이력 있는 수강생, false - 신규 수강생 등록

        if (check == false) {
            result.put("result", String.valueOf(check));
        } else {
            // 등록된 수강생 기본 정보 불러오기
            result.put("result", String.valueOf(check));
            RegisteredStudentInfoDTO dto = studentService.getRegisteredStudentBasicInfo(hrdNetId);
            result.put("data", dto);
        }
        return result;
    }

    // [수강생 정보] - 수강생 등록
    // 2. 현재 진행 중인 수강신청 가능한 교육과정 목록 불러오기
    @GetMapping("/on-going-courses")
    // spring security 적용 후에는 principal 로 로그인한 매니저의 교육장을 받아오는 걸로 변경해야함.
    public Map<String, Object> getOnGoingCourseList() {
        Map<String, Object> result = new HashMap<String, Object>();
        String academyLocation = "가산";  // spring security 적용 전, 임시 코드
        result.put("courseList", studentService.getOnGoingCourseList(academyLocation));
        return result;
    }

    // [수강생 정보] - 수강생 등록
    // 3-1. 신규 수강생 등록
    @PostMapping()
    public UpdateDeleteResultDTO setStudentWithCourse(@RequestBody RequestAddStudentBasicInfoDTO request) {
        UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
        // @principal 적용 후에는 String managerId 제거하고 @principal 내용으로 교체할 것!
        String managerId = "e84dea58-3784-11ef-b0b2-0206f94be675";  // Name: 테스트용, pw: 1234, 교육장: 가산
        try {
            studentService.setStudentWithCourse(request.getHrdNetId(), request.getName(), request.getBirth(), request.getAddress(), request.getBank(), request.getAccount(), request.getPhoneNumber(), request.getEmail(), request.getGender(), managerId, request.getCourseNumber());
        } catch (NoSuchDataException e) {
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: setStudentWithCourse");
        } catch (Exception e) {
            log.error("[StudentController addStudentWithCourse]", e);
        }
        return dto;
    }

    // [수강생 정보] - 수강생 등록
    // 3-2. 기존 수강생의 과정 수강 신규 등록
    @PostMapping("/new-course")
    public UpdateDeleteResultDTO setRegisteredStudentWithNewCourse(@RequestBody RequestAddStudentBasicInfoDTO request) {
        UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
        try {
            studentService.setStudentCourseSeqInfo(request.getHrdNetId(), request.getCourseNumber());
        } catch (NoSuchDataException e) {
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: setRegisteredStudentWithNewCourse");
        } catch (Exception e) {
            log.error("[StudentController addStudentWithCourse]", e);
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: setRegisteredStudentWithNewCourse");
        }
        return dto;
    }

    // [수강생 정보] - 수강생 정보 수정
    // 1. 선택한 수강생의 등록된 정보 불러오기
    // @Controller에서 작업

    // 2. 페이지 양식에서 작성한 내용으로 수강생 정보 수정하기
    @PutMapping()
    public UpdateDeleteResultDTO updateSelectedStudentInfo(@RequestBody UpdateSelectedStudentInfoDTO request) {
        UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
        log.info("🧰 수강생 정보 수정: ");
        log.info("🧰 requestDTO: " + request.toString());
        try {
            studentService.updateSelectedStudentInfo(request.getName(), request.getAddress(), request.getBank(), request.getAccount(), request.getPhoneNumber(), request.getEmail(), request.getStudentId());
        } catch (NoSuchDataException e) {
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: updateSelectedStudentInfo");
        } catch (Exception e) {
            log.error("[StudentController updateSelectedStudentInfo]", e);
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: updateSelectedStudentInfo");
        }
        return dto;
    }

    // [수강생 정보] - 수강생 삭제
    @PatchMapping("/active-status")
    public UpdateDeleteResultDTO deleteSelectedStudent(@RequestBody UpdateSelectedStudentInfoDTO request) {
        UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
        try {
            studentService.removeSelectedStudent(request.getStudentId());
        } catch (NoSuchDataException e) {
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: updateSelectedStudentInfo");
        } catch (Exception e) {
            log.error("[StudentController updateSelectedStudentInfo]", e);
            dto.setCode(ResCode.FAIL.value());
            dto.setMessage("Fail: updateSelectedStudentInfo");
        }
        return dto;
    }
}