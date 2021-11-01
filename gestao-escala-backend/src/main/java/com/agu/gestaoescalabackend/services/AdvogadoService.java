package com.agu.gestaoescalabackend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agu.gestaoescalabackend.dto.AdvogadoDTO;
import com.agu.gestaoescalabackend.entities.Advogado;
import com.agu.gestaoescalabackend.repositories.AdvogadoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdvogadoService {

	private AdvogadoRepository repository;

	@Transactional(readOnly = true)
	public List<AdvogadoDTO> pesquisarTodos() {
		List<Advogado> list = repository.findAllByOrderByNomeAdvogadoAsc();
		return list.stream().map(x -> new AdvogadoDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public AdvogadoDTO salvar(AdvogadoDTO advogadoDto) {
		Advogado advogado = new Advogado(advogadoDto);

		if (!validarCriacao(advogado))
			return null;

		advogado = repository.save(advogado);
		return new AdvogadoDTO(advogado);
	}

	@Transactional
	public AdvogadoDTO editar(Long advogadoId, AdvogadoDTO advogadoDto) {
		
		if (!repository.existsById(advogadoId))
			return null;

		Advogado advogado = new Advogado(advogadoId, advogadoDto);
		
		advogado = repository.save(advogado);
		return new AdvogadoDTO(advogado);

	}

	@Transactional
	public void excluir(Long advogadoId) {
		if (repository.existsById(advogadoId))
			repository.deleteById(advogadoId);
	}

//////////////////////////////////	 MÉTODOS    ///////////////////////////////////

	private boolean validarCriacao(Advogado advogado) {
		Advogado advogadoExistente = repository.findByNomeAdvogado(advogado.getNomeAdvogado());
		if (advogadoExistente != null && !advogadoExistente.equals(advogado)) {
			return false;
		}
		return true;
	}

}
