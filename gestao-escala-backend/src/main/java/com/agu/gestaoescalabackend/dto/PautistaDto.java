package com.agu.gestaoescalabackend.dto;

import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.enums.Grupo;
import com.agu.gestaoescalabackend.enums.Status;
import com.agu.gestaoescalabackend.util.Conversor;
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

    // ATRIBUTOS DE IDENTIFICAÇÃO
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    private String nome;
    @NotNull
    private Grupo grupo;

    // ATRIBUTOS DE ESTADO
    @NotNull
    private Status status;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

    // ATRIBUTOS DE ESCALA
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer saldo;
    @NotNull
    private Integer peso;
    private Integer saldoPeso;

    /*------------------------------------------------
     METODOS DE CONVERSÃO
    ------------------------------------------------*/

    public Pautista toEntity(){
        return Conversor.converter(this, Pautista.class);
    }

}
