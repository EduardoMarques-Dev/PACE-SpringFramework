package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Pautista;
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

    List<Pautista> findAllByStatusInOrderByNomeAsc(List<String> status);

    List<Pautista> findAllByStatusOrderBySaldoPesoAsc(String status);

    List<Pautista> findAllByGrupoAndStatusOrderBySaldoPesoAsc(String string, String status);
    @Modifying
    @Query(
            value = "TRUNCATE TABLE tb_pautistas RESTART IDENTITY CASCADE;",
            nativeQuery = true
    )
    void truncateTable();

}
