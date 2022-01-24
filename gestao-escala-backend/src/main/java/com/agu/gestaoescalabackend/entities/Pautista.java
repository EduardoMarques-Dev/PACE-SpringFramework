package com.agu.gestaoescalabackend.entities;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.enums.Grupo;
import com.agu.gestaoescalabackend.enums.Status;
import com.agu.gestaoescalabackend.util.Conversor;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pautistas")
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
    private Grupo grupo;

    // ATRIBUTOS DE ESTADO
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

    // ATRIBUTOS DE ESCALA
    private Integer saldo;
    private Integer peso;
    private Integer saldoPeso;


    // ATRIBUTOS DE RELACIONAMENTO
    // @OneToMany(mappedBy = "procurador")
    // private List<PautaDeAudiencia> pautas = new ArrayList<>();

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

    public Pautista forUpdate(Long pautistaId){
        id = pautistaId;
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
