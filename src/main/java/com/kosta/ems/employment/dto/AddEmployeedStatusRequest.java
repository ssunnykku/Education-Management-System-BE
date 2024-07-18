package com.kosta.ems.employment.dto;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddEmployeedStatusRequest {
    private int sCSeq;
    private String company;
}
