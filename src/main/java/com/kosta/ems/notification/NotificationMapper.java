package com.kosta.ems.notification;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface NotificationMapper {
	Collection<NotificationDTO> selectAll(String managerId,int limit, int offset);//
	Collection<NotificationDTO> selectByKeyword(@Param("keyword")String keyword,String managerId,int limit,int offset);//
	boolean insertNotification(NotificationDTO notification);
	boolean inActivateNotificaiotn(int notificationSeq);
	boolean updateNotification(NotificationDTO notification);
	NotificationDTO selectDescription(int notificationSeq);  
	Integer getTotalCount(String managerId,@Param("keyword")String keyword);
	Integer updateViewCount(int notificationSeq);
}
