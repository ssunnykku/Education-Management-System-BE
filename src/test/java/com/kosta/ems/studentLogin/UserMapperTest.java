package com.kosta.ems.studentLogin;

import com.kosta.ems.student.dto.StudentCourseInfoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper studentLoginMapper;

    @Test
    void selectStudentListByCourseSeqTest() {

        List<StudentCourseInfoDTO> list = studentLoginMapper.selectStudentListBycourseSeq(19);

        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getCourseNumber()).isEqualTo(277);
        }

    }

    @Test
    void getRefreshToken() {
    }


}