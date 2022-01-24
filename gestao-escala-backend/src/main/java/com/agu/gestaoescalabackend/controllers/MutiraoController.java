package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.MutiraoDTO;
import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.entities.Pauta;
import com.agu.gestaoescalabackend.services.MutiraoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/mutiroes")
public class MutiraoController {

	@Autowired
	private MutiraoService service;

	@GetMapping
	public ResponseEntity<List<MutiraoDTO>> pesquisarTodos() {
		List<MutiraoDTO> list = service.findAll();
		return ResponseEntity.ok(list);
	}
	
//	@GetMapping("/{mutiraoId}")
//	public ResponseEntity<MutiraoDTO> pesquisarEspecifico (@PathVariable Long mutiraoId) {
//		MutiraoDTO mutiraoDto = service.pesquisarEspecifico(mutiraoId);
//		return ResponseEntity.ok(mutiraoDto);
//	}
	
	@GetMapping("/{mutiraoId}/pautas")
	public ResponseEntity<List<PautaDto>> pesquisarPautasDoMutirao(@PathVariable Long mutiraoId) {
		List<PautaDto> list = service.findPautas(mutiraoId);
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity<MutiraoDTO> salvar(@RequestBody MutiraoDTO mutiraoDto) {
		mutiraoDto = service.save(mutiraoDto);
		if (mutiraoDto != null)
			return ResponseEntity.ok().body(mutiraoDto);
		else
			return ResponseEntity.notFound().build();
	}

	@PutMapping("/{mutiraoId}")
	public ResponseEntity<MutiraoDTO> editar(@PathVariable Long mutiraoId, @RequestBody MutiraoDTO mutiraoDto) {
		mutiraoDto = service.update(mutiraoId, mutiraoDto);
		if (mutiraoDto != null)
			return ResponseEntity.ok().body(mutiraoDto);
		else
			return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{mutiraoId}")
	public ResponseEntity<Void> excluir(@PathVariable Long mutiraoId) {
		service.excluir(mutiraoId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{pautaDeAudienciaId}/{procuradorId}")
	public ResponseEntity<PautaDto> atualizarProcurador(@PathVariable Long pautaDeAudienciaId,
														@PathVariable Long procuradorId) {
		PautaDto pautaDto = service.atualizarProcurador(pautaDeAudienciaId, procuradorId);
		if (pautaDto != null)
			return ResponseEntity.ok().body(pautaDto);
		else
			return ResponseEntity.notFound().build();
	}


	//@PostMapping("/{mutiraoId}/escala")
	@PostMapping("/{mutiraoId}/{grupo}")
	public List<Pauta> gerarEscala(@PathVariable Long mutiraoId, @PathVariable String grupo) {
		return service.gerarEscala(mutiraoId, grupo);
	}

}
