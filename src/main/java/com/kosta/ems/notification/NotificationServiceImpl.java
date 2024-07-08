package com.kosta.ems.notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.Notification;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{
	private final NotificationMapper notificationMapper;

	@Override
	public Collection<NotificationDTO> searchAll(String managerId, int page, int size) {

		int limit = size;
		int offset = size * (page - 1);

		Collection<NotificationDTO> notifications = notificationMapper.selectAll(managerId, limit, offset);

		// 새로운 NotificationDTO 객체 목록 생성
		List<NotificationDTO> dtos = new ArrayList<>();

		for (NotificationDTO notification : notifications) {
			// SimpleDateFormat 객체를 사용하여 원하는 형식 문자열 생성
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = formatter.format(notification.getNotificationDate());

			// NotificationDTO 객체 생성 및 데이터 설정
			NotificationDTO dto = NotificationDTO.builder()
					.notificationSeq(notification.getNotificationSeq())
					.managerId(notification.getManagerId())
					.title(notification.getTitle())
					.description(notification.getDescription())
					.formattedNotificationDate(formattedDate)
					.isActive(notification.getIsActive())
					.viewCount(notification.getViewCount())
					.name(notification.getName())
					.build();
			// NotificationDTO 목록에 추가
			dtos.add(dto);
		}
		return dtos;
	}


	@Override
	public Collection<NotificationDTO> searchByKeyword(String keyword, String managerId,int page,int size) {
		int limit = size;
		int offset = size * (page - 1);


		Collection<NotificationDTO> notifications = notificationMapper.selectByKeyword(keyword, managerId, limit, offset);

		// 새로운 NotificationDTO 객체 목록 생성
		List<NotificationDTO> dtos = new ArrayList<>();

		for (NotificationDTO notification : notifications) {
			// SimpleDateFormat 객체를 사용하여 원하는 형식 문자열 생성
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = formatter.format(notification.getNotificationDate());

			// NotificationDTO 객체 생성 및 데이터 설정
			NotificationDTO dto = NotificationDTO.builder()
					.notificationSeq(notification.getNotificationSeq())
					.managerId(notification.getManagerId())
					.title(notification.getTitle())
					.description(notification.getDescription())
					.formattedNotificationDate(formattedDate)
					.isActive(notification.getIsActive())
					.viewCount(notification.getViewCount())
					.name(notification.getName())
					.build();
			// NotificationDTO 목록에 추가
			dtos.add(dto);

		}

		return dtos;
	}

	@Override
	public boolean addNotification(NotificationDTO notification) {
		return notificationMapper.insertNotification(notification);
	}

	@Override
	public boolean deleteNotificaiotn(int notificationSeq) {
		return notificationMapper.inActivateNotificaiotn(notificationSeq);
	}

	@Override
	public boolean updateNotification(NotificationDTO notification) {
		return notificationMapper.updateNotification(notification);
	}

	@Override
	public NotificationDTO getDescription(int notificationSeq) {
		int viewCount=0;
		NotificationDTO dto= notificationMapper.selectDescription(notificationSeq);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(dto.getNotificationDate());
		dto.setFormattedNotificationDate(formattedDate);
		viewCount=notificationMapper.updateViewCount(notificationSeq);
		dto.setViewCount(viewCount);
		return dto;

	}





	@Override
	public Integer getTotalCount(String managerId,String keyword) {
		Integer result=notificationMapper.getTotalCount(managerId, keyword);
		if(result==null) {
			result=0;
		}
		return result;
	}



}
