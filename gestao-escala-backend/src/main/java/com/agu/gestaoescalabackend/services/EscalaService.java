package com.agu.gestaoescalabackend.services;

import com.agu.gestaoescalabackend.dto.EscalaDTO;
import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.entities.Mutirao;
import com.agu.gestaoescalabackend.entities.Pauta;
import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.enums.TipoPauta;
import com.agu.gestaoescalabackend.repositories.PautaRepository;
import com.agu.gestaoescalabackend.repositories.PautistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EscalaService {
	@Autowired
	private PautaRepository repository;

	@Autowired
	private PautistaRepository pautistaRepository;

//	@Transactional
//	public PautaDeAudienciaDTO editarProcurador(Long pautaDeAudienciaId, PautaDeAudienciaDTO pautaDeAudienciaDto) {
//
//		PautaDeAudiencia pautaDeAudiencia = repository.getOne(pautaDeAudienciaId);
//
//		Procurador procurador = procuradorRepository.findByNomeProcurador(pautaDeAudienciaDto.getProcuradorDto()
//				.getNomeProcurador());
//
//		pautaDeAudiencia.setProcurador(procurador);
//		pautaDeAudiencia = repository.save(pautaDeAudiencia);
//		return new PautaDeAudienciaDTO("",pautaDeAudiencia);
//	}

//	public PautaDeAudienciaDTO editarProcurador(Long pautaDeAudienciaId, Long procuradorId,
//			PautaDeAudienciaDTO pautaDeAudienciaDto) {
//
//		// Verifica se o Id existe no banco
//		if (repository.existsById(pautaDeAudienciaId)) {
//			diminuirSaldo(procuradorId);
//
//			// Instancia um objeto base que irá receber o argumento do DTO + ID e insere
//			// um procurador na pauta se for passado o nome pelo DTO
//			PautaDeAudiencia pautaDeAudiencia = new PautaDeAudiencia(pautaDeAudienciaId, pautaDeAudienciaDto);
//			inserirProcurador(pautaDeAudienciaDto, pautaDeAudiencia);
//
//			// Salva e retorna um DTO com as informações persistidas no banco
//			pautaDeAudiencia = repository.save(pautaDeAudiencia);
//			return new PautaDeAudienciaDTO(pautaDeAudiencia);
//		} else
//			return null;
//	}


	public PautaDto editarProcurador(Long pautaDeAudienciaId, Long procuradorId,
									 PautaDto pautaDto) {

		// Verifica se o Id existe no banco
		if (repository.existsById(pautaDeAudienciaId)) {
			int saldoExistente;
			String nomeProcurador;
			List<Pautista> listaPautista = pautistaRepository.findAll();
			Pautista pautista = new Pautista();

			for (Pautista pautistaEscala : listaPautista) {
				if (pautistaEscala.getId().equals(procuradorId)) {
					pautista = pautistaRepository.findByNome(pautistaEscala.getNome());
				}
			}

			saldoExistente = pautista.getSaldo();
			System.out.println("O Saldo é: " + saldoExistente);
			saldoExistente--;
			System.out.println("O Saldo diminuido é: " + saldoExistente);
			pautista.setSaldo(saldoExistente);
			pautistaRepository.save(pautista);

			// Instancia um objeto base que irá receber o argumento do DTO + ID e insere
			// um procurador na pauta se for passado o nome pelo DTO
			Pauta pauta = new Pauta(pautaDeAudienciaId, pautaDto);
			int saldo;
			// Verifica se no Repositório há um procurador com o nome passado pelo DTO
			if (pautistaRepository.existsByNome(pautaDto.getPautista().getNome())) {
				// Atribui ao objeto o procurador encontrado anteriormente
				pautista = pautistaRepository
						.findByNome(pautaDto.getPautista().getNome());
				// Seta na pauta o procurador
				pauta.setPautista(pautista);

				saldo = pautista.getSaldo();
				saldo++;
				System.out.println("O Saldo é: " + saldo);
				pautista.setSaldo(saldo);
				pautistaRepository.save(pautista);

			} else {
				// Seta nulo se não for encontrado referência para o nome do dto
				pauta.setPautista(null);
			}

			// Salva e retorna um DTO com as informações persistidas no banco
			return repository.save(pauta).toDto();
		} else
			return null;
	}

	private void inserirProcurador(PautaDto pautaDto, Pauta pauta) {
		int saldo;
		// Verifica se no Repositório há um procurador com o nome passado pelo DTO
		if (pautistaRepository.existsByNome(pautaDto.getPautista().getNome())) {
			// Atribui ao objeto o procurador encontrado anteriormente
			Pautista pautista = pautistaRepository
					.findByNome(pautaDto.getPautista().getNome());
			// Seta na pauta o procurador
			pauta.setPautista(pautista);

			saldo = pautista.getSaldo();
			saldo++;
			System.out.println("O Saldo é: " + saldo);
			pautista.setSaldo(saldo);
			pautistaRepository.save(pautista);

		} else {
			// Seta nulo se não for encontrado referência para o nome do dto
			pauta.setPautista(null);
		}

	}

	private void diminuirSaldo(Long procuradorId) {
		int saldoExistente;
		String nomeProcurador;
		List<Pautista> listaPautista = pautistaRepository.findAll();
		Pautista pautista = new Pautista();

		for (Pautista pautistaEscala : listaPautista) {
			if (pautistaEscala.getId().equals(procuradorId)) {
				pautista = pautistaRepository.findByNome(pautistaEscala.getNome());
			}
		}

		saldoExistente = pautista.getSaldo();
		System.out.println("O Saldo é: " + saldoExistente);
		saldoExistente--;
		System.out.println("O Saldo diminuido é: " + saldoExistente);
		pautista.setSaldo(saldoExistente);
		pautistaRepository.save(pautista);

	}

	public List adicionarEscala(EscalaDTO escalaDto) {

		// Contém todos os procuradores por ordem de saldo
		List<Pautista> listaPautista = pautistaRepository.findAllByOrderBySaldoPesoAsc();
		// Armazenará todas as pautas por ordem de id
		List<Pauta> listaPauta = repository.findAllByOrderByIdAsc();
		// Armazenará todos os procuradores em uma lista.
		List<Pautista> listaPautistaEscala = new ArrayList<Pautista>();
		// Armazenará todas as pautas de mesma vara
		List<Pauta> listaPautaEscala = new ArrayList<Pauta>();

		// insere na lista todas as pautas da mesma vara
		int c = 0;
		for (Pauta p : listaPauta) {
			if (p.getVara().equals(escalaDto.getVara())) {
				listaPautaEscala.add(p);
				System.out.println("Pauta:" + listaPautaEscala.get(c).getId());
				c++;
			}
		}

		TipoPauta tipoPauta = listaPautaEscala.get(0).getTipoPauta();

		// insere na lista todos os procuradores
		// int cont = 0;
		if (tipoPauta.equals(TipoPauta.INSTRUÇÃO)) {
			for (Pautista pEscala : listaPautista) {
				if ((pEscala.getGrupoPautista().toString().equalsIgnoreCase("procurador"))
						&& (pEscala.getStatusPautista().toString().equalsIgnoreCase("ativo")))
					listaPautistaEscala.add(pEscala);
			}
		} else {
			for (Pautista pEscala : listaPautista) {
				if (pEscala.getStatusPautista().toString().equalsIgnoreCase("ativo"))
					listaPautistaEscala.add(pEscala);
			}
		}

		// Determina qual é a posição do procurador no array
		int procuradorAtual = 0;
		// pega o valor da sala na primeira posição
		String salaLista = listaPautaEscala.get(0).getSala();

		// percorre a lista para inserir e salvar no banco o procurador
		for (int pautaAtual = 0; pautaAtual < listaPautaEscala.size(); pautaAtual++) {
			// compara se a sala da lista que foi pego inicialmente é igual a sala da lista
			if (salaLista.equals(listaPautaEscala.get(pautaAtual).getSala()))

				definirProcurador(listaPautistaEscala, listaPautaEscala, procuradorAtual, pautaAtual);

			else {
				if (procuradorAtual < (listaPautistaEscala.size() - 1))
					procuradorAtual++;
				else
					procuradorAtual = 0;
				// Atribui para a salaLista a sala corrente
				salaLista = listaPautaEscala.get(pautaAtual).getSala();

				definirProcurador(listaPautistaEscala, listaPautaEscala, procuradorAtual, pautaAtual);
			}
		}

		//return repository.findByVara(escalaDto.getVara());
        return null;

	}

	private void definirProcurador(List<Pautista> listaPautistaEscala, List<Pauta> listaPautaEscala,
                                   int procuradorAtual, int pautaAtual) {
		// seta na pauta o procurador na posição especificada
		listaPautaEscala.get(pautaAtual).setPautista(listaPautistaEscala.get(procuradorAtual));
		// Incrementa o saldo do procurador
		listaPautistaEscala.get(procuradorAtual).setSaldo(listaPautistaEscala.get(procuradorAtual).getSaldo() + 1);
		// Salva o procurador com o saldo atualizado no banco
		Pautista pautistaSaldo = listaPautistaEscala.get(procuradorAtual);
		pautistaRepository.save(pautistaSaldo);
		// salva a pauta no banco
		Pauta pauta = listaPautaEscala.get(pautaAtual);
		repository.save(pauta);
	}

	public void inserirMutirao(PautaDto pautaDto, List<Mutirao> mutirao,
							   Pauta pauta) {
		int x = 0;
		// Faça enquanto estiver dentro do tamanho do multirão (OU) enquanto o multirão
		// for nulo
		while ((x < mutirao.size()) || (mutirao == null)) {
			// Se a data que está sendo comparada for anterior à data passada como
			// argumento, um valor menor que zero será retornado. Se o contrário acontecer,
			// o valor retornado será maior que zero.
			if ((mutirao.get(x).getDataInicial().compareTo(pautaDto.getData()) <= 0)
					&& (mutirao.get(x).getDataFinal().compareTo(pautaDto.getData()) >= 0)) {
				// Se a condição das datas for verdadeira, seta na pauta o multirão corrente
				pauta.setMutirao(mutirao.get(x));
				break;
			}
			x++;
		}
	}

}
