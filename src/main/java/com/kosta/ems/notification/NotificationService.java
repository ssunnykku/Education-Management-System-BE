package com.kosta.ems.notification;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.kosta.ems.scholarship.ScholarshipTargetListReqDTO;

import lombok.RequiredArgsConstructor;

@Service
public interface NotificationService {

	Collection<NotificationDTO> searchAll(String managerId);
	Collection<NotificationDTO> searchByKeyword(String keyword,String managerId) throws NoResultsFoundException;
	boolean addNotification(NotificationDTO notification);
	boolean deleteNotificaiotn(int notificationSeq);
	boolean updateNotification(NotificationDTO notification);
	NotificationDTO getDescription(int notificationSeq);
	int getCountNotificationList(NotificationDTO notification);
	
	
}
