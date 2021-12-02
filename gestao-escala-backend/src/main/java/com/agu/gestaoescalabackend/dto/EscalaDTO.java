package com.agu.gestaoescalabackend.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EscalaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String vara;
	private String dataInicial;
	private String dataFinal;

}
