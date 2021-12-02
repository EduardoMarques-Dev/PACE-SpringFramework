package com.agu.gestaoescalabackend.dto;

import java.io.Serializable;

import com.agu.gestaoescalabackend.entities.Advogado;

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
	private Long id;
	private String nomeAdvogado;
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
