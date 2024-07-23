package com.kosta.ems.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Slf4j
class NotificationServiceTest {
	@Autowired
	NotificationService notificationService;

	//@Test
	void testSearchAll() {
		log.info(notificationService.searchAll("d893bf71-2f8f-11ef-b0b2-0206f94be675", 10, 10).toString());
	}


	@Test
	void testSearchByKeyword() {
		assertThat(notificationService.searchByKeyword("gg","d893c34e-2f8f-11ef-b0b2-0206f94be675", 1, 10 ).size()).isEqualTo(0);

	}
	//@Test
	void testAddNotification() {
		NotificationDTO notification=
				NotificationDTO.builder()
				.managerId("d893c34e-2f8f-11ef-b0b2-0206f94be675")
				.title("학생들 출결관리 집중 단속 기간")
				.description("학생들이 부정으로 출결등록을 하고 있다는 목격이 있어 집중 관리 부탁드립니다.")
				.build();
		assertThat(notificationService.addNotification(notification)).isTrue();
	}

	//@Test
	void testDeleteNotificaiotn() {
		assertThat(notificationService.deleteNotificaiotn(194));
	}

	//@Test
	void testUpdateNotification() {
			NotificationDTO notification=
					NotificationDTO.builder()
					.description("더위로 인한 열사병 주의드리며 반바지 착용 권고 드립니다.")
					.notificationSeq(171)
					.build();
			assertThat(notificationService.updateNotification(notification)).isTrue();
		}

	//@Test
	void testGetDescription() {
		log.info(notificationService.getDescription(171).toString());
	}


	//@Test
	void testTotalCount() {
		 log.info(String.valueOf(notificationService.getTotalCount("d893c34e-2f8f-11ef-b0b2-0206f94be675", "gg")));

	}

}
