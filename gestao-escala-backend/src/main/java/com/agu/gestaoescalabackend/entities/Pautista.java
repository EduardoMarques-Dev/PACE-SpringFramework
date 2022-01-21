package com.agu.gestaoescalabackend.entities;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.enums.GrupoProcurador;
import com.agu.gestaoescalabackend.enums.Status;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    @Enumerated(value = EnumType.STRING)
    private GrupoProcurador grupo;
    private Integer saldo;
    private Integer peso;
    private Integer saldoPeso;


    // ATRIBUTOS DE RELACIONAMENTO
    // @OneToMany(mappedBy = "procurador")
    // private List<PautaDeAudiencia> pautas = new ArrayList<>();

/////////////////  CONSTRUTOR  //////////////////

    // FRONT para BACK (Salvar)
    public Pautista(PautistaDto dto) {
        super();
        id = dto.getId();
        nome = dto.getNome();
        status = dto.getStatus();
        dataInicial = dto.getDataInicial();
        dataFinal = dto.getDataFinal();
        grupo = dto.getGrupo();
        saldo = dto.getSaldo();
        peso = dto.getPeso();
        saldoPeso = dto.getSaldoPeso();
    }

    // FRONT para BACK com ID (Editar)
    public Pautista(Long id, PautistaDto dto) {
        super();
        this.id = id;
        nome = dto.getNome();
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
    public int compareTo(Pautista outroPautista) {
        if (this.saldoPeso < outroPautista.saldoPeso) {
            return -1;
        } else if (this.saldoPeso > outroPautista.saldoPeso) {
            return 1;
        }
        return 0;
    }

}
