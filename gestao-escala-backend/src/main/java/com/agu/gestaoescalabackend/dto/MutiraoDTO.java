package com.agu.gestaoescalabackend.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.agu.gestaoescalabackend.entities.Mutirao;
import com.agu.gestaoescalabackend.entities.TipoStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MutiraoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer quantidaDePautas;
	private String vara;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	private TipoStatus status;

/////////////////  CONSTRUTOR  //////////////////

	// BACK para FRONT (listar)
	public MutiraoDTO(Mutirao entity) {
		this.id = entity.getId();
		this.vara = entity.getVara();
		this.dataInicial = entity.getDataInicial();
		this.dataFinal = entity.getDataFinal();
		this.status = entity.getStatus();
		this.quantidaDePautas = entity.getQuantidaDePautas();
	}

	// CRIAÇÃO AUTOMÁTICA Mutirao
	public MutiraoDTO(List<PautaDeAudienciaDTO> listDto) {
		this.id = null;
		this.vara = listDto.get(0).getVara();
		this.dataInicial = listDto.get(0).getData();
		this.dataFinal = listDto.get(listDto.size() - 1).getData();
		this.status = null;
		this.quantidaDePautas = null;
	}

}
