package com.agu.gestaoescalabackend.services;

import com.agu.gestaoescalabackend.dto.MutiraoDTO;
import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.entities.Mutirao;
import com.agu.gestaoescalabackend.entities.Pauta;
import com.agu.gestaoescalabackend.entities.Pautista;
import com.agu.gestaoescalabackend.entities.TipoStatus;
import com.agu.gestaoescalabackend.repositories.MutiraoRepository;
import com.agu.gestaoescalabackend.repositories.PautaRepository;
import com.agu.gestaoescalabackend.repositories.PautistaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PautaService {

	private PautaRepository pautaRepository;
	private PautistaRepository pautistaRepository;
	private MutiraoRepository mutiraoRepository;
	private MutiraoService mutiraoService;

//////////////////////////////////   SERVIÇOS   ///////////////////////////////////

	public List<PautaDto> findAll() {

		return pautaRepository.findAllByOrderByIdAsc()
				.stream()
				.map(Pauta::toDto)
				.collect(Collectors.toList());
	}

	public PautaDto findById(Long id) {

		if (!pautaRepository.existsById(id))
			return null;

		return pautaRepository.findById(id).get().toDto();
	}

	@Transactional
	public List<PautaDto> save(List<PautaDto> listaPautaDto) {
		
		Mutirao mutirao = retornarMutirao(listaPautaDto);
		
		List<PautaDto> listaRetorno = new ArrayList<>();

		for (int pautaAtual = 0; pautaAtual < listaPautaDto.size(); pautaAtual++) {

			Pauta pauta = listaPautaDto.get(pautaAtual).toEntity();

			pauta.setMutirao(mutirao);
			inserirProcurador(listaPautaDto.get(pautaAtual), pauta);

			if (validarCriacao(listaPautaDto.get(pautaAtual), pauta)) {
				
				mutirao.setQuantidaDePautas(mutirao.getQuantidaDePautas() + 1);
				pauta = pautaRepository.save(pauta);
				PautaDto pautaDto = pauta.toDto();
				listaRetorno.add(pautaDto);
			}
		}
		mutiraoService.editar(mutirao);
		return listaRetorno;

	}

	@Transactional
	public PautaDto editar(Long pautaDeAudienciaId, PautaDto pautaDto) {

		List<Mutirao> mutirao = mutiraoRepository.findByVara(pautaDto.getVara());

		if (!pautaRepository.existsById(pautaDeAudienciaId))
			return null;
		
		Pauta pauta = new Pauta(pautaDeAudienciaId, pautaDto);
		inserirMutirao(pautaDto, mutirao, pauta);
		inserirProcurador(pautaDto, pauta);

		pauta = pautaRepository.save(pauta);
		return pauta.toDto();

	}

	@Transactional
	public void excluir(Long pautaDeAudienciaId) {
		if (pautaRepository.existsById(pautaDeAudienciaId))
			pautaRepository.deleteById(pautaDeAudienciaId);
	}

	/*------------------------------------------------
     METODOS DO MUTIRAO
    ------------------------------------------------*/

	private Mutirao retornarMutirao(List<PautaDto> listaPautaDto) {

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

	private void inserirProcurador(PautaDto pautaDto, Pauta pauta) {
		// Verifica se no Repositório há um procurador com o nome passado pelo DTO
		if (pautistaRepository.existsByNome(pautaDto.getProcurador().getNome())) {
			// Atribui ao objeto o procurador encontrado anteriormente
			Pautista pautista = pautistaRepository
					.findByNome(pautaDto.getProcurador().getNome());
			// Seta na pauta o procurador
			pauta.setPautista(pautista);
		} else {
			// Seta nulo se não for encontrado referência para o nome do dto
			pauta.setPautista(null);
		}

	}

	public void inserirMutirao(PautaDto pautaDto, List<Mutirao> mutirao,
							   Pauta pauta) {
		int x = 0;
		// Faça enquanto estiver dentro do tamanho do multirão (OU) enquanto o multirão
		// for nulo
		while ((x < mutirao.size()) || (mutirao == null)) {

			LocalDate dataInicialMutirao = mutirao.get(x).getDataInicial().minusDays(1);
			LocalDate dataFinalMutirao = mutirao.get(x).getDataFinal().plusDays(1);

			if (dataInicialMutirao.isBefore(pauta.getData())
					&& dataFinalMutirao.isAfter(pauta.getData())) {
				// Se a condição das datas for verdadeira, seta na pauta o multirão corrente
				pauta.setMutirao(mutirao.get(x));
				break;

			}
			x++;
		}
	}

	private boolean validarCriacao(PautaDto pautaDto, Pauta pauta) {
		// Instancia um objeto base para verificar se já existe um registro 'nome'
		// no banco igual ao do DTO | OU | se não há algum multirão válido para a pauta
		Pauta pautaExistente = pautaRepository.findByProcessoAndTipo(pautaDto.getProcesso(),
				pautaDto.getTipo());
		if ((pautaExistente != null && !pautaExistente.equals(pauta))
				|| (pauta.getMutirao() == null)) {
			return false;
		} else
			return true;
	}

	public boolean validarCriacaoMutirao(List<PautaDto> dto) {
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
