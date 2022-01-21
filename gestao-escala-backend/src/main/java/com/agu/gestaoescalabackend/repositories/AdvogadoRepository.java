package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvogadoRepository extends JpaRepository<Advogado, Long>{
	List<Advogado> findAllByOrderByNomeAdvogadoAsc();
	Advogado findByNomeAdvogado(String nomeAdvogado);
	
}
