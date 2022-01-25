package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.enums.GrupoPautista;
import com.agu.gestaoescalabackend.enums.StatusPautista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautistaRepository extends JpaRepository<Pautista, Long> {

    Pautista findByNome(String nome);

    boolean existsByNome(String nome);

    List<Pautista> findAllByOrderBySaldoPesoAsc();

    List<Pautista> findAllByStatusOrderBySaldoPesoAsc(StatusPautista statusPautista);

    List<Pautista> findAllByStatusInOrderByNomeAsc(List<String> status);

    List<Pautista> findAllByGrupoAndStatusOrderBySaldoPesoAsc(GrupoPautista grupoPautista, StatusPautista statusPautista);

    @Modifying
    @Query(
            value = "TRUNCATE TABLE tb_pautista RESTART IDENTITY CASCADE;",
            nativeQuery = true
    )
    void truncateTable();

}
