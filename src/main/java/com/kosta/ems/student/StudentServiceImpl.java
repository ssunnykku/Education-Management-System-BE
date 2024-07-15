package com.kosta.ems.student;
import com.kosta.ems.attendance.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.data.domain.Page;


import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;
    private final AttendanceService attendanceService;

    @Override
    public Map<String, Collection> getStudentByName(String name) {
        log.info(studentMapper);
        Collection<StudentCourseInfoDTO> data = studentMapper.selectStudentByName("김선희");

        return null;
    }

    // 수강생 정보 검색 결과 데이터 개수 (for 페이지네이션)
    @Override
    public int getStudentsByNameOrCourseNumberAmount(String name, int courseNumber) {
        return studentMapper.findByStudentNumberOrCourseNumberAll(name, courseNumber);
    }

    // 수강생 정보 검색 결과 데이터 불러오기
    @Override
    public List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumberList(String name, int courseNumber, int page, int size) {
        return studentMapper.findByStudentNameOrCourseNumberList(name, courseNumber, ((page*size)-size), size);
    }

    // *0710_수강생 정보 조회
    @Override
    public int getStudentInfoListCnt(int isActive, String name, int courseNumber, String academyLocation) {
        return studentMapper.selectStudentInfoListCnt(isActive, name, courseNumber, academyLocation);
    }
    @Override
    public List<StudentInfoDTO> getStudentInfoList(int isActive, String name, int courseNumber, String academyLocation, int page, int size) {
        return studentMapper.selectStudentInfoList(isActive, name, courseNumber, academyLocation, ((page*size)-size), size);
    }
    @Override
    public List<ArrayList> getStudentInfoList2(int isActive, String name, int courseNumber, String academyLocation, int page, int size) {
        /* 수료 여부 항목
        미수료(교육과정 기간이 지났지만 출석률이 80% 미만), 수료 예정(교육과정 기간 중+출석률 80% 미만),
        수료 대상(교육과정 기간 중+출석률 80% 이상), 수료(교육과정 기간 이후+출석률 80% 이상) */

        // 수료 여부를 보여주기 위해 'AttendnaceService'의 'getAttendanceIntegratedList' _ attendanceRatioFormatted 데이터 필요
        // 'StudentService'의 'selectStudentCourseHistory'도 필요 (n차 수강생인 경우, 가장 최근에 수강하는 교육과정의 수료여부를 보여줄 것임)
        List<ArrayList> item = new ArrayList<>();  // [[studentInfoList, attendanceRatio], []]
        List<StudentInfoDTO> studentInfoList = studentMapper.selectStudentInfoList(isActive, name, courseNumber, academyLocation, ((page*size)-size), size);
        List<ArrayList> attendanceRatioList = attendanceService.getAttendanceIntegratedList(name, courseNumber, "가산", page, size);

        int loopSize = studentInfoList.size() > attendanceRatioList.size() ? attendanceRatioList.size() : studentInfoList.size();
        for(int i=0; i<loopSize; i++) {
            // if(studentInfoList.get(i).getHrdNetId().equals(attendanceRatioList.get(i).get(0)))
            ArrayList tmp = new ArrayList<>(2);
            log.info(">>>>>>>>> attendanceRatioList.get(i).get(1): " + attendanceRatioList.get(i).get(1));
            log.info(">>>>>>>>> attendanceRatioList.get(i).get(0): " + attendanceRatioList.get(i).get(0));
            log.info(">>>>> studentInfoList.get(i): " + studentInfoList.get(i));
            log.info(">>>>> studentInfoList.get(i): " + studentInfoList.get(i).getHrdNetId());
            String dataString = attendanceRatioList.get(i).get(0).toString();

            String[] dataPairs = dataString.split(", ");
            for (String pair : dataPairs) {
                String[] keyValue = pair.split("=");

                if (keyValue[0].equals("hrdNetId")) {
                    String hrdNetId = keyValue[1];
                    if(hrdNetId.equals(studentInfoList.get(i).getHrdNetId())) {
                        tmp.add(studentInfoList.get(i));
                        String ratio = attendanceRatioList.get(i).get(1).toString();
                        tmp.add(ratio);
                    }
                } else {
                    continue;
                }
            }
            item.add(tmp);
        }
        log.info(">>> item: " + item);



        return item;
    }
    // *0710_수강생 정보 조회 (end)

    // *0710_수강생 id로 수강내역 조회
    @Override
    public List<StudentCourseHistoryDTO> getStudentCourseHistory(String studentId) {
        return studentMapper.selectStudentCourseHistory(studentId);
    }

    // * 수강생 등록
    // ** 입력 id가 등록된 hrdNetId인지 확인
    @Override
    public boolean findByHrdNetId(String hrdNetId) {
        int count = studentMapper.findByHrdNetId(hrdNetId);
        if(count == 0) {
            // 수강생 신규 등록 진행
            return false;
        } else {
            // 수강생 기존 정보 불러오기 + DB엔 수강생_강좌에 새로 강좌 등록하기
            return true;
        }
    }

    // * 수강생 등록
    // * 기존 수강생인 경우, 수강생 기본 정보 불러오기
    @Override
    public RegisteredStudentInfoDTO getRegisteredStudentBasicInfo(String hrdNetId) {
        return studentMapper.selectRegisteredStudentBasicInfo(hrdNetId);
    }

    // * 수강생 등록
    // * 현재 진행 중 + 최대 정원까지 수강생 등록 안 되어 있는 교육과정 목록 불러오기
    @Override
    public List<CourseInfoDTO> getOnGoingCourseList(String academyLocation) {
        return studentMapper.selectOnGoingCourseList(academyLocation);
    }

    // * 수강생 등록
    // ** 신규 수강생 등록    
    @Override
    public void setStudentWithCourse(String hrdNetId, String name, String birth, String address, String bank, String account, String phoneNumber, String email, String gender, String managerId, String courseNumber) {
        int year = Integer.parseInt(birth.split("-")[0]);
        int month = Integer.parseInt(birth.split("-")[1]);
        int day = Integer.parseInt(birth.split("-")[2]);
        char g = gender.toCharArray()[0];

        AddStudentBasicInfoDTO dto = AddStudentBasicInfoDTO.builder().hrdNetId(hrdNetId).name(name).birth(LocalDate.of(year, month, day)).address(address).bank(bank).account(account).phoneNumber(phoneNumber).email(email).gender(g).managerId(managerId).courseNumber(Integer.parseInt(courseNumber)).build();

        int result1 = studentMapper.addStudentBasicInfo(dto);
        if(result1 == 0) {
            throw new NoSuchDataException("Fail:: Add new student");
        }

        int result2 = studentMapper.addStudentCourseSeqInfo(dto);
        if(result2 == 0) {
            throw new NoSuchDataException("Fail:: Add student_course");
        }
    }

    // ** students 테이블에 수강생 데이터 등록
    @Override
    public void setStudentBasicInfo(String hrdNetId, String name, String birth, String address, String bank, String account, String phoneNumber, String email, String gender, String managerId, String courseNumber) {
        int year = Integer.parseInt(birth.split("-")[0]);
        int month = Integer.parseInt(birth.split("-")[1]);
        int day = Integer.parseInt(birth.split("-")[2]);
        char g = gender.toCharArray()[0];
        AddStudentBasicInfoDTO dto = AddStudentBasicInfoDTO.builder().hrdNetId(hrdNetId).name(name).birth(LocalDate.of(year, month, day)).address(address).bank(bank).account(account).phoneNumber(phoneNumber).email(email).gender(g).managerId(managerId).courseNumber(Integer.parseInt(courseNumber)).build();
        studentMapper.addStudentBasicInfo(dto);
    }

    // * 수강생 등록
    // ** students_courses 테이블에 수강생 데이터 등록
    @Override
    public void setStudentCourseSeqInfo(String hrdNetId, String courseNumber) {
        AddStudentBasicInfoDTO dto = AddStudentBasicInfoDTO.builder().hrdNetId(hrdNetId).courseNumber(Integer.parseInt(courseNumber)).build();
        studentMapper.addStudentCourseSeqInfo(dto);
    }

    // 수강생 정보 수정
    @Override
    public StudentBasicInfoDTO getRegisteredStudentInfo(String studentId) {
        return studentMapper.selectRegisteredStudentInfo(studentId);
    }
    @Override
    public void updateSelectedStudentInfo(String name, String address, String bank, String account, String phoneNumber, String email, String studentId, int isActiveStatus) {
        boolean tmp = true;
        if(isActiveStatus == 0) {
            tmp = false;
        }
        UpdateSelectedStudentInfoDTO dto = UpdateSelectedStudentInfoDTO.builder().name(name).address(address).bank(bank).account(account).phoneNumber(phoneNumber).email(email).studentId(studentId).isActive(isActiveStatus).build();
        studentMapper.updateSelectedStudentInfo(dto);
    }

    // 수강생 삭제
    @Override
    public void removeSelectedStudent(String studentId) {
        studentMapper.deleteSelectedStudent(studentId);
    }

}