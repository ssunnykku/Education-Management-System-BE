package com.kosta.ems.notification;

import java.util.Collection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
	private final NotificationMapper notificationMapper;

	@Override
	public Collection<NotificationDTO> searchAll(String managerId) {
		return notificationMapper.selectAll(managerId);
	}

	@Override
	public Collection<NotificationDTO> searchByKeyword(String keyword, String managerId) throws NoResultsFoundException {
		if (keyword == null || keyword.isEmpty()) {
	        throw new IllegalArgumentException("검색어를 입력해주세요.");
	    }
		Collection<NotificationDTO> notification = notificationMapper.selectByKeyword(keyword, managerId);
		if (notification.isEmpty()) {
		    throw new NoResultsFoundException("검색 결과가 없습니다. : " + keyword);
		}
	return notification;
	 
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
		return notificationMapper.selectDescription(notificationSeq);
	}
}
