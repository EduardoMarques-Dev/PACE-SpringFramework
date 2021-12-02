package com.agu.gestaoescalabackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agu.gestaoescalabackend.entities.Advogado;

@Repository
public interface AdvogadoRepository extends JpaRepository<Advogado, Long>{
	List<Advogado> findAllByOrderByNomeAdvogadoAsc();
	Advogado findByNomeAdvogado(String nomeAdvogado);
	
}
