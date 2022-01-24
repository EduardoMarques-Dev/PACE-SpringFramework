package com.agu.gestaoescalabackend.controllers;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.services.PautistaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@GetMapping
	public ResponseEntity<List<PautistaDto>> findAll() {
		return ResponseEntity.ok(
				pautistaService.findAll());
	}

	@GetMapping("/status")
	public ResponseEntity<List<PautistaDto>> findByStatus(@RequestBody List<String> status) {
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

	@PutMapping("/{procuradorId}")
	public ResponseEntity<PautistaDto> update(@PathVariable Long id,
											  @Validated @RequestBody PautistaDto pautistaDto) {
		pautistaDto = pautistaService.update(id, pautistaDto);
		if (pautistaDto == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.ok().body(pautistaDto);
	}

	@DeleteMapping("/{procuradorId}")
	public ResponseEntity<Void> delete(@PathVariable Long procuradorId) {
		pautistaService.delete(procuradorId);
		return ResponseEntity.noContent().build();
	}

	//Exception Handling
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
}
