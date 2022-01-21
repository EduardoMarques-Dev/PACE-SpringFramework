package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Procurador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcuradorRepository extends JpaRepository<Procurador, Long> {

	List<Procurador> findAllByStatusInOrderByNomeProcuradorAsc(List<String> status);

	List<Procurador> findAllByOrderByIdAsc();

	List<Procurador> findAllByOrderBySaldoPesoAsc();

	Procurador findByNomeProcurador(String nomeProcurador);
	
	List<Procurador> findAllByStatusOrderBySaldoPesoAsc(String status);
	
	List<Procurador> findAllByGrupoAndStatusOrderBySaldoPesoAsc(String string, String status);

	boolean existsByNomeProcurador(String nomeProcurador);
}
