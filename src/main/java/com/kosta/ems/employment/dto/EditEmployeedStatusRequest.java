package com.kosta.ems.employment.dto;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditEmployeedStatusRequest {
    @JsonProperty("sCSeq")
    private int sCSeq;
    
    private String company;
}
