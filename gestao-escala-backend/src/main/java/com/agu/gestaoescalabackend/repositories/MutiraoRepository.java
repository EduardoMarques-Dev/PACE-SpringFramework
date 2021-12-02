package com.agu.gestaoescalabackend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agu.gestaoescalabackend.entities.Mutirao;

@Repository
public interface MutiraoRepository extends JpaRepository<Mutirao, Long> {
	List<Mutirao> findAllByOrderByIdAsc();
	Mutirao findByVaraAndDataInicialAndDataFinal(String vara, LocalDate dataInicial, LocalDate dataFinal);
	List<Mutirao> findByVara(String vara);
	boolean  existsByVaraAndDataInicialAndDataFinal(String vara, LocalDate dataInicial, LocalDate dataFinal);
	
}
