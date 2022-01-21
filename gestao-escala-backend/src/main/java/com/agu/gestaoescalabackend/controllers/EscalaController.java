package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.EscalaDTO;
import com.agu.gestaoescalabackend.dto.PautaDeAudienciaDTO;
import com.agu.gestaoescalabackend.repositories.PautaDeAudienciaRepository;
import com.agu.gestaoescalabackend.services.EscalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/escala")
public class EscalaController {
	@Autowired
	private EscalaService service;

	@Autowired
	private PautaDeAudienciaRepository repository;

	@PutMapping("/{pautaDeAudienciaId}/{procuradorId}")
	public ResponseEntity<PautaDeAudienciaDTO> atualizarProcurador(@PathVariable Long pautaDeAudienciaId,
			@PathVariable Long procuradorId, @RequestBody PautaDeAudienciaDTO pautaDeAudienciaDto) {

		if (!repository.existsById(pautaDeAudienciaId)) {
			return ResponseEntity.notFound().build();
		}
		pautaDeAudienciaDto = service.editarProcurador(pautaDeAudienciaId, procuradorId, pautaDeAudienciaDto);

		return ResponseEntity.ok().body(pautaDeAudienciaDto);
	}

	@PostMapping
	public List gerarEscala(@RequestBody EscalaDTO escala) {

		return service.adicionarEscala(escala);
	}

}

//@PutMapping("/{pautaDeAudienciaId}")
//public ResponseEntity<PautaDeAudienciaDTO> atualizarProcurador(@PathVariable Long pautaDeAudienciaId, 
//		@RequestBody PautaDeAudienciaDTO pautaDeAudienciaDto){
//	 
//	if(!repository.existsById(pautaDeAudienciaId)) {
//		return ResponseEntity.notFound().build();
//	}
//	pautaDeAudienciaDto = service.editar(pautaDeAudienciaId, pautaDeAudienciaDto);
//	
//	return ResponseEntity.ok().body(pautaDeAudienciaDto);
//}	 
