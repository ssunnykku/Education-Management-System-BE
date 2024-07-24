package com.kosta.ems.manager;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.manager.ManagerService;

@SpringBootTest
public class ManagerServiceTest {
    @Autowired
    ManagerService service;

    @Test
    void fintByManagerIdTest() {
        assertThat(service.fintByManagerId("3ddf8681-3eaf-11ef-bd30-0206f94be675").getName()).isEqualTo("김선희");
    }
}
