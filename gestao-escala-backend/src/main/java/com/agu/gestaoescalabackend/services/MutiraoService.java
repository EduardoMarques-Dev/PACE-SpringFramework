/**
 * @author Carlos Eduardo
 * A classe <b>Mutirao Service</b> é utilizada para tratar das regras de negócio relacionadas à entidade <b>Mutirao</b>. 
*/
package com.agu.gestaoescalabackend.services;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agu.gestaoescalabackend.dto.MutiraoDTO;
import com.agu.gestaoescalabackend.dto.PautaDeAudienciaDTO;
import com.agu.gestaoescalabackend.entities.Mutirao;
import com.agu.gestaoescalabackend.entities.PautaDeAudiencia;
import com.agu.gestaoescalabackend.entities.Procurador;
import com.agu.gestaoescalabackend.entities.TipoStatus;
import com.agu.gestaoescalabackend.repositories.MutiraoRepository;
import com.agu.gestaoescalabackend.repositories.PautaDeAudienciaRepository;
import com.agu.gestaoescalabackend.repositories.ProcuradorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MutiraoService {

	private MutiraoRepository repository;
	private PautaDeAudienciaRepository pautaRepository;
	private ProcuradorRepository procuradorRepository;

//////////////////////////////////   SERVIÇOS   ///////////////////////////////////

	@Transactional(readOnly = true)
	public List<MutiraoDTO> pesquisarTodos() {
		List<Mutirao> list = repository.findAllByOrderByIdAsc();
		return list.stream().map(x -> new MutiraoDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public MutiraoDTO pesquisarEspecifico(Long mutiraoId) {
		if (!repository.existsById(mutiraoId))
			return null;

		MutiraoDTO mutiraoDto = new MutiraoDTO(repository.getOne(mutiraoId));
		return mutiraoDto;
	}

	@Transactional(readOnly = true)
	public List<PautaDeAudienciaDTO> pesquisarPautasDoMutirao(Long mutiraoId) {
		List<PautaDeAudiencia> list = pautaRepository.findAllByMutiraoId(mutiraoId);
		return list.stream().map(x -> new PautaDeAudienciaDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public MutiraoDTO salvar(MutiraoDTO mutiraoDto) {

		if (!validarCriacao(mutiraoDto))
			return null;

		Mutirao mutirao = new Mutirao(mutiraoDto);
		mutirao = repository.save(mutirao);
		return new MutiraoDTO(mutirao);

	}

	@Transactional
	public MutiraoDTO editar(Long mutiraoId, MutiraoDTO mutiraoDto) {
		if (!repository.existsById(mutiraoId))
			return null;

		atualizarVaraPautas(mutiraoId, mutiraoDto.getVara());

		Mutirao mutirao = new Mutirao(mutiraoId, mutiraoDto);
		mutirao = repository.save(mutirao);
		return new MutiraoDTO(mutirao);

	}

	// USADO NO MÉTODO SALVAR DA PAUTA
	@Transactional
	public MutiraoDTO editar(Mutirao mutirao) {
		if (!repository.existsById(mutirao.getId()))
			return null;

		atualizarVaraPautas(mutirao.getId(), mutirao.getVara());

		mutirao = repository.save(mutirao);
		return new MutiraoDTO(mutirao);

	}

	@Transactional
	public void excluir(Long mutiraoId) {
		if (repository.existsById(mutiraoId))
			repository.deleteById(mutiraoId);
	}

	@Transactional
	public PautaDeAudienciaDTO atualizarProcurador(Long pautaDeAudienciaId, Long procuradorId) {

		if ((!pautaRepository.existsById(pautaDeAudienciaId)) || (!procuradorRepository.existsById(procuradorId)))
			return null;

		PautaDeAudiencia pautaDeAudiencia = pautaRepository.getOne(pautaDeAudienciaId);
		List<PautaDeAudiencia> listaPautaDoProcurador = 
				pautaRepository.findByDataAndSalaAndTurno(pautaDeAudiencia.getData(), pautaDeAudiencia.getSala(), 
						pautaDeAudiencia.getTurno());
		Procurador procuradorAntigo = procuradorRepository.getOne(pautaDeAudiencia.getProcurador().getId());
		Procurador procuradorNovo = procuradorRepository.getOne(procuradorId);

		for(int i = 0; i < listaPautaDoProcurador.size() ; i++) {
			pautaDeAudiencia = pautaRepository.getOne(listaPautaDoProcurador.get(i).getId());
			procuradorAntigo.setSaldo(procuradorAntigo.getSaldo() - 1);
			procuradorAntigo.setSaldoPeso(procuradorAntigo.getSaldo() * procuradorAntigo.getPeso());
			procuradorNovo.setSaldo(procuradorNovo.getSaldo() + 1);
			procuradorNovo.setSaldoPeso(procuradorNovo.getSaldo() * procuradorNovo.getPeso());
			pautaDeAudiencia.setProcurador(procuradorNovo);

			procuradorRepository.save(procuradorNovo);
			procuradorRepository.save(procuradorAntigo);
			pautaDeAudiencia = pautaRepository.save(pautaDeAudiencia);
		}

		return new PautaDeAudienciaDTO(pautaDeAudiencia);

	}

//////////////////////////////////    ESCALA    ///////////////////////////////////

	@Transactional
	public List<PautaDeAudiencia> gerarEscala(Long mutiraoId, String grupo) { // 24 linhas

		List<PautaDeAudiencia> listaPauta = pautaRepository.findAllByMutiraoId(mutiraoId);
		List<Procurador> listaProcurador = retornarListaDe("Procurador", "Ativo");
		List<Procurador> listaPreposto = retornarListaDe("Preposto", "Ativo");
		List<Procurador> listaPautista = procuradorRepository.findAllByStatusOrderBySaldoPesoAsc("Ativo");
		String salaAtual = listaPauta.get(0).getSala();
		LocalDate diaAtual = listaPauta.get(0).getData();
		String turnoAtual = listaPauta.get(0).getTurno();
		String salaDaPautaAtual = listaPauta.get(0).getSala();
		LocalDate diaDaPautaAtual = listaPauta.get(0).getData();
		String turnoDaPautaAtual = listaPauta.get(0).getTurno();
		String tipoDoUltimoPautistaInserido = "Nenhum";
		boolean repetiuPautista = false;
		
		for(Procurador lista : listaPautista) {
			System.out.println(lista.getNomeProcurador()+": "+lista.getSaldoPeso());
		}

		definirStatusMutiraoParaSemEscala(mutiraoId);

		// percorre a lista para inserir e salvar no banco o procurador
		for (int pautaAtual = 0; pautaAtual < listaPauta.size(); pautaAtual++) {
			// Atribuição para facilitar a legibilidade da condicional
			salaDaPautaAtual = listaPauta.get(pautaAtual).getSala();
			diaDaPautaAtual = listaPauta.get(pautaAtual).getData();
			turnoDaPautaAtual = listaPauta.get(pautaAtual).getTurno();

			// compara se a sala da lista que foi pego inicialmente é igual a sala da lista
			if ((salaAtual.equals(salaDaPautaAtual)) && (diaAtual.equals(diaDaPautaAtual)) 
					&& (turnoAtual.equals(turnoDaPautaAtual))) {
				System.out.println("Primeiro if");
				tipoDoUltimoPautistaInserido = validarInserçãoDePautista(listaPauta.get(pautaAtual), listaProcurador,
						listaPreposto,listaPautista, repetiuPautista, grupo);
				System.out.println("Voltou para o Primeiro if: "+ tipoDoUltimoPautistaInserido);

			} else {

				System.out.println("----------------------------------------");

				// Ordena apenas a lista dos procuradores
				if (tipoDoUltimoPautistaInserido.equals("Procurador")) {
					System.out.println("Else Procurador");
					repetiuPautista = reordenarPautista(listaProcurador, repetiuPautista,grupo);

					// Ordena apenas a lista dos prepostos
				} else if (tipoDoUltimoPautistaInserido.equals("Preposto")) {
					System.out.println("Else Preposto");
					repetiuPautista = reordenarPautista(listaPreposto, repetiuPautista, grupo);
					
				}else if (tipoDoUltimoPautistaInserido.equals("Todos")) {
					System.out.println("Else Todos");
					repetiuPautista = reordenarPautista(listaPautista, repetiuPautista, grupo);
					System.out.println("Voltou para Else Todos: repetiu pautista: "+ repetiuPautista);	
				}

				System.out.println("----------------------------------- ");

				// Atribui para a salaLista a sala corrente
				salaAtual = listaPauta.get(pautaAtual).getSala();
				diaAtual = listaPauta.get(pautaAtual).getData();
				turnoAtual = listaPauta.get(pautaAtual).getTurno();


				validarInserçãoDePautista(listaPauta.get(pautaAtual), listaProcurador, listaPreposto,listaPautista, repetiuPautista,grupo);

				if (repetiuPautista) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				}

			}
		}

		List<PautaDeAudiencia> pauta = pautaRepository.findAllByMutiraoId(mutiraoId);

		return pauta;
	}

//////////////////////////////////    MÉTODOS    ///////////////////////////////////

	private boolean reordenarPautista(List<Procurador> listaProcurador, boolean repetiuPautista, String grupo) {
		String nomeAntigo;
		int marcador = 0;

		if (repetiuPautista) {
			marcador = 1;
		}

		nomeAntigo = listaProcurador.get(marcador).getNomeProcurador();
		for(int i=0; i <listaProcurador.size(); i++) {
			System.out.println("Antigo: "+listaProcurador.get(i).getNomeProcurador()+": "+listaProcurador.get(i).getSaldoPeso());
		}

		// Reordena a lista
		Collections.sort(listaProcurador);
		for(int i=0; i <listaProcurador.size(); i++) {
			System.out.println("Novo: "+listaProcurador.get(i).getNomeProcurador()+": "+listaProcurador.get(i).getSaldoPeso());
		}

		// Verifica se o novo pautista é igual ao último antes da reordenação
		return (nomeAntigo.equals(listaProcurador.get(0).getNomeProcurador()));
	}

	private String validarInserçãoDePautista(PautaDeAudiencia pautaAtual, List<Procurador> listaProcurador,
			List<Procurador> listaPreposto,List<Procurador> listaPautista, boolean repetiuPautista, String grupo) {
		System.out.println("Validar");
		int marcador = 0;
		if (repetiuPautista) {
			marcador = 1;
		}
		// if (pautaAtual.getProcurador() == null) {
			if (grupo.equalsIgnoreCase("1")) {

				definirPautista(listaProcurador.get(marcador), pautaAtual);
				return "Procurador";
				
			} else if (grupo.equalsIgnoreCase("2")){

				definirPautista(listaPreposto.get(marcador), pautaAtual);
				return "Preposto";
				
			} else {
				System.out.println("ValidarIf3: marcador: " + marcador);
				definirPautista(listaPautista.get(marcador), pautaAtual);
				return "Todos";
			}
		//} return "Nenhum";
	}

	private boolean validarCriacao(MutiraoDTO mutiraoDto) {
		return (!repository.existsByVaraAndDataInicialAndDataFinal(mutiraoDto.getVara(), mutiraoDto.getDataInicial(),
				mutiraoDto.getDataFinal()))
				&& (mutiraoDto.getDataInicial() != null || mutiraoDto.getDataFinal() != null);
	}

	private void atualizarVaraPautas(Long mutiraoId, String vara) {

		if (repository.getOne(mutiraoId).getVara() != vara) {
			List<PautaDeAudiencia> pautaDeAudiencia = pautaRepository.findAllByMutiraoId(mutiraoId);
			pautaDeAudiencia.forEach(x -> x.setVara(vara));
		}

	}

	private List<Procurador> retornarListaDe(String grupo, String status) {
		return procuradorRepository.findAllByGrupoAndStatusOrderBySaldoPesoAsc(grupo, status);
	}

	private void definirStatusMutiraoParaSemEscala(Long mutiraoId) {
		Mutirao mutirao = repository.getOne(mutiraoId);
		mutirao.setStatus(TipoStatus.COM_ESCALA);
		repository.save(mutirao);
	}

	private void definirPautista(Procurador pautistaAtual, PautaDeAudiencia pautaAtual) {
		// Seta na pauta o procurador na posição especificada e incrementa seu saldo
		System.out.println("Definir Pautista");
		pautaAtual.setProcurador(pautistaAtual);
		pautistaAtual.setSaldo(pautistaAtual.getSaldo() + 1);
		pautistaAtual.setSaldoPeso(pautistaAtual.getSaldo() * pautistaAtual.getPeso());
		System.out.println(pautistaAtual.getNomeProcurador()+"= "+pautistaAtual.getSaldo()+"  "+ 
				pautistaAtual.getSaldoPeso());
		// Salva a pauta e o procurador com o saldo atualizado no banco
		procuradorRepository.save(pautistaAtual);
		pautaRepository.save(pautaAtual);
	}
	

}
