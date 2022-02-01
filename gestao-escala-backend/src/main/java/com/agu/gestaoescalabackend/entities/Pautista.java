package com.agu.gestaoescalabackend.entities;

import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.enums.GrupoPautista;
import com.agu.gestaoescalabackend.enums.StatusPautista;
import com.agu.gestaoescalabackend.util.Conversor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_pautista")
@Getter
@Setter
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
    @Formula("(select count(*) FROM tb_pauta s WHERE s.pautista_id = id)")
    private Integer saldo;
    private Integer peso;
    private Integer saldoPeso;

    // ATRIBUTOS DE RELACIONAMENTO
    @OneToMany(mappedBy = "pautista")
    private List<Pauta> pautas;

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
        this.saldoPeso = 0;
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

    public boolean estaDisponivel(LocalDate dataPassada){
        for (Pauta pauta : this.getPautas())
            if(pauta.getData().equals(dataPassada))
                return false;
        return true;
    }

    public void atualizarSaldo(int valor){
        this.setSaldo(this.getSaldo() + valor);
        this.setSaldoPeso(this.getSaldo() * this.getPeso());
    }

}
