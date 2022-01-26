package com.agu.gestaoescalabackend.entities;

import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.dto.PautistaDto;
import com.agu.gestaoescalabackend.enums.TipoPauta;
import com.agu.gestaoescalabackend.enums.TurnoPauta;
import com.agu.gestaoescalabackend.util.Conversor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pauta")
@Getter
@Setter
@NoArgsConstructor
public class Pauta implements Serializable {
	private static final long serialVersionUID = 1L;

	// ATRIBUTOS DE IDENTIFICAÇÃO
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	// ATRIBUTOS DE PERÍODO
	private LocalDate data;
	private String hora;
	private String sala;
	private String processo;

	// ATRIBUTOS DE ENVOLVIDOS
	@Column(name = "nome_parte")
	private String nomeParte;
	private String cpf;
	@Column(name = "nome_advogado")
	private String nomeAdvogado;
	private String objeto;

	//  ATRIBUTOS DE MUTIRAO
	private String vara;
	@Enumerated(value = EnumType.STRING)
	private TipoPauta tipoPauta;
	@Enumerated(value = EnumType.STRING)
	private TurnoPauta turnoPauta;

	// ATRIBUTOS DE RELACIONAMENTO
	@ManyToOne
	@JoinColumn(name = "pautista_id")
	private Pautista pautista;
	@ManyToOne
	@JoinColumn(name = "mutirao_id")
	private Mutirao mutirao;

	/*------------------------------------------------
     METODOS DE CONVERSÃO
    ------------------------------------------------*/

	public PautaDto toDto(){
		return Conversor.converter(this, PautaDto.class);
	}


  	/*------------------------------------------------
    METODOS DE CRUD
    ------------------------------------------------*/

	public Pauta forSave(){
		return this;
	}

	public Pauta forUpdate(PautaDto pautaDto) {

		this.data = pautaDto.getData();
		this.hora = pautaDto.getHora();
		this.sala = pautaDto.getSala();
		this.processo = pautaDto.getProcesso();

		this.nomeParte = pautaDto.getNomeParte();
		this.cpf = pautaDto.getCpf();
		this.nomeAdvogado = pautaDto.getNomeAdvogado();
		this.objeto = pautaDto.getObjeto();

		this.vara = pautaDto.getVara();
		this.tipoPauta = pautaDto.getTipoPauta();
		this.turnoPauta = pautaDto.getTurnoPauta();

		return this;
	}

	/*------------------------------------------------
     METODOS DE NEGÓCIO
    ------------------------------------------------*/

	public boolean isTheSame(Pauta pauta){
		return this.sala == pauta.getSala()
				&& this.data == pauta.getData()
				&& this.turnoPauta == pauta.getTurnoPauta();
	}

/////////////////  CONSTRUTOR  //////////////////

	// FRONT PARA O BACK (Salvar)

	// FRONT PARA O BACK + ID (Editar)
	public Pauta(Long id, PautaDto dto) {
		this.id = id;
		data = dto.getData();
		hora = dto.getHora();
		turnoPauta = dto.getTurnoPauta();
		sala = dto.getSala();
		processo = dto.getProcesso();
		nomeParte = dto.getNomeParte();
		cpf = dto.getCpf();
		nomeAdvogado = dto.getNomeAdvogado();
		objeto = dto.getObjeto();
		vara = dto.getVara();
		pautista = null;
		tipoPauta = dto.getTipoPauta();
	}


}
