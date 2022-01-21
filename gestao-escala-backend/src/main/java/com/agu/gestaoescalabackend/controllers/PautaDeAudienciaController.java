package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.PautaDeAudienciaDTO;
import com.agu.gestaoescalabackend.services.PautaDeAudienciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/pautas")
public class PautaDeAudienciaController {

	@Autowired
	private PautaDeAudienciaService service;

	@GetMapping
	public ResponseEntity<List<PautaDeAudienciaDTO>> pesquisarTodos() {
		List<PautaDeAudienciaDTO> list = service.pesquisarTodos();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<PautaDeAudienciaDTO> pesquisarEspecifico (@PathVariable Long pautaDeAudienciaId) {
		PautaDeAudienciaDTO pautaDto = service.pesquisarEspecifico(pautaDeAudienciaId);
		return ResponseEntity.ok(pautaDto);
	}
	
	@PostMapping
	public ResponseEntity<List<PautaDeAudienciaDTO>> salvar(
			@RequestBody List<PautaDeAudienciaDTO> PautaDeAudienciaDTO) {
		List<PautaDeAudienciaDTO> listaPautaDto = service.salvar(PautaDeAudienciaDTO);
		if (listaPautaDto != null)
			return ResponseEntity.ok().body(listaPautaDto);
		else
			return ResponseEntity.notFound().build();
	}

	@PutMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<PautaDeAudienciaDTO> editar(@PathVariable Long pautaDeAudienciaId,
			@RequestBody PautaDeAudienciaDTO pautaDeAudienciaDto) {
		pautaDeAudienciaDto = service.editar(pautaDeAudienciaId, pautaDeAudienciaDto);
		return ResponseEntity.ok().body(pautaDeAudienciaDto);
	}

	@DeleteMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<Void> excluir(@PathVariable Long pautaDeAudienciaId) {
		service.excluir(pautaDeAudienciaId);
		return ResponseEntity.noContent().build();
	}

}
