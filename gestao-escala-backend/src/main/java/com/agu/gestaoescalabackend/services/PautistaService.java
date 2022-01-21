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

    private PautistaRepository repository;

    @Transactional(readOnly = true)
    public List<PautistaDto> pesquisarTodos() {
        List<Pautista> list = repository.findAllByOrderByIdAsc();
        return list.stream().map(x -> new PautistaDto(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PautistaDto> pesquisarPorStatus(List<String> status) {
        List<Pautista> list = repository.findAllByStatusInOrderByNomeAsc(status);
        return list.stream().map(x -> new PautistaDto(x)).collect(Collectors.toList());
    }

    @Transactional
    public PautistaDto salvar(PautistaDto pautistaDto) {

        Pautista pautista = new Pautista(pautistaDto);

        if (!validarCriacao(pautista))
            return null;

        definirSaldo(pautista);

        pautista = repository.save(pautista);
        return new PautistaDto(pautista);
    }

    @Transactional
    public PautistaDto editar(Long procuradorId, PautistaDto pautistaDto) {

        if (!repository.existsById(procuradorId))
            return null;

        Pautista pautista = new Pautista(procuradorId, pautistaDto);
        pautista = repository.save(pautista);
        return new PautistaDto(pautista);

    }

    @Transactional
    public void excluir(Long procuradorId) {
        if (repository.existsById(procuradorId))
            repository.deleteById(procuradorId);
    }

//////////////////////////////////	  MÉTODOS    ///////////////////////////////////

    private boolean validarCriacao(Pautista pautista) {
        Pautista pautistaExistente = repository.findByNome(pautista.getNome());
        if (pautistaExistente != null && !pautistaExistente.equals(pautista)) {
            return false;
        }
        return true;
    }

    private void definirSaldo(Pautista pautista) {
        int media = 0;
        List<Pautista> listPautista = repository.findAll();
        for (int x = 0; x < listPautista.size(); x++) {
            media += listPautista.get(x).getSaldo();
        }
        pautista.setSaldo(media);
    }

}
