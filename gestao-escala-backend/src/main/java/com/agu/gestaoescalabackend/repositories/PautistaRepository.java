package com.agu.gestaoescalabackend.repositories;

import com.agu.gestaoescalabackend.entities.Pautista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautistaRepository extends JpaRepository<Pautista, Long> {

    List<Pautista> findAllByStatusInOrderByNomeAsc(List<String> status);

    List<Pautista> findAllByOrderByIdAsc();

    List<Pautista> findAllByOrderBySaldoPesoAsc();

    Pautista findByNome(String nome);

    List<Pautista> findAllByStatusOrderBySaldoPesoAsc(String status);

    List<Pautista> findAllByGrupoAndStatusOrderBySaldoPesoAsc(String string, String status);

    boolean existsByNome(String nome);
}
