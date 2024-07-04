package com.kosta.ems.notification;






import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	private int notificationSeq;
	private String managerId;
	private String title;
	private String description;
	private Date notificationDate;
	private char isActive;
	private int viewCount;
	private String name;
	private String formattedNotificationDate;
	private boolean	sameWriter;
		
	}


