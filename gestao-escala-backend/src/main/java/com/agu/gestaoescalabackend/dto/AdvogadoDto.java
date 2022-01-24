package com.agu.gestaoescalabackend.dto;

import com.agu.gestaoescalabackend.entities.Advogado;
import com.agu.gestaoescalabackend.util.Conversor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class AdvogadoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	// ATRIBUTOS DE IDENTIFICAÇÃO
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	@NotBlank
	private String nomeAdvogado;
	@NotBlank
	private String numeroOAB;

	 /*------------------------------------------------
     METODOS DE CONVERSÃO
    ------------------------------------------------*/

	public Advogado toEntity(){
		return Conversor.converter(this, Advogado.class);
	}

}
