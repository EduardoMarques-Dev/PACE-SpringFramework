package com.agu.gestaoescalabackend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agu.gestaoescalabackend.dto.ProcuradorDTO;
import com.agu.gestaoescalabackend.entities.Procurador;
import com.agu.gestaoescalabackend.repositories.ProcuradorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProcuradorService {

	private ProcuradorRepository repository;

	@Transactional(readOnly = true)
	public List<ProcuradorDTO> pesquisarTodos() {
		List<Procurador> list = repository.findAllByOrderByIdAsc();
		return list.stream().map(x -> new ProcuradorDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ProcuradorDTO> pesquisarPorStatus(List<String> status) {
		List<Procurador> list = repository.findAllByStatusInOrderByNomeProcuradorAsc(status);
		return list.stream().map(x -> new ProcuradorDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public ProcuradorDTO salvar(ProcuradorDTO procuradorDto) {

		Procurador procurador = new Procurador(procuradorDto);

		if (!validarCriacao(procurador))
			return null;

		definirSaldo(procurador);

		procurador = repository.save(procurador);
		return new ProcuradorDTO(procurador);
	}

	@Transactional
	public ProcuradorDTO editar(Long procuradorId, ProcuradorDTO procuradorDto) {

		if (!repository.existsById(procuradorId))
			return null;

		Procurador procurador = new Procurador(procuradorId, procuradorDto);
		procurador = repository.save(procurador);
		return new ProcuradorDTO(procurador);

	}

	@Transactional
	public void excluir(Long procuradorId) {
		if (repository.existsById(procuradorId))
			repository.deleteById(procuradorId);
	}

//////////////////////////////////	  MÉTODOS    ///////////////////////////////////

	private boolean validarCriacao(Procurador procurador) {
		Procurador procuradorExistente = repository.findByNomeProcurador(procurador.getNomeProcurador());
		if (procuradorExistente != null && !procuradorExistente.equals(procurador)) {
			return false;
		}
		return true;
	}

	private void definirSaldo(Procurador procurador) {
		int media = 0;
		List<Procurador> listProcurador = repository.findAll();
		for (int x = 0; x < listProcurador.size(); x++) {
			media += listProcurador.get(x).getSaldo();
		}
		procurador.setSaldo(media);
	}

}
