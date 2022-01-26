package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvogadoRepository extends JpaRepository<Advogado, Long>{

	List<Advogado> findAllByOrderByNomeAsc();

	@Modifying
	@Query(
			value = "TRUNCATE TABLE tb_advogado RESTART IDENTITY CASCADE;",
			nativeQuery = true
	)
	void truncateTable();

}
