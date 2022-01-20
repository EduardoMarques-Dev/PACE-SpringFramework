package com.agu.gestaoescalabackend.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agu.gestaoescalabackend.dto.PautaDeAudienciaDTO;

import com.agu.gestaoescalabackend.util.Conversor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_pauta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PautaDeAudiencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	private LocalDate data;
	private String hora;
	private String turno;
	private String sala;
	private String processo;

	@Column(name = "nome_parte")
	private String nomeParte;
	private String cpf;

	@Column(name = "nome_advogado")
	private String nomeAdvogado;
	private String objeto;
	private String vara;
	private String tipo;

	@ManyToOne
	@JoinColumn(name = "procurador_id")
	private Procurador procurador;

	@ManyToOne
	@JoinColumn(name = "mutirao_id")
	private Mutirao mutirao;

/////////////////  CONSTRUTOR  //////////////////

	// FRONT PARA O BACK (Salvar)
	public PautaDeAudiencia(PautaDeAudienciaDTO dto) {
		super();
		id = dto.getId();
		data = dto.getData();
		hora = dto.getHora();
		turno = dto.getTurno();
		sala = dto.getSala();
		processo = dto.getProcesso();
		nomeParte = dto.getNomeParte();
		cpf = dto.getCpf();
		nomeAdvogado = dto.getNomeAdvogado();
		objeto = dto.getObjeto();
		vara = dto.getVara();
		procurador = null;
		tipo = dto.getTipo();
	}

	// FRONT PARA O BACK + ID (Editar)
	public PautaDeAudiencia(Long id, PautaDeAudienciaDTO dto) {
		this.id = id;
		data = dto.getData();
		hora = dto.getHora();
		turno = dto.getTurno();
		sala = dto.getSala();
		processo = dto.getProcesso();
		nomeParte = dto.getNomeParte();
		cpf = dto.getCpf();
		nomeAdvogado = dto.getNomeAdvogado();
		objeto = dto.getObjeto();
		vara = dto.getVara();
		procurador = null;
		tipo = dto.getTipo();
	}

	public PautaDeAudienciaDTO toDto(){
		return Conversor.converter(this, PautaDeAudienciaDTO.class);
	}
}
