package com.agu.gestaoescalabackend.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agu.gestaoescalabackend.dto.AdvogadoDTO;
import com.agu.gestaoescalabackend.services.AdvogadoService;

@RestController
@RequestMapping(value = "/advogados")
public class AdvogadoController {

	@Autowired
	private AdvogadoService service;

	@GetMapping
	public ResponseEntity<List<AdvogadoDTO>> pesquisarTodos() {
		List<AdvogadoDTO> list = service.pesquisarTodos();
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity<AdvogadoDTO> salvar(@Valid @RequestBody AdvogadoDTO advogadoDto) {
		advogadoDto = service.salvar(advogadoDto);
		if (advogadoDto != null)
			return ResponseEntity.ok().body(advogadoDto);
		else
			return ResponseEntity.notFound().build();
	}

	@PutMapping("/{advogadoId}")
	public ResponseEntity<AdvogadoDTO> editar(@PathVariable Long advogadoId, @Valid @RequestBody AdvogadoDTO advogadoDto) {
		advogadoDto = service.editar(advogadoId, advogadoDto);
		if (advogadoDto != null)
			return ResponseEntity.ok().body(advogadoDto);
		else
			return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{advogadoId}")
	public ResponseEntity<Void> excluir(@PathVariable Long advogadoId) {
		service.excluir(advogadoId);
		return ResponseEntity.noContent().build();
	}
}
