package com.agu.gestaoescalabackend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agu.gestaoescalabackend.dto.EscalaDTO;
import com.agu.gestaoescalabackend.dto.PautaDeAudienciaDTO;
import com.agu.gestaoescalabackend.entities.Mutirao;
import com.agu.gestaoescalabackend.entities.PautaDeAudiencia;
import com.agu.gestaoescalabackend.entities.Procurador;
import com.agu.gestaoescalabackend.repositories.PautaDeAudienciaRepository;
import com.agu.gestaoescalabackend.repositories.ProcuradorRepository;

@Service
public class EscalaService {
	@Autowired
	private PautaDeAudienciaRepository repository;

	@Autowired
	private ProcuradorRepository procuradorRepository;

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
	

	public PautaDeAudienciaDTO editarProcurador(Long pautaDeAudienciaId, Long procuradorId,
			PautaDeAudienciaDTO pautaDeAudienciaDto) {

		// Verifica se o Id existe no banco
		if (repository.existsById(pautaDeAudienciaId)) {
			int saldoExistente;
			String nomeProcurador;
			List<Procurador> listaProcurador = procuradorRepository.findAll();
			Procurador procurador = new Procurador();

			for (Procurador procuradorEscala : listaProcurador) {
				if (procuradorEscala.getId().equals(procuradorId)) {
					procurador = procuradorRepository.findByNomeProcurador(procuradorEscala.getNomeProcurador());
				}
			}

			saldoExistente = procurador.getSaldo();
			System.out.println("O Saldo é: " + saldoExistente);
			saldoExistente--;
			System.out.println("O Saldo diminuido é: " + saldoExistente);
			procurador.setSaldo(saldoExistente);
			procuradorRepository.save(procurador);

			// Instancia um objeto base que irá receber o argumento do DTO + ID e insere
			// um procurador na pauta se for passado o nome pelo DTO
			PautaDeAudiencia pautaDeAudiencia = new PautaDeAudiencia(pautaDeAudienciaId, pautaDeAudienciaDto);
			int saldo;
			// Verifica se no Repositório há um procurador com o nome passado pelo DTO
			if (procuradorRepository.existsByNomeProcurador(pautaDeAudienciaDto.getProcurador().getNomeProcurador())) {
				// Atribui ao objeto o procurador encontrado anteriormente
				procurador = procuradorRepository
						.findByNomeProcurador(pautaDeAudienciaDto.getProcurador().getNomeProcurador());
				// Seta na pauta o procurador
				pautaDeAudiencia.setProcurador(procurador);

				saldo = procurador.getSaldo();
				saldo++;
				System.out.println("O Saldo é: " + saldo);
				procurador.setSaldo(saldo);
				procuradorRepository.save(procurador);

			} else {
				// Seta nulo se não for encontrado referência para o nome do dto
				pautaDeAudiencia.setProcurador(null);
			}

			
			// Salva e retorna um DTO com as informações persistidas no banco
			pautaDeAudiencia = repository.save(pautaDeAudiencia);
			return new PautaDeAudienciaDTO(pautaDeAudiencia);
		} else
			return null;
	}

	private void inserirProcurador(PautaDeAudienciaDTO pautaDeAudienciaDto, PautaDeAudiencia pautaDeAudiencia) {
		int saldo;
		// Verifica se no Repositório há um procurador com o nome passado pelo DTO
		if (procuradorRepository.existsByNomeProcurador(pautaDeAudienciaDto.getProcurador().getNomeProcurador())) {
			// Atribui ao objeto o procurador encontrado anteriormente
			Procurador procurador = procuradorRepository
					.findByNomeProcurador(pautaDeAudienciaDto.getProcurador().getNomeProcurador());
			// Seta na pauta o procurador
			pautaDeAudiencia.setProcurador(procurador);

			saldo = procurador.getSaldo();
			saldo++;
			System.out.println("O Saldo é: " + saldo);
			procurador.setSaldo(saldo);
			procuradorRepository.save(procurador);

		} else {
			// Seta nulo se não for encontrado referência para o nome do dto
			pautaDeAudiencia.setProcurador(null);
		}

	}

	private void diminuirSaldo(Long procuradorId) {
		int saldoExistente;
		String nomeProcurador;
		List<Procurador> listaProcurador = procuradorRepository.findAll();
		Procurador procurador = new Procurador();

		for (Procurador procuradorEscala : listaProcurador) {
			if (procuradorEscala.getId().equals(procuradorId)) {
				procurador = procuradorRepository.findByNomeProcurador(procuradorEscala.getNomeProcurador());
			}
		}

		saldoExistente = procurador.getSaldo();
		System.out.println("O Saldo é: " + saldoExistente);
		saldoExistente--;
		System.out.println("O Saldo diminuido é: " + saldoExistente);
		procurador.setSaldo(saldoExistente);
		procuradorRepository.save(procurador);

	}

