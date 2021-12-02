package com.agu.gestaoescalabackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agu.gestaoescalabackend.dto.EscalaDTO;
import com.agu.gestaoescalabackend.dto.PautaDeAudienciaDTO;
import com.agu.gestaoescalabackend.repositories.PautaDeAudienciaRepository;
import com.agu.gestaoescalabackend.services.EscalaService;

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
