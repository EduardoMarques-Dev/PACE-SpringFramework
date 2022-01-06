package com.agu.gestaoescalabackend.controllers;

import java.util.List;

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

import com.agu.gestaoescalabackend.dto.ProcuradorDTO;
import com.agu.gestaoescalabackend.services.ProcuradorService;

@RestController
@RequestMapping(value = "/procuradores")
public class ProcuradorController {
	
	@Autowired
	private ProcuradorService service;

	@GetMapping
	public ResponseEntity<List<ProcuradorDTO>> pesquisarTodos() {
		List<ProcuradorDTO> list = service.pesquisarTodos();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/status")
	public ResponseEntity<List<ProcuradorDTO>> pesquisarPorStatus(@RequestBody List<String> status) {
		List<ProcuradorDTO> list = service.pesquisarPorStatus(status);
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity<ProcuradorDTO> salvar(@RequestBody ProcuradorDTO procuradorDto) {
		procuradorDto = service.salvar(procuradorDto);
		if (procuradorDto != null)
			return ResponseEntity.ok().body(procuradorDto);
		else
			return ResponseEntity.notFound().build();
	}

	@PutMapping("/{procuradorId}")
	public ResponseEntity<ProcuradorDTO> editar(@PathVariable Long procuradorId,
			@RequestBody ProcuradorDTO procuradorDto) {
		procuradorDto = service.editar(procuradorId, procuradorDto);
		if (procuradorDto != null)
			return ResponseEntity.ok().body(procuradorDto);
		else
			return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{procuradorId}")
	public ResponseEntity<Void> excluir(@PathVariable Long procuradorId) {
		service.excluir(procuradorId);
		return ResponseEntity.noContent().build();
	}
}