	public List adicionarEscala(EscalaDTO escalaDto) {

		// Contém todos os procuradores por ordem de saldo
		List<Procurador> listaProcurador = procuradorRepository.findAllByOrderBySaldoPesoAsc();
		// Armazenará todas as pautas por ordem de id
		List<PautaDeAudiencia> listaPauta = repository.findAllByOrderByIdAsc();
		// Armazenará todos os procuradores em uma lista.
		List<Procurador> listaProcuradorEscala = new ArrayList<Procurador>();
		// Armazenará todas as pautas de mesma vara
		List<PautaDeAudiencia> listaPautaEscala = new ArrayList<PautaDeAudiencia>();

		// insere na lista todas as pautas da mesma vara
		int c = 0;
		for (PautaDeAudiencia p : listaPauta) {
			if (p.getVara().equals(escalaDto.getVara())) {
				listaPautaEscala.add(p);
				System.out.println("Pauta:" + listaPautaEscala.get(c).getId());
				c++;
			}
		}

		String tipo = listaPautaEscala.get(0).getTipo();

		// insere na lista todos os procuradores
		// int cont = 0;
		if (tipo.equalsIgnoreCase("instrução")) {
			for (Procurador pEscala : listaProcurador) {
				if ((pEscala.getGrupo().equalsIgnoreCase("procurador"))
						&& (pEscala.getStatus().equalsIgnoreCase("ativo")))
					listaProcuradorEscala.add(pEscala);
			}
		} else {
			for (Procurador pEscala : listaProcurador) {
				if (pEscala.getStatus().equalsIgnoreCase("ativo"))
					listaProcuradorEscala.add(pEscala);
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

				definirProcurador(listaProcuradorEscala, listaPautaEscala, procuradorAtual, pautaAtual);

			else {
				if (procuradorAtual < (listaProcuradorEscala.size() - 1))
					procuradorAtual++;
				else
					procuradorAtual = 0;
				// Atribui para a salaLista a sala corrente
				salaLista = listaPautaEscala.get(pautaAtual).getSala();

				definirProcurador(listaProcuradorEscala, listaPautaEscala, procuradorAtual, pautaAtual);
			}
		}

		return repository.findByVara(escalaDto.getVara());

	}

	private void definirProcurador(List<Procurador> listaProcuradorEscala, List<PautaDeAudiencia> listaPautaEscala,
			int procuradorAtual, int pautaAtual) {
		// seta na pauta o procurador na posição especificada
		listaPautaEscala.get(pautaAtual).setProcurador(listaProcuradorEscala.get(procuradorAtual));
		// Incrementa o saldo do procurador
		listaProcuradorEscala.get(procuradorAtual).setSaldo(listaProcuradorEscala.get(procuradorAtual).getSaldo() + 1);
		// Salva o procurador com o saldo atualizado no banco
		Procurador procuradorSaldo = listaProcuradorEscala.get(procuradorAtual);
		procuradorRepository.save(procuradorSaldo);
		// salva a pauta no banco
		PautaDeAudiencia pauta = listaPautaEscala.get(pautaAtual);
		repository.save(pauta);
	}

	public void inserirMutirao(PautaDeAudienciaDTO pautaDeAudienciaDto, List<Mutirao> mutirao,
			PautaDeAudiencia pautaDeAudiencia) {
		int x = 0;
		// Faça enquanto estiver dentro do tamanho do multirão (OU) enquanto o multirão
		// for nulo
		while ((x < mutirao.size()) || (mutirao == null)) {
			// Se a data que está sendo comparada for anterior à data passada como
			// argumento, um valor menor que zero será retornado. Se o contrário acontecer,
			// o valor retornado será maior que zero.
			if ((mutirao.get(x).getDataInicial().compareTo(pautaDeAudienciaDto.getData()) <= 0)
					&& (mutirao.get(x).getDataFinal().compareTo(pautaDeAudienciaDto.getData()) >= 0)) {
				// Se a condição das datas for verdadeira, seta na pauta o multirão corrente
				pautaDeAudiencia.setMutirao(mutirao.get(x));
				break;
			}
			x++;
		}
	}

}
