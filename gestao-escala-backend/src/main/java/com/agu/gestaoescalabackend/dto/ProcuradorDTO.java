package com.agu.gestaoescalabackend.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.agu.gestaoescalabackend.entities.Procurador;
import com.agu.gestaoescalabackend.enums.GrupoProcurador;
import com.agu.gestaoescalabackend.enums.Statusprocurador;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ProcuradorDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	@NotBlank
	private String nomeProcurador;
	@NotNull
	private Statusprocurador status;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	@NotNull
	private GrupoProcurador grupo;
	private Integer saldo;
	@NotNull
	private Integer peso;
	private Integer saldoPeso;

/////////////////  CONSTRUTOR  //////////////////

	public ProcuradorDTO() {
	}

	// Todos os Campos
	public ProcuradorDTO(Long id, String nomeProcurador, Statusprocurador status, LocalDate dataInicial, LocalDate dataFinal,
	GrupoProcurador grupo, Integer saldo, Integer peso, Integer saldoPeso) {
		super();
		this.id = id;
		this.nomeProcurador = nomeProcurador;
		this.status = status;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.grupo = grupo;
		this.saldo = saldo;
		this.peso = peso;
		this.saldoPeso = saldoPeso;
	}

	// BACK para FRONT (listar)
	public ProcuradorDTO(Procurador entity) {
		super();
		id = entity.getId();
		nomeProcurador = entity.getNomeProcurador();
		status = entity.getStatus();
		dataInicial = entity.getDataInicial();
		dataFinal = entity.getDataFinal();
		grupo = entity.getGrupo();
		saldo = entity.getSaldo();
		peso = entity.getPeso();
		saldoPeso = entity.getSaldoPeso();
	}

///////////////// GET & SETTER //////////////////	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeProcurador() {
		return nomeProcurador;
	}

	public void setNomeProcurador(String nomeProcurador) {
		this.nomeProcurador = nomeProcurador;
	}

	public Statusprocurador getStatus() {
		return status;
	}

	public void setStatus(Statusprocurador status) {
		this.status = status;
	}

	public LocalDate getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(LocalDate dataInicial) {
		this.dataInicial = dataInicial;
	}

	public LocalDate getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}

	public GrupoProcurador getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoProcurador grupo) {
		this.grupo = grupo;
	}

	public Integer getSaldo() {
		return saldo;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}
	
	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	public Integer getSaldoPeso() {
		return saldoPeso;
	}

	public void setSaldoPeso(Integer saldoPeso) {
		this.saldoPeso = saldoPeso;
	}
	
	

}
