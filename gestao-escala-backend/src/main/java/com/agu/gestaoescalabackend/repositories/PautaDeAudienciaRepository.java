package com.agu.gestaoescalabackend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agu.gestaoescalabackend.entities.PautaDeAudiencia;

@Repository
public interface PautaDeAudienciaRepository extends JpaRepository<PautaDeAudiencia, Long> {

	PautaDeAudiencia findByProcesso(String processo);

	PautaDeAudiencia findByProcessoAndTipo(String processo, String tipo);

	List<PautaDeAudiencia> findByData(LocalDate data);
	
	List<PautaDeAudiencia> findByDataAndSalaAndTurno(LocalDate data, String sala, String turno);

	List<PautaDeAudiencia> findByVara(String vara);

	List<PautaDeAudiencia> findAllByMutiraoId(Long mutirao_id);

	Optional<PautaDeAudiencia> findById(Long id);

	List<PautaDeAudiencia> findAllByOrderByIdAsc();
}
