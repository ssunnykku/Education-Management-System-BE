package com.kosta.ems.notification;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PageRequestDTO {
	@Builder.Default
	private int page = 1;
	
	@Builder.Default
	private int size = 10;

}
