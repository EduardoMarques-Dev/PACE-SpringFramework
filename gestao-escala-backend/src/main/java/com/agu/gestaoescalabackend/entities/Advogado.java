package com.agu.gestaoescalabackend.entities;

import com.agu.gestaoescalabackend.dto.AdvogadoDto;
import com.agu.gestaoescalabackend.util.Conversor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_advogado")
@Getter
@Setter
@NoArgsConstructor
public class Advogado implements Serializable {
	private static final long serialVersionUID = 1L;

	// ATRIBUTOS DE IDENTIFICAÇÃO
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String nome;
	private String numeroOAB;

 	/*------------------------------------------------
     METODOS DE CONVERSÃO
    ------------------------------------------------*/

	public AdvogadoDto toDto(){
		return Conversor.converter(this, AdvogadoDto.class);
	}

	/*------------------------------------------------
    METODOS DE CRUD
    ------------------------------------------------*/

	public Advogado forSave(){
		return this;
	}

	public Advogado forUpdate(Long id){
		this.id = id;
		return this;
	}


/////////////////  CONSTRUTOR  //////////////////

	// FRONT para BACK com ID (Editar)
	public Advogado(Long id, AdvogadoDto dto) {
		super();
		this.id = id;
		nome = dto.getNomeAdvogado();
		numeroOAB = dto.getNumeroOAB();
	}

}
