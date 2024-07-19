package com.kosta.ems.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kosta.ems.student.ResCode;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateDeleteResultDTO {
	private int code = ResCode.SUCCESS.value();
	private String message;
}
