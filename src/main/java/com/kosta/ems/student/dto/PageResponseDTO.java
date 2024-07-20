package com.kosta.ems.student.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponseDTO {
    private int totalCount, prevPage, nextPage, totalPage, currentPage;
}
