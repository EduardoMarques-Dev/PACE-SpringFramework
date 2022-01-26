package com.agu.gestaoescalabackend.services;

import com.agu.gestaoescalabackend.dto.AdvogadoDto;
import com.agu.gestaoescalabackend.entities.Advogado;
import com.agu.gestaoescalabackend.repositories.AdvogadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdvogadoService {

	private AdvogadoRepository advogadoRepository;

	@Transactional(readOnly = true)
	public List<AdvogadoDto> findAll() {

		return advogadoRepository.findAllByOrderByNomeAsc()
				.stream()
				.map(Advogado::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public AdvogadoDto save(AdvogadoDto advogadoDto) {

		Advogado advogado = advogadoDto.toEntity();
		return advogadoRepository.save(advogado).toDto();
	}

	@Transactional
	public AdvogadoDto update(Long id, AdvogadoDto advogadoDto) {

		if (!advogadoRepository.existsById(id))
			return null;

		Advogado advogado = advogadoDto.toEntity().forUpdate(id);
		return advogadoRepository.save(advogado).toDto();
	}

	@Transactional
	public void delete(Long id) {
		if (advogadoRepository.existsById(id))
			advogadoRepository.deleteById(id);
	}

}
