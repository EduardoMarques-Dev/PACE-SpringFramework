package com.agu.gestaoescalabackend.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.agu.gestaoescalabackend.entities.PautaDeAudiencia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PautaDeAudienciaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private LocalDate data;
	private String hora;
	private String turno;
	private String sala;
	private String processo;
	private String nomeParte;
	private String cpf;
	private String nomeAdvogado;
	private String objeto;
	private String vara;
	private String tipo;

	private ProcuradorDTO procurador;
	private MutiraoDTO mutirao;

/////////////////  CONSTRUTOR  //////////////////		

	// BACK para FRONT (listar)
	public PautaDeAudienciaDTO(PautaDeAudiencia entity) {
		id = entity.getId();
		data = entity.getData();
		hora = entity.getHora();
		turno = entity.getTurno();
		sala = entity.getSala();
		processo = entity.getProcesso();
		nomeParte = entity.getNomeParte();
		cpf = entity.getCpf();
		nomeAdvogado = entity.getNomeAdvogado();
		objeto = entity.getObjeto();
		vara = entity.getVara();
		tipo = entity.getTipo();

		if (entity.getProcurador() != null) {
			procurador = new ProcuradorDTO(entity.getProcurador());
		} else {
			procurador = null;
		}

		if (entity.getMutirao() != null) {
			mutirao = new MutiraoDTO(entity.getMutirao());
		} else {
			mutirao = null;
		}

	}
}
