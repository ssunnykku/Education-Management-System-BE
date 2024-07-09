package com.kosta.ems.employment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmploymentRepo extends JpaRepository<EmploymentDTO, Integer>{
    Optional<EmploymentDTO> findBySeq(int employmentSeq);
		
}
