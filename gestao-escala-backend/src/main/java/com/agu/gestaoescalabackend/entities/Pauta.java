package com.agu.gestaoescalabackend.entities;

import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.enums.Tipo;
import com.agu.gestaoescalabackend.enums.Turno;
import com.agu.gestaoescalabackend.util.Conversor;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pautas")
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
	private Tipo tipo;
	@Enumerated(value = EnumType.STRING)
	private Turno turno;

	// ATRIBUTOS DE RELACIONAMENTO
	@ManyToOne
	@JoinColumn(name = "procurador_id")
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

/////////////////  CONSTRUTOR  //////////////////

	// FRONT PARA O BACK (Salvar)

	// FRONT PARA O BACK + ID (Editar)
	public Pauta(Long id, PautaDto dto) {
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
		pautista = null;
		tipo = dto.getTipo();
	}


}
