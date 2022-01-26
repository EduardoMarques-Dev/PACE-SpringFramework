package com.agu.gestaoescalabackend.entities;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.enums.GrupoPautista;
import com.agu.gestaoescalabackend.enums.StatusPautista;
import com.agu.gestaoescalabackend.util.Conversor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pautista")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Pautista implements Serializable, Comparable<Pautista> {
    private static final long serialVersionUID = 1L;

    // ATRIBUTOS DE IDENTIFICAÇÃO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nome;
    @Enumerated(value = EnumType.STRING)
    private GrupoPautista grupoPautista;

    // ATRIBUTOS DE ESTADO
    @Enumerated(value = EnumType.STRING)
    private StatusPautista statusPautista;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

    // ATRIBUTOS DE ESCALA
    private Integer saldo;
    private Integer peso;
    private Integer saldoPeso;

    /*------------------------------------------------
     METODOS DE CONVERSÃO
    ------------------------------------------------*/

    public PautistaDto toDto(){
        return Conversor.converter(this, PautistaDto.class);
    }

    /*------------------------------------------------
    METODOS DE CRUD
    ------------------------------------------------*/

    public Pautista forSave(){
        return this;
    }

    public Pautista forUpdate(PautistaDto pautistaDto){
        this.nome = pautistaDto.getNome();
        this.grupoPautista = pautistaDto.getGrupoPautista();
        this.statusPautista = pautistaDto.getStatusPautista();
        this.dataInicial = pautistaDto.getDataInicial();
        this.dataFinal = pautistaDto.getDataFinal();

        this.peso = pautistaDto.getPeso();

        return this;
    }

    /*------------------------------------------------
    METODOS DE NEGÓCIO
    ------------------------------------------------*/

    @Override
    public int compareTo(Pautista outroPautista) {
        if (this.saldoPeso < outroPautista.saldoPeso) {
            return -1;
        } else if (this.saldoPeso > outroPautista.saldoPeso) {
            return 1;
        }
        return 0;
    }

}
