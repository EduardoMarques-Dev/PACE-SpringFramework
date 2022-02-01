package com.agu.gestaoescalabackend.services;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.enums.StatusPautista;
import com.agu.gestaoescalabackend.repositories.PautistaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PautistaService {

    private PautistaRepository pautistaRepository;

    @Transactional(readOnly = true)
    public List<PautistaDto> findAll() {
        return pautistaRepository.findAll()
                .stream()
                .map(Pautista::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PautistaDto> findByStatus(List<StatusPautista> status) {
        return pautistaRepository.findAllByStatusPautistaInOrderByNomeAsc(status)
                .stream()
                .map(Pautista::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PautistaDto> findAllAvailablePautistas(LocalDate data) {

        List<StatusPautista> statusPautistas = new ArrayList<>();
        statusPautistas.add(StatusPautista.ATIVO);
        statusPautistas.add(StatusPautista.INATIVO);

        // Busca todos os pautistas ativos no banco
        List<Pautista> pautistaList = pautistaRepository.findAllByStatusPautistaInOrderByNomeAsc(statusPautistas);
        List<Pautista> pautistaRetorno = new ArrayList<>();

        for (Pautista pautista : pautistaList){

            if (pautista.estaDisponivel(data))
                pautistaRetorno.add(pautista);
        }

        return pautistaRetorno
                .stream()
                .map(Pautista::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PautistaDto save(PautistaDto pautistaDto) {

        Pautista pautista = pautistaDto.toEntity().forSave();
        definirSaldo(pautista);
        return pautistaRepository.save(pautista).toDto();
    }

    @Transactional
    public PautistaDto update(Long id, PautistaDto pautistaDto) {


        Optional<Pautista> pautistaOptional = pautistaRepository.findById(id);
        if (pautistaOptional.isEmpty())
            return null;
        Pautista pautista = pautistaOptional.get().forUpdate(pautistaDto);
        return pautistaRepository.save(pautista).toDto();

    }

    @Transactional
    public void delete(Long procuradorId) {
        if (pautistaRepository.existsById(procuradorId))
            pautistaRepository.deleteById(procuradorId);
    }

    /*------------------------------------------------
    METODOS DE NEGÓCIO
    ------------------------------------------------*/

    private void definirSaldo(Pautista pautista) {
        int media = 0;
        List<Pautista> pautistas = pautistaRepository.findAll();
        for (Pautista pautistaFor : pautistas) {
            media += pautistaFor.getSaldo();
        }
        pautista.setSaldo(media);
    }

}
