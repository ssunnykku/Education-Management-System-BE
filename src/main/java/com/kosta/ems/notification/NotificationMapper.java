package com.kosta.ems.notification;

import java.util.Collection;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface NotificationMapper {
	Collection<NotificationDTO> selectAll(String managerId);//
	Collection<NotificationDTO> selectByKeyword(@Param("keyword")String keyword,String managerId);//
	boolean insertNotification(NotificationDTO notification);
	boolean inActivateNotificaiotn(int notificationSeq);
	boolean updateNotification(NotificationDTO notification);
	NotificationDTO selectDescription(int notificationSeq);  
}
