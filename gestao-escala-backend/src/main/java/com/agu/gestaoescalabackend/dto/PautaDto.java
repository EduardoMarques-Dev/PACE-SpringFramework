package com.agu.gestaoescalabackend.dto;

import com.agu.gestaoescalabackend.entities.Pauta;
import com.agu.gestaoescalabackend.enums.Tipo;
import com.agu.gestaoescalabackend.enums.Turno;
import com.agu.gestaoescalabackend.util.Conversor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PautaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	// ATRIBUTOS DE IDENTIFICAÇÃO
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	// ATRIBUTOS DE PERÍODO
	@NotNull
	private LocalDate data;
	@NotBlank
	private String hora;

	@NotBlank
	private String sala;
	@NotBlank
	private String processo;

	// ATRIBUTOS DE ENVOLVIDOS
	@NotBlank
	private String nomeParte;
	@CPF
	@NotNull
	private String cpf;
	@NotBlank
	private String nomeAdvogado;
	@NotBlank
	private String objeto;

	//  ATRIBUTOS DE MUTIRAO
	@NotBlank
	private String vara;
	@NotNull
	private Tipo tipo;
	@NotNull
	private Turno turno;

	// ATRIBUTOS DE RELACIONAMENTO
	private PautistaDto procurador;
	private MutiraoDTO mutirao;

	/*------------------------------------------------
     METODOS DE CONVERSÃO
    ------------------------------------------------*/

	public Pauta toEntity(){
		return Conversor.converter(this, Pauta.class);
	}
}
