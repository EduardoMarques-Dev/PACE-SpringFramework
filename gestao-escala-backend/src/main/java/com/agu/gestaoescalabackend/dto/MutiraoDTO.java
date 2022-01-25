package com.agu.gestaoescalabackend.dto;

import com.agu.gestaoescalabackend.entities.Mutirao;
import com.agu.gestaoescalabackend.enums.StatusPauta;
import com.agu.gestaoescalabackend.util.Conversor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MutiraoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	// ATRIBUTOS DE IDENTIFICAÇÃO
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "status")
	private StatusPauta statusPauta;

	// ATRIBUTOS DE REGISTRO
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer quantidaDePautas;

	// ATRIBUTOS PADRÃO
	@NotNull
	private String vara;
	@NotNull
	private LocalDate dataInicial;
	@NotNull
	private LocalDate dataFinal;

	/*------------------------------------------------
     METODOS DE CONVERSÃO
    ------------------------------------------------*/

	public Mutirao toEntity(){
		return Conversor.converter(this, Mutirao.class);
	}

	/*------------------------------------------------
    METODOS DE CRUD
    ------------------------------------------------*/

	public MutiraoDTO forSave(List<PautaDto> pautaDtoList){

		this.vara = pautaDtoList.get(0).getVara();
		this.dataInicial = pautaDtoList.get(0).getData();
		this.dataFinal = pautaDtoList.get(pautaDtoList.size() - 1).getData();

		return this;
	}

	public MutiraoDTO forUpdate(Long id){
		this.id = id;
		return this;
	}
}
