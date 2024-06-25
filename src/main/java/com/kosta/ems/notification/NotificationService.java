package com.kosta.ems.notification;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
public interface NotificationService {

	Collection<NotificationDTO> selectAll(String managerId);
	Collection<NotificationDTO> findByKeyword(String keyword,String managerId);//
	boolean insertNotification(NotificationDTO notification);
	boolean inActivateNotificaiotn(int notificationSeq);
	boolean updateNotification(NotificationDTO notification);
	NotificationDTO getDescription(int notificationSeq);
	
	
}
