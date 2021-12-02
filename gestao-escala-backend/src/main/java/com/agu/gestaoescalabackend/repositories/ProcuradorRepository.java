package com.agu.gestaoescalabackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agu.gestaoescalabackend.entities.Procurador;

@Repository
public interface ProcuradorRepository extends JpaRepository<Procurador, Long> {
	List<Procurador> findAllByOrderByIdAsc();

	List<Procurador> findAllByOrderBySaldoPesoAsc();
	List<Procurador> findAllByOrderBySaldoPesoDesc();

	boolean existsByNomeProcurador(String nomeProcurador);

	Procurador findByNomeProcurador(String nomeProcurador);

	//List<Procurador> findAllByGrupoByOrderBySaldoAsc(String grupo);
	List<Procurador> findAllByGrupo(String Grupo);
	
	List<Procurador> findAllByStatusOrderBySaldoPesoAsc(String status);

	List<Procurador> findAllByGrupoOrderBySaldoPesoAsc(String grupo);
	
	List<Procurador> findAllByGrupoAndStatusOrderBySaldoPesoAsc(String string, String status);
}
