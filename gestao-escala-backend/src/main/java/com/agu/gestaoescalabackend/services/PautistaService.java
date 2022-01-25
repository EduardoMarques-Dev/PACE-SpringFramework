package com.agu.gestaoescalabackend.services;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.repositories.PautistaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<PautistaDto> findByStatus(List<String> status) {
        return pautistaRepository.findAllByStatusPautistaInOrderByNomeAsc(status)
                .stream()
                .map(Pautista::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PautistaDto save(PautistaDto pautistaDto) {

        Pautista pautista = pautistaDto.toEntity();
        definirSaldo(pautista);
        return pautistaRepository.save(pautista).toDto();
    }

    @Transactional
    public PautistaDto update(Long id, PautistaDto pautistaDto) {

        if (!pautistaRepository.existsById(id))
            return null;
        Pautista pautista = pautistaDto.toEntity().forUpdate(id);
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
