package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.EscalaDTO;
import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.repositories.PautaRepository;
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
	private PautaRepository repository;

	@PutMapping("/{pautaDeAudienciaId}/{procuradorId}")
	public ResponseEntity<PautaDto> atualizarProcurador(@PathVariable Long pautaDeAudienciaId,
														@PathVariable Long procuradorId, @RequestBody PautaDto pautaDto) {

		if (!repository.existsById(pautaDeAudienciaId)) {
			return ResponseEntity.notFound().build();
		}
		pautaDto = service.editarProcurador(pautaDeAudienciaId, procuradorId, pautaDto);

		return ResponseEntity.ok().body(pautaDto);
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
