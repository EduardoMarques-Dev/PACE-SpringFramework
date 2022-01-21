package com.agu.gestaoescalabackend.dto;

import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.enums.GrupoProcurador;
import com.agu.gestaoescalabackend.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class PautistaDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    private String nome;
    @NotNull
    private Status status;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    @NotNull
    private GrupoProcurador grupo;
    private Integer saldo;
    @NotNull
    private Integer peso;
    private Integer saldoPeso;

/////////////////  CONSTRUTOR  //////////////////

    // BACK para FRONT (listar)
    public PautistaDto(Pautista entity) {
        super();
        id = entity.getId();
        nome = entity.getNome();
        status = entity.getStatus();
        dataInicial = entity.getDataInicial();
        dataFinal = entity.getDataFinal();
        grupo = entity.getGrupo();
        saldo = entity.getSaldo();
        peso = entity.getPeso();
        saldoPeso = entity.getSaldoPeso();
    }

}
