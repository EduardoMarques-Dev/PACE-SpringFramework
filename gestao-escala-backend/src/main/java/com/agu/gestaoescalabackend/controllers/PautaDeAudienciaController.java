package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.services.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/pautas")
public class PautaDeAudienciaController {

	@Autowired
	private PautaService service;

	@GetMapping
	public ResponseEntity<List<PautaDto>> pesquisarTodos() {
		List<PautaDto> list = service.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<PautaDto> pesquisarEspecifico (@PathVariable Long pautaDeAudienciaId) {
		PautaDto pautaDto = service.findById(pautaDeAudienciaId);
		return ResponseEntity.ok(pautaDto);
	}
	
	@PostMapping
	public ResponseEntity<List<PautaDto>> salvar(
			@RequestBody List<PautaDto> PautaDto) {
		List<PautaDto> listaPautaDto = service.save(PautaDto);
		if (listaPautaDto != null)
			return ResponseEntity.ok().body(listaPautaDto);
		else
			return ResponseEntity.notFound().build();
	}

	@PutMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<PautaDto> editar(@PathVariable Long pautaDeAudienciaId,
										   @RequestBody PautaDto pautaDto) {
		pautaDto = service.editar(pautaDeAudienciaId, pautaDto);
		return ResponseEntity.ok().body(pautaDto);
	}

	@DeleteMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<Void> excluir(@PathVariable Long pautaDeAudienciaId) {
		service.excluir(pautaDeAudienciaId);
		return ResponseEntity.noContent().build();
	}

}
