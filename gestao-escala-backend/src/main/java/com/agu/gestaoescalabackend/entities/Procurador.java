package com.agu.gestaoescalabackend.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.agu.gestaoescalabackend.dto.ProcuradorDTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_procuradores")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Procurador implements Serializable, Comparable<Procurador> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	@Column(name = "nome_procurador")
	private String nomeProcurador;
	private String status;
	@Column(name = "data_inicial")
	private LocalDate dataInicial;
	@Column(name = "data_final")
	private LocalDate dataFinal;
	private String grupo;
	private Integer saldo;
	private Integer peso;
	private Integer saldoPeso;
	

	// @OneToMany(mappedBy = "procurador")
	// private List<PautaDeAudiencia> pautas = new ArrayList<>();

/////////////////  CONSTRUTOR  //////////////////

	// FRONT para BACK (Salvar)
	public Procurador(ProcuradorDTO dto) {
		super();
		id = dto.getId();
		nomeProcurador = dto.getNomeProcurador();
		status = dto.getStatus();
		dataInicial = dto.getDataInicial();
		dataFinal = dto.getDataFinal();
		grupo = dto.getGrupo();
		saldo = dto.getSaldo();
		peso = dto.getPeso();
		saldoPeso = dto.getSaldoPeso();
	}

	// FRONT para BACK com ID (Editar)
	public Procurador(Long id, ProcuradorDTO dto) {
		super();
		this.id = id;
		nomeProcurador = dto.getNomeProcurador();
		status = dto.getStatus();
		dataInicial = dto.getDataInicial();
		dataFinal = dto.getDataFinal();
		grupo = dto.getGrupo();
		saldo = dto.getSaldo();
		peso = dto.getPeso();
		saldoPeso = dto.getSaldoPeso();
	}

/////////////////  MÉTODOS  //////////////////

	@Override
	public int compareTo(Procurador outroProcurador) {
		if (this.saldoPeso < outroProcurador.saldoPeso) {
			return -1;
		} else if (this.saldoPeso > outroProcurador.saldoPeso) {
			return 1;
		}
		return 0;
	}

}
