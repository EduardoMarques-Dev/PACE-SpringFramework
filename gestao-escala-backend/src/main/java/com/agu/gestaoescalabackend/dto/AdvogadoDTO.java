package com.agu.gestaoescalabackend.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.agu.gestaoescalabackend.entities.Advogado;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.annotation.ReadOnlyProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvogadoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	@NotBlank
	private String nomeAdvogado;
	@NotBlank
	private String numeroOAB;

/////////////////  CONSTRUTOR  //////////////////

	// BACK para FRONT (listar)
	public AdvogadoDTO(Advogado entity) {
		super();
		id = entity.getId();
		nomeAdvogado = entity.getNomeAdvogado();
		numeroOAB = entity.getNumeroOAB();
	}

}
