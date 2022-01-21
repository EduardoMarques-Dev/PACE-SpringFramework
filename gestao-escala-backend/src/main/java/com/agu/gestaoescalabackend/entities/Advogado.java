package com.agu.gestaoescalabackend.entities;

import com.agu.gestaoescalabackend.dto.AdvogadoDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_advogado")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Advogado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	private String nomeAdvogado;
	private String numeroOAB;
	

/////////////////  CONSTRUTOR  //////////////////

	// FRONT para BACK (Salvar)
	public Advogado(AdvogadoDTO dto) {
		super();
		id = dto.getId();
		nomeAdvogado = dto.getNomeAdvogado();
		numeroOAB = dto.getNumeroOAB();
	}

	// FRONT para BACK com ID (Editar)
	public Advogado(Long id, AdvogadoDTO dto) {
		super();
		this.id = id;
		nomeAdvogado = dto.getNomeAdvogado();
		numeroOAB = dto.getNumeroOAB();
	}

}
