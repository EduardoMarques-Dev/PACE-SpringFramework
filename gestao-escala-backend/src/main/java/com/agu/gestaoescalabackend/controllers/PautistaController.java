package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.services.PautistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/pautista")
public class PautistaController {
	
	@Autowired
	private PautistaService pautistaService;

	@GetMapping
	public ResponseEntity<List<PautistaDto>> pesquisarTodos() {
		List<PautistaDto> list = pautistaService.pesquisarTodos();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/status")
	public ResponseEntity<List<PautistaDto>> pesquisarPorStatus(@RequestBody List<String> status) {
		List<PautistaDto> list = pautistaService.pesquisarPorStatus(status);
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity<PautistaDto> salvar(@Validated @RequestBody PautistaDto pautistaDto) {
		pautistaDto = pautistaService.salvar(pautistaDto);
		if (pautistaDto != null)
			return ResponseEntity.ok().body(pautistaDto);
		else
			return ResponseEntity.notFound().build();
	}

	@PutMapping("/{procuradorId}")
	public ResponseEntity<PautistaDto> editar(@PathVariable Long procuradorId,
											  @Validated @RequestBody PautistaDto pautistaDto) {
		pautistaDto = pautistaService.editar(procuradorId, pautistaDto);
		if (pautistaDto != null)
			return ResponseEntity.ok().body(pautistaDto);
		else
			return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{procuradorId}")
	public ResponseEntity<Void> excluir(@PathVariable Long procuradorId) {
		pautistaService.excluir(procuradorId);
		return ResponseEntity.noContent().build();
	}
}
