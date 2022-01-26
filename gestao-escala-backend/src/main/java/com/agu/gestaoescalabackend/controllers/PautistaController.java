package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.enums.StatusPautista;
import com.agu.gestaoescalabackend.repositories.PautistaRepository;
import com.agu.gestaoescalabackend.services.PautistaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/pautista")
@AllArgsConstructor
public class PautistaController {

	private PautistaService pautistaService;
	private PautistaRepository pautistaRepository;

	@GetMapping
	public ResponseEntity<List<PautistaDto>> findAll() {
		return ResponseEntity.ok(
				pautistaService.findAll());
	}

	@GetMapping("/status")
	public ResponseEntity<List<PautistaDto>> findByStatus(@RequestParam List<StatusPautista> status) {
		List<PautistaDto> pautistaDtoList = pautistaService.findByStatus(status);

		if (pautistaDtoList.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.ok(pautistaDtoList);
	}

	@PostMapping
	public ResponseEntity<PautistaDto> save(@Validated @RequestBody PautistaDto pautistaDto) {
		pautistaDto = pautistaService.save(pautistaDto);
		if (pautistaDto == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.ok(pautistaDto);

	}

	@PutMapping("/{pautistaId}")
	public ResponseEntity<PautistaDto> update(@PathVariable Long pautistaId,
											  @Validated @RequestBody PautistaDto pautistaDto) {
		pautistaDto = pautistaService.update(pautistaId, pautistaDto);
		if (pautistaDto == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.ok().body(pautistaDto);
	}

	@DeleteMapping("/{pautistaId}")
	public ResponseEntity<Void> delete(@PathVariable Long pautistaId) {
		pautistaService.delete(pautistaId);
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
		pautistaRepository.truncateTable();
		return ResponseEntity.noContent().build();
	}
}
