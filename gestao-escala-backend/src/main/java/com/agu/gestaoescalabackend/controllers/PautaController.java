package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.repositories.PautaRepository;
import com.agu.gestaoescalabackend.services.PautaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/pauta")
@AllArgsConstructor
public class PautaController {

	private PautaService pautaService;
	private PautaRepository pautaRepository;

	@GetMapping
	public ResponseEntity<List<PautaDto>> findAll() {
		return ResponseEntity.ok(
				pautaService.findAll());
	}

	@GetMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<PautaDto> findById(@PathVariable Long pautaDeAudienciaId) {
		PautaDto pautaDto = pautaService.findById(pautaDeAudienciaId);
		return ResponseEntity.ok(pautaDto);
	}
	
	@PostMapping
	public ResponseEntity<List<PautaDto>> save(@RequestBody List<PautaDto> PautaDto) {
		List<PautaDto> listaPautaDto = pautaService.save(PautaDto);
		if (listaPautaDto == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.ok(listaPautaDto);
	}

	@PutMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<PautaDto> update(@PathVariable Long pautaDeAudienciaId,
										   @RequestBody PautaDto pautaDto) {
		pautaDto = pautaService.editar(pautaDeAudienciaId, pautaDto);
		if (pautaDto == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.ok(pautaDto);
	}

	@DeleteMapping("/{pautaDeAudienciaId}")
	public ResponseEntity<Void> delete(@PathVariable Long pautaDeAudienciaId) {
		pautaService.excluir(pautaDeAudienciaId);
		return ResponseEntity.noContent().build();
	}

	/*------------------------------------------------
    MANIPULADOR DE EXCESSÕES
    ------------------------------------------------*/

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptio(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) ->{
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();

			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	/*------------------------------------------------
    ACTIONS DE DESENVOLVIMENTO
    ------------------------------------------------*/

	@PutMapping("/truncate")
	@Transactional
	public ResponseEntity<Void> truncatePautistaTable() {
		pautaRepository.truncateTable();
		return ResponseEntity.noContent().build();
	}

}
