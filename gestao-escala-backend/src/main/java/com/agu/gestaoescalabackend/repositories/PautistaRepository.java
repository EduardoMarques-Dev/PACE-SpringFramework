package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.enums.Grupo;
import com.agu.gestaoescalabackend.enums.Status;
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

    List<Pautista> findAllByStatusOrderBySaldoPesoAsc(Status status);

    List<Pautista> findAllByStatusInOrderByNomeAsc(List<String> status);

    List<Pautista> findAllByGrupoAndStatusOrderBySaldoPesoAsc(Grupo grupo, Status status);

    @Modifying
    @Query(
            value = "TRUNCATE TABLE tb_pautistas RESTART IDENTITY CASCADE;",
            nativeQuery = true
    )
    void truncateTable();

}
