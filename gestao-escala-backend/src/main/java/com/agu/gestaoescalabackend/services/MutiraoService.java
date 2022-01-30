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
import com.agu.gestaoescalabackend.enums.StatusPauta;
import com.agu.gestaoescalabackend.enums.GrupoPautista;
import com.agu.gestaoescalabackend.enums.StatusPautista;
import com.agu.gestaoescalabackend.enums.TurnoPauta;
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
	public List<PautaDto> atualizarProcurador(Long pautaDeAudienciaId, Long procuradorId) {

		if ((!pautaRepository.existsById(pautaDeAudienciaId)) || (!pautistaRepository.existsById(procuradorId)))
			return null;

		Pauta pautaDoPautista = pautaRepository.findById(pautaDeAudienciaId).get();
		List<Pauta> listaPautaDoPautista =
				pautaRepository.findByDataAndSalaAndTurnoPautaAndVara(pautaDoPautista.getData(), pautaDoPautista.getSala(),
						pautaDoPautista.getTurnoPauta(), pautaDoPautista.getVara());

		Pautista pautistaAntigo = pautistaRepository.findById(pautaDoPautista.getPautista().getId()).get();
		Pautista pautistaNovo = pautistaRepository.findById(procuradorId).get();

		for (Pauta pauta : listaPautaDoPautista) {
			pautistaAntigo.atualizarSaldo(-1);
			pautistaNovo.atualizarSaldo(1);
			pauta.setPautista(pautistaNovo);

			pautaRepository.save(pauta);
		}
		pautistaRepository.save(pautistaNovo);
		pautistaRepository.save(pautistaAntigo);

		return listaPautaDoPautista
				.stream()
				.map(Pauta::toDto)
				.collect(Collectors.toList());

	}

//////////////////////////////////    ESCALA    ///////////////////////////////////

	@Transactional
	public List<PautaDto> gerarEscala(Long mutiraoId, GrupoPautista grupoPautista) { // 24 linhas

		// INSTANCIA A LISTA DE OBJETOS
		List<Pauta> pautaList = pautaRepository.findAllByMutiraoId(mutiraoId);
		List<Pautista> procuradorList = retornarListaDe(
				GrupoPautista.PROCURADOR);
		List<Pautista> prepostoList = retornarListaDe(
				GrupoPautista.PREPOSTO);
		List<Pautista> pautistaList = pautistaRepository.findAllByStatusPautistaOrderBySaldoPesoAsc(
				StatusPautista.ATIVO);

		// ---------------
		String salaDaPautaAtual;
		LocalDate diaDaPautaAtual;
		TurnoPauta turnoPautaDaPautaAtual;

		// ----------------
		String tipoDoUltimoPautistaInserido = "Nenhum";
		boolean repetiuPautista = false;

		// CHECA O SALDO PESO
		System.out.println("CHECAR SALDO PESO");
		for(Pautista lista : pautistaList) {
			System.out.println(lista.getNome()+": "+lista.getSaldoPeso());
		}

		definirStatusMutiraoParaSemEscala(mutiraoId);

		// Inicializa as informações da pauta
		Pauta pautaVerificada = pautaList.get(0);

		// percorre a lista para inserir e salvar no banco o procurador
		for (Pauta pautaAtual : pautaList) {

			// Verifica se a sala, dia ou turno mudaram
			if (pautaVerificada.isTheSame(pautaAtual)) {

				tipoDoUltimoPautistaInserido = validarInserçãoDePautista(pautaAtual, procuradorList,
						prepostoList, pautistaList, repetiuPautista, grupoPautista);

			} else {

				System.out.println("----------------------------------------");

				// Ordena apenas a lista dos procuradores
				switch (tipoDoUltimoPautistaInserido) {
					case "Procurador":
						System.out.println("Else Procurador");
						repetiuPautista = reordenarPautista(procuradorList, repetiuPautista, grupoPautista);

						// Ordena apenas a lista dos prepostos
						break;
					case "Preposto":
						System.out.println("Else Preposto");
						repetiuPautista = reordenarPautista(prepostoList, repetiuPautista, grupoPautista);

						break;
					case "Todos":
						System.out.println("Else Todos");
						repetiuPautista = reordenarPautista(pautistaList, repetiuPautista, grupoPautista);
						System.out.println("Voltou para Else Todos: repetiu pautista: " + repetiuPautista);
						break;
				}

				System.out.println("----------------------------------- ");

				// Atribui para a salaLista a sala corrente
				pautaVerificada = pautaAtual;


				validarInserçãoDePautista(pautaAtual, procuradorList, prepostoList, pautistaList, repetiuPautista, grupoPautista);

				if (repetiuPautista) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				}

			}
		}

		return pautaRepository.findAllByMutiraoId(mutiraoId)
				.stream()
				.map(Pauta::toDto)
				.collect(Collectors.toList());
	}

//////////////////////////////////    MÉTODOS    ///////////////////////////////////

	private boolean reordenarPautista(List<Pautista> listaPautista, boolean repetiuPautista, GrupoPautista grupoPautista) {
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
											 List<Pautista> listaPreposto, List<Pautista> listaPautista, boolean repetiuPautista, GrupoPautista grupoPautista) {

		// O MARCADOR SERVE PARA PEGAR O PRÓXIMO PAUTISTA, CASO IDENTIFIQUE QUE O PAUTISTA REPETIU

		int pautistaIndex = 0;
		if (repetiuPautista) {
			pautistaIndex = 1;
		}
			if (grupoPautista.equals(GrupoPautista.PROCURADOR)) {
				definirPautista(listaProcurador.get(pautistaIndex), pautaAtual);
				return "Procurador";
				
			} else if (grupoPautista.equals(GrupoPautista.PREPOSTO)){

				definirPautista(listaPreposto.get(pautistaIndex), pautaAtual);
				return "Preposto";
				
			} else {
				definirPautista(listaPautista.get(pautistaIndex), pautaAtual);
				return "Todos";
			}
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

	private List<Pautista> retornarListaDe(GrupoPautista grupoPautista) {
		return pautistaRepository.findAllByGrupoPautistaAndStatusPautistaOrderBySaldoPesoAsc(grupoPautista, StatusPautista.ATIVO);
	}

	private void definirStatusMutiraoParaSemEscala(Long mutiraoId) {
		Mutirao mutirao = mutiraoRepository.findById(mutiraoId).get();
		mutirao.setStatusPauta(StatusPauta.COM_ESCALA);
		mutiraoRepository.save(mutirao);
	}

	private void definirPautista(Pautista pautistaAtual, Pauta pautaAtual) {
		// Seta na pauta o procurador na posição especificada e incrementa seu saldo

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
