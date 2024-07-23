package com.kosta.ems.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Slf4j
class NotificationMapperTest {
	@Autowired
	NotificationMapper notificationMapper;
	//@Test
	void testSelectAll() {
		log.info(notificationMapper.selectAll("d893c29b-2f8f-11ef-b0b2-0206f94be675",10,0).toString());
		assertThat(notificationMapper.selectAll("d893c29b-2f8f-11ef-b0b2-0206f94be675",10,0).size()).isEqualTo(10);
	}
	@Test
	void testFindByKeyword() {
		log.info(notificationMapper.selectByKeyword("2025","d893c29b-2f8f-11ef-b0b2-0206f94be675", 10, 0 ).toString());
	}

	@Test
	void testInsertNotification() {
		NotificationDTO notification=
				NotificationDTO.builder()
				.managerId("3ddf873c-3eaf-11ef-bd30-0206f94be675")
				.title("더위로 인한 전사 직원 복장안내")
				.description("무더위로 인해 8월말까지 7부 반바지까지 허용합니다.")
				.build();
		assertThat(notificationMapper.insertNotification(notification)).isTrue();
		//		log.info(notificationMapper.insertNotification("d893c34e-2f8f-11ef-b0b2-0206f94be675", "여름휴가 안내", "2024년 8월부터 전사적인 휴가권고입니다. 자세한것은 HR 안내 시스템을 확인하세요."));
	}

	//@Test
	void testInActivateNotificaiotn() {
		assertThat(notificationMapper.inActivateNotificaiotn(171)).isTrue();
	}

	//@Transactional
	//@Test
	void testUpdateNotification() {
		NotificationDTO notification=
				NotificationDTO.builder()
				//.title("무더위로 인한 전원 반바지 허용 안내")
				.description("무더위로 인해 9월말까지 7부 반바지까지 허용합니다.")
				.notificationSeq(171)
				//.managerId("d893c34e-2f8f-11ef-b0b2-0206f94be675")
				.build();
		assertThat(notificationMapper.updateNotification(notification)).isTrue();
	}

	//@Test
	void testGetDescription() {
		log.info(notificationMapper.selectDescription(160).toString());
	}
	//@Test
	void testGetTotalCount() {
		 log.info(String.valueOf(notificationMapper.getTotalCount("d893c34e-2f8f-11ef-b0b2-0206f94be675", null)));
	}
}
