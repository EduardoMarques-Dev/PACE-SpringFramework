package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Pauta;
import com.agu.gestaoescalabackend.enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

	Pauta findByProcessoAndTipo(String processo, Tipo tipo);

	List<Pauta> findByDataAndSalaAndTurno(LocalDate data, String sala, String turno);

	List<Pauta> findByVara(String vara);

	List<Pauta> findAllByMutiraoId(Long mutirao_id);

	Optional<Pauta> findById(Long id);

	List<Pauta> findAllByOrderByIdAsc();

	@Modifying
	@Query(
			value = "TRUNCATE TABLE tb_pautas RESTART IDENTITY CASCADE;",
			nativeQuery = true
	)
	void truncateTable();
}
