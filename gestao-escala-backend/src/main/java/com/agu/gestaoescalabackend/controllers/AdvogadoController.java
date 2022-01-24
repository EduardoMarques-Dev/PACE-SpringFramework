package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.AdvogadoDto;
import com.agu.gestaoescalabackend.services.AdvogadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/advogado")
public class AdvogadoController {

	@Autowired
	private AdvogadoService advogadoService;

	@GetMapping
	public ResponseEntity<List<AdvogadoDto>> findAll() {
		List<AdvogadoDto> list = advogadoService.findAll();
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity<AdvogadoDto> save(@Valid @RequestBody AdvogadoDto advogadoDto) {
		advogadoDto = advogadoService.save(advogadoDto);
		if (advogadoDto != null)
			return ResponseEntity.ok(advogadoDto);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@PutMapping("/{advogadoId}")
	public ResponseEntity<AdvogadoDto> update(@PathVariable Long advogadoId, @Valid @RequestBody AdvogadoDto advogadoDto) {
		advogadoDto = advogadoService.update(advogadoId, advogadoDto);
		if (advogadoDto != null)
			return ResponseEntity.ok(advogadoDto);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@DeleteMapping("/{advogadoId}")
	public ResponseEntity<Void> delete(@PathVariable Long advogadoId) {
		advogadoService.delete(advogadoId);
		return ResponseEntity.noContent().build();
	}
}
