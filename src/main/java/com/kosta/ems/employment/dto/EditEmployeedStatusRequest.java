package com.kosta.ems.employment.dto;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditEmployeedStatusRequest {

    private int employmentSeq;
    private String company;
}
