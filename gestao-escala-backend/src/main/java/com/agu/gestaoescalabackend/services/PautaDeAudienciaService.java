package com.agu.gestaoescalabackend.services;

import java.time.LocalDate;
import java.util.ArrayList;
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
public class PautaDeAudienciaService {

	private PautaDeAudienciaRepository repository;
	private ProcuradorRepository procuradorRepository;
	private MutiraoRepository mutiraoRepository;
	private MutiraoService mutiraoService;

//////////////////////////////////   SERVIÇOS   ///////////////////////////////////

	@Transactional(readOnly = true)
	public List<PautaDeAudienciaDTO> pesquisarTodos() {
		List<PautaDeAudiencia> list = repository.findAllByOrderByIdAsc();
		return list.stream().map(x -> new PautaDeAudienciaDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PautaDeAudienciaDTO pesquisarEspecifico(Long pautaDeAudienciaId) {

		if (!repository.existsById(pautaDeAudienciaId))
			return null;

		PautaDeAudienciaDTO pautaDto = new PautaDeAudienciaDTO(repository.getOne(pautaDeAudienciaId));
		return pautaDto;
	}

	@Transactional
	public List<PautaDeAudienciaDTO> salvar(List<PautaDeAudienciaDTO> listaPautaDto) {
		
		Mutirao mutirao = retornarMutirao(listaPautaDto);
		
		List<PautaDeAudienciaDTO> listaRetorno = new ArrayList<>();

		for (int pautaAtual = 0; pautaAtual < listaPautaDto.size(); pautaAtual++) {

			PautaDeAudiencia pautaDeAudiencia = new PautaDeAudiencia(listaPautaDto.get(pautaAtual));

			pautaDeAudiencia.setMutirao(mutirao);
			inserirProcurador(listaPautaDto.get(pautaAtual), pautaDeAudiencia);

			if (validarCriacao(listaPautaDto.get(pautaAtual), pautaDeAudiencia)) {
				
				mutirao.setQuantidaDePautas(mutirao.getQuantidaDePautas() + 1);
				pautaDeAudiencia = repository.save(pautaDeAudiencia);
				PautaDeAudienciaDTO pautaDeAudienciaDto = new PautaDeAudienciaDTO(pautaDeAudiencia);
				listaRetorno.add(pautaDeAudienciaDto);
			}
		}
		mutiraoService.editar(mutirao);
		return listaRetorno;

	}

	@Transactional
	public PautaDeAudienciaDTO editar(Long pautaDeAudienciaId, PautaDeAudienciaDTO pautaDeAudienciaDto) {

		List<Mutirao> mutirao = mutiraoRepository.findByVara(pautaDeAudienciaDto.getVara());

		if (!repository.existsById(pautaDeAudienciaId))
			return null;
		
		PautaDeAudiencia pautaDeAudiencia = new PautaDeAudiencia(pautaDeAudienciaId, pautaDeAudienciaDto);
		inserirMutirao(pautaDeAudienciaDto, mutirao, pautaDeAudiencia);
		inserirProcurador(pautaDeAudienciaDto, pautaDeAudiencia);

		pautaDeAudiencia = repository.save(pautaDeAudiencia);
		return new PautaDeAudienciaDTO(pautaDeAudiencia);

	}

	@Transactional
	public void excluir(Long pautaDeAudienciaId) {
		if (repository.existsById(pautaDeAudienciaId))
			repository.deleteById(pautaDeAudienciaId);
	}

//////////////////////////////////    MÉTODOS    ///////////////////////////////////

	private void inserirProcurador(PautaDeAudienciaDTO pautaDeAudienciaDto, PautaDeAudiencia pautaDeAudiencia) {
		// Verifica se no Repositório há um procurador com o nome passado pelo DTO
		if (procuradorRepository.existsByNomeProcurador(pautaDeAudienciaDto.getProcurador().getNomeProcurador())) {
			// Atribui ao objeto o procurador encontrado anteriormente
			Procurador procurador = procuradorRepository
					.findByNomeProcurador(pautaDeAudienciaDto.getProcurador().getNomeProcurador());
			// Seta na pauta o procurador
			pautaDeAudiencia.setProcurador(procurador);
		} else {
			// Seta nulo se não for encontrado referência para o nome do dto
			pautaDeAudiencia.setProcurador(null);
		}

	}

	public void inserirMutirao(PautaDeAudienciaDTO pautaDeAudienciaDto, List<Mutirao> mutirao,
			PautaDeAudiencia pautaDeAudiencia) {
		int x = 0;
		// Faça enquanto estiver dentro do tamanho do multirão (OU) enquanto o multirão
		// for nulo
		while ((x < mutirao.size()) || (mutirao == null)) {

			LocalDate dataInicialMutirao = mutirao.get(x).getDataInicial().minusDays(1);
			LocalDate dataFinalMutirao = mutirao.get(x).getDataFinal().plusDays(1);

			if (dataInicialMutirao.isBefore(pautaDeAudiencia.getData())
					&& dataFinalMutirao.isAfter(pautaDeAudiencia.getData())) {
				// Se a condição das datas for verdadeira, seta na pauta o multirão corrente
				pautaDeAudiencia.setMutirao(mutirao.get(x));
				break;

			}
			x++;
		}
	}

	private boolean validarCriacao(PautaDeAudienciaDTO pautaDeAudienciaDto, PautaDeAudiencia pautaDeAudiencia) {
		// Instancia um objeto base para verificar se já existe um registro 'nome'
		// no banco igual ao do DTO | OU | se não há algum multirão válido para a pauta
		PautaDeAudiencia pautaExistente = repository.findByProcessoAndTipo(pautaDeAudienciaDto.getProcesso(),
				pautaDeAudienciaDto.getTipo());
		if ((pautaExistente != null && !pautaExistente.equals(pautaDeAudiencia))
				|| (pautaDeAudiencia.getMutirao() == null)) {
			return false;
		} else
			return true;
	}

	public boolean validarCriacaoMutirao(List<PautaDeAudienciaDTO> dto) {
		// O objetivo deste método é verificar se alguma das datas passadas pelo DTO se
		// encaixam em algum multirão, e se encaixarem, retorna falso para que não seja
		// criado um novo multirão
		LocalDate dataInicialPauta = dto.get(0).getData();
		LocalDate dataFinalPauta = dto.get(dto.size() - 1).getData();
		List<Mutirao> mutirao = mutiraoRepository.findByVara(dto.get(0).getVara());
		int x = 0;

		while ((x < mutirao.size())) {

			LocalDate dataInicialMutirao = mutirao.get(x).getDataInicial().minusDays(1);
			LocalDate dataFinalMutirao = mutirao.get(x).getDataFinal().plusDays(1);

			if (((dataInicialMutirao.isBefore(dataInicialPauta)) && (dataFinalMutirao.isAfter(dataInicialPauta)))
					|| ((dataInicialMutirao.isBefore(dataFinalPauta)) && (dataFinalMutirao.isAfter(dataFinalPauta))))
				return false;
			x++;
		}
		return true;
	}

	private Mutirao retornarMutirao(List<PautaDeAudienciaDTO> listaPautaDto) {
		MutiraoDTO mutiraoDto;

		// Se a criação do mutirão for válida, retornará o mutirao criado.
		if (validarCriacaoMutirao(listaPautaDto)) {
			mutiraoDto = new MutiraoDTO(listaPautaDto);
			mutiraoDto.setStatus(TipoStatus.SEM_ESCALA);
			mutiraoDto.setQuantidaDePautas(0);
			return new Mutirao(mutiraoService.salvar(mutiraoDto));
		} else {
			// Se a criação do mutirão não for válida, buscará dentre os mutirões qual o
			// adequado
			List<Mutirao> listaMutirao = mutiraoRepository.findByVara(listaPautaDto.get(0).getVara());
			LocalDate dataInicialPauta = listaPautaDto.get(0).getData();
			LocalDate dataFinalPauta = listaPautaDto.get(listaPautaDto.size() - 1).getData();
			int x = 0;

			while ((x < listaMutirao.size())) {

				LocalDate dataInicialMutirao = listaMutirao.get(x).getDataInicial().minusDays(1);
				LocalDate dataFinalMutirao = listaMutirao.get(x).getDataFinal().plusDays(1);

				if (dataInicialMutirao.isBefore(dataInicialPauta) && dataFinalMutirao.isAfter(dataFinalPauta)) {
					// Se a condição das datas for verdadeira, retorna o mutirao encontrado
					mutiraoDto = new MutiraoDTO(listaMutirao.get(x));
					if (listaMutirao.get(x).getStatus() == TipoStatus.COM_ESCALA)
						mutiraoDto.setStatus(TipoStatus.PARCIAL_ESCALA);
					return new Mutirao(mutiraoService.editar(listaMutirao.get(x).getId(), mutiraoDto));
				}
				x++;
			}
		}
		return null;
	}

//	@Transactional
//	public List<PautaDeAudienciaDTO> salvar(List<PautaDeAudienciaDTO> listaPautaDto) {
//		
//		boolean mutiraoCriado = false;
//		
//		if (validarCriacaoMutirao(listaPautaDto)) {
//			MutiraoDTO mutiraoDto = new MutiraoDTO(listaPautaDto);
//			mutiraoService.salvar(mutiraoDto);
//			mutiraoCriado = true;
//		}
//		
//		List<Mutirao> mutirao = mutiraoRepository.findByVara(listaPautaDto.get(0).getVara());
//		List<PautaDeAudienciaDTO> listaRetorno = new ArrayList<>();
//		
//		if (!mutirao.isEmpty()) {
//			
//			for (int pautaAtual = 0; pautaAtual < listaPautaDto.size(); pautaAtual++) {
//				
//				PautaDeAudiencia pautaDeAudiencia = new PautaDeAudiencia(listaPautaDto.get(pautaAtual));
//				inserirMutirao(listaPautaDto.get(pautaAtual), mutirao, pautaDeAudiencia);
//				inserirProcurador(listaPautaDto.get(pautaAtual), pautaDeAudiencia);
//				
//				if (validarCriacaoPauta(listaPautaDto.get(pautaAtual), pautaDeAudiencia)) {
//					pautaDeAudiencia = repository.save(pautaDeAudiencia);
//					PautaDeAudienciaDTO pautaDeAudienciaDto = new PautaDeAudienciaDTO(pautaDeAudiencia);
//					listaRetorno.add(pautaDeAudienciaDto);
//				}
//			}
//			return listaRetorno;
//		}
//		return null;
//		
//	}

}
