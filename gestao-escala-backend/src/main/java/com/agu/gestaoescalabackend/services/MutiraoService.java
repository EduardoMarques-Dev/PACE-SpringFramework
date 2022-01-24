/**
 * @author Carlos Eduardo
 * A classe <b>Mutirao Service</b> é utilizada para tratar das regras de negócio relacionadas à entidade <b>Mutirao</b>. 
*/
package com.agu.gestaoescalabackend.services;

import com.agu.gestaoescalabackend.dto.MutiraoDTO;
import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.entities.Mutirao;
import com.agu.gestaoescalabackend.entities.Pauta;
import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.entities.TipoStatus;
import com.agu.gestaoescalabackend.enums.Grupo;
import com.agu.gestaoescalabackend.enums.Status;
import com.agu.gestaoescalabackend.repositories.MutiraoRepository;
import com.agu.gestaoescalabackend.repositories.PautaRepository;
import com.agu.gestaoescalabackend.repositories.PautistaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MutiraoService {

	private MutiraoRepository mutiraoRepository;
	private PautaRepository pautaRepository;
	private PautistaRepository pautistaRepository;

//////////////////////////////////   SERVIÇOS   ///////////////////////////////////

	@Transactional(readOnly = true)
	public List<MutiraoDTO> findAll() {

		return mutiraoRepository.findAllByOrderByIdAsc()
				.stream()
				.map(Mutirao::toDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public MutiraoDTO findById(Long id) {

		return mutiraoRepository.findById(id)
				.map(Mutirao::toDto)
				.orElse(null);
	}

	@Transactional(readOnly = true)
	public List<PautaDto> findPautas(Long mutiraoId) {

		return  pautaRepository.findAllByMutiraoId(mutiraoId)
				.stream()
				.map(Pauta::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public MutiraoDTO save(MutiraoDTO mutiraoDto) {

		if (!validarCriacao(mutiraoDto))
			return null;

		Mutirao mutirao = mutiraoDto.toEntity();
		return mutiraoRepository.save(mutirao).toDto();
	}

	@Transactional
	public MutiraoDTO update(Long mutiraoId, MutiraoDTO mutiraoDto) {

		if (!mutiraoRepository.existsById(mutiraoId))
			return null;

		atualizarVaraPautas(mutiraoId, mutiraoDto.getVara());

		Mutirao mutirao = mutiraoDto.toEntity().forUpdate(mutiraoId);
		return mutiraoRepository.save(mutirao).toDto();
	}

	@Transactional
	public void excluir(Long mutiraoId) {
		if (mutiraoRepository.existsById(mutiraoId))
			mutiraoRepository.deleteById(mutiraoId);
	}

	/*------------------------------------------------
     METODOS DE NEGÓCIO
    ------------------------------------------------*/

	@Transactional
	public PautaDto atualizarProcurador(Long pautaDeAudienciaId, Long procuradorId) {

		if ((!pautaRepository.existsById(pautaDeAudienciaId)) || (!pautistaRepository.existsById(procuradorId)))
			return null;

		Pauta pauta = pautaRepository.findById(pautaDeAudienciaId).get();
		List<Pauta> listaPautaDoProcurador =
				pautaRepository.findByDataAndSalaAndTurno(pauta.getData(), pauta.getSala(),
						pauta.getTurno().toString());
		Pautista pautistaAntigo = pautistaRepository.findById(pauta.getPautista().getId()).get();
		Pautista pautistaNovo = pautistaRepository.findById(procuradorId).get();

		for (Pauta value : listaPautaDoProcurador) {
			pauta = pautaRepository.findById(value.getId()).get();
			pautistaAntigo.setSaldo(pautistaAntigo.getSaldo() - 1);
			pautistaAntigo.setSaldoPeso(pautistaAntigo.getSaldo() * pautistaAntigo.getPeso());
			pautistaNovo.setSaldo(pautistaNovo.getSaldo() + 1);
			pautistaNovo.setSaldoPeso(pautistaNovo.getSaldo() * pautistaNovo.getPeso());
			pauta.setPautista(pautistaNovo);

			pautistaRepository.save(pautistaNovo);
			pautistaRepository.save(pautistaAntigo);
			pauta = pautaRepository.save(pauta);
		}

		return pauta.toDto();

	}

//////////////////////////////////    ESCALA    ///////////////////////////////////

	@Transactional
	public List<Pauta> gerarEscala(Long mutiraoId, String grupo) { // 24 linhas

		List<Pauta> listaPauta = pautaRepository.findAllByMutiraoId(mutiraoId);
		List<Pautista> listaProcurador = retornarListaDe(
				Grupo.PROCURADOR);
		List<Pautista> listaPreposto = retornarListaDe(
				Grupo.PREPOSTO);
		List<Pautista> listaPautista = pautistaRepository.findAllByStatusOrderBySaldoPesoAsc(
				Status.ATIVO);
		String salaAtual = listaPauta.get(0).getSala();
		LocalDate diaAtual = listaPauta.get(0).getData();
		String turnoAtual = listaPauta.get(0).getTurno().toString();
		String salaDaPautaAtual = listaPauta.get(0).getSala();
		LocalDate diaDaPautaAtual = listaPauta.get(0).getData();
		String turnoDaPautaAtual = listaPauta.get(0).getTurno().toString();
		String tipoDoUltimoPautistaInserido = "Nenhum";
		boolean repetiuPautista = false;
		
		for(Pautista lista : listaPautista) {
			System.out.println(lista.getNome()+": "+lista.getSaldoPeso());
		}

		definirStatusMutiraoParaSemEscala(mutiraoId);

		// percorre a lista para inserir e salvar no banco o procurador
		for (Pauta value : listaPauta) {
			// Atribuição para facilitar a legibilidade da condicional
			salaDaPautaAtual = value.getSala();
			diaDaPautaAtual = value.getData();
			turnoDaPautaAtual = value.getTurno().toString();

			// compara se a sala da lista que foi pego inicialmente é igual a sala da lista
			if ((salaAtual.equals(salaDaPautaAtual)) && (diaAtual.equals(diaDaPautaAtual))
					&& (turnoAtual.equals(turnoDaPautaAtual))) {
				System.out.println("Primeiro if");
				tipoDoUltimoPautistaInserido = validarInserçãoDePautista(value, listaProcurador,
						listaPreposto, listaPautista, repetiuPautista, grupo);
				System.out.println("Voltou para o Primeiro if: " + tipoDoUltimoPautistaInserido);

			} else {

				System.out.println("----------------------------------------");

				// Ordena apenas a lista dos procuradores
				switch (tipoDoUltimoPautistaInserido) {
					case "Procurador":
						System.out.println("Else Procurador");
						repetiuPautista = reordenarPautista(listaProcurador, repetiuPautista, grupo);

						// Ordena apenas a lista dos prepostos
						break;
					case "Preposto":
						System.out.println("Else Preposto");
						repetiuPautista = reordenarPautista(listaPreposto, repetiuPautista, grupo);

						break;
					case "Todos":
						System.out.println("Else Todos");
						repetiuPautista = reordenarPautista(listaPautista, repetiuPautista, grupo);
						System.out.println("Voltou para Else Todos: repetiu pautista: " + repetiuPautista);
						break;
				}

				System.out.println("----------------------------------- ");

				// Atribui para a salaLista a sala corrente
				salaAtual = value.getSala();
				diaAtual = value.getData();
				turnoAtual = value.getTurno().toString();


				validarInserçãoDePautista(value, listaProcurador, listaPreposto, listaPautista, repetiuPautista, grupo);

				if (repetiuPautista) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				}

			}
		}

		return pautaRepository.findAllByMutiraoId(mutiraoId);
	}

//////////////////////////////////    MÉTODOS    ///////////////////////////////////

	private boolean reordenarPautista(List<Pautista> listaPautista, boolean repetiuPautista, String grupo) {
		String nomeAntigo;
		int marcador = 0;

		if (repetiuPautista) {
			marcador = 1;
		}

		nomeAntigo = listaPautista.get(marcador).getNome();
		for (Pautista value : listaPautista) {
			System.out.println("Antigo: " + value.getNome() + ": " + value.getSaldoPeso());
		}

		// Reordena a lista
		Collections.sort(listaPautista);
		for (Pautista pautista : listaPautista) {
			System.out.println("Novo: " + pautista.getNome() + ": " + pautista.getSaldoPeso());
		}

		// Verifica se o novo pautista é igual ao último antes da reordenação
		return (nomeAntigo.equals(listaPautista.get(0).getNome()));
	}

	private String validarInserçãoDePautista(Pauta pautaAtual, List<Pautista> listaProcurador,
											 List<Pautista> listaPreposto, List<Pautista> listaPautista, boolean repetiuPautista, String grupo) {
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
		return (!mutiraoRepository.existsByVaraAndDataInicialAndDataFinal(mutiraoDto.getVara(), mutiraoDto.getDataInicial(),
				mutiraoDto.getDataFinal()))
				&& (mutiraoDto.getDataInicial() != null || mutiraoDto.getDataFinal() != null);
	}

	private void atualizarVaraPautas(Long mutiraoId, String vara) {

		if (mutiraoRepository.findById(mutiraoId).get().getVara() != vara) {
			List<Pauta> pauta = pautaRepository.findAllByMutiraoId(mutiraoId);
			pauta.forEach(x -> x.setVara(vara));
		}

	}

	private List<Pautista> retornarListaDe(Grupo grupo) {
		return pautistaRepository.findAllByGrupoAndStatusOrderBySaldoPesoAsc(grupo, Status.ATIVO);
	}

	private void definirStatusMutiraoParaSemEscala(Long mutiraoId) {
		Mutirao mutirao = mutiraoRepository.findById(mutiraoId).get();
		mutirao.setStatus(TipoStatus.COM_ESCALA);
		mutiraoRepository.save(mutirao);
	}

	private void definirPautista(Pautista pautistaAtual, Pauta pautaAtual) {
		// Seta na pauta o procurador na posição especificada e incrementa seu saldo
		System.out.println("Definir Pautista");
		pautaAtual.setPautista(pautistaAtual);
		pautistaAtual.setSaldo(pautistaAtual.getSaldo() + 1);
		pautistaAtual.setSaldoPeso(pautistaAtual.getSaldo() * pautistaAtual.getPeso());
		System.out.println(pautistaAtual.getNome()+"= "+pautistaAtual.getSaldo()+"  "+
				pautistaAtual.getSaldoPeso());
		// Salva a pauta e o procurador com o saldo atualizado no banco
		pautistaRepository.save(pautistaAtual);
		pautaRepository.save(pautaAtual);
	}
	

}
