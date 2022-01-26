package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Pauta;
import com.agu.gestaoescalabackend.enums.TipoPauta;
import com.agu.gestaoescalabackend.enums.TurnoPauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

	Pauta findByProcessoAndTipoPauta(String processo, TipoPauta tipoPauta);

	List<Pauta> findByDataAndSalaAndTurnoPauta(LocalDate data, String sala, TurnoPauta turno);

	List<Pauta> findByVara(String vara);

	List<Pauta> findAllByMutiraoId(Long mutirao_id);

	Optional<Pauta> findById(Long id);

	List<Pauta> findAllByOrderByIdAsc();

	@Modifying
	@Query(
			value = "TRUNCATE TABLE tb_pauta RESTART IDENTITY CASCADE;",
			nativeQuery = true
	)
	void truncateTable();
}
