package com.agu.gestaoescalabackend.services;

import com.agu.gestaoescalabackend.dto.MutiraoDTO;
import com.agu.gestaoescalabackend.dto.PautaDto;
import com.agu.gestaoescalabackend.entities.Mutirao;
import com.agu.gestaoescalabackend.entities.Pauta;
import com.agu.gestaoescalabackend.enums.StatusPauta;
import com.agu.gestaoescalabackend.repositories.MutiraoRepository;
import com.agu.gestaoescalabackend.repositories.PautaRepository;
import com.agu.gestaoescalabackend.repositories.PautistaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PautaService {

	private PautaRepository pautaRepository;
	private PautistaRepository pautistaRepository;
	private MutiraoRepository mutiraoRepository;
	private MutiraoService mutiraoService;

//////////////////////////////////   SERVIÇOS   ///////////////////////////////////

	@Transactional(readOnly = true)
	public List<PautaDto> findAll() {

		return pautaRepository.findAllByOrderByIdAsc()
				.stream()
				.map(Pauta::toDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PautaDto findById(Long id) {

		return pautaRepository.findById(id)
				.map(Pauta::toDto)
				.orElse(null);
	}

	@Transactional
	public List<PautaDto> save(List<PautaDto> listaPautaDto) {
		
		Mutirao mutirao = retornarMutirao(listaPautaDto);
		
		List<PautaDto> listaRetorno = new ArrayList<>();

		for (PautaDto dto : listaPautaDto) {

			Pauta pauta = dto.toEntity();

			pauta.setMutirao(mutirao);

			if (validarCriacao(dto, pauta)) {

				mutirao.setQuantidaDePautas(mutirao.getQuantidaDePautas() + 1);
				PautaDto pautaDto =pautaRepository.save(pauta).toDto();
				listaRetorno.add(pautaDto);
			}
		}
		mutiraoService.update(mutirao.getId(),mutirao.toDto());
		return listaRetorno;

	}

	@Transactional
	public PautaDto editar(Long pautaDeAudienciaId, PautaDto pautaDto) {

		List<Mutirao> mutirao = mutiraoRepository.findByVara(pautaDto.getVara());

		Optional<Pauta> pautaOptional = pautaRepository.findById(pautaDeAudienciaId);

		if (pautaOptional.isEmpty())
			return null;
		
		Pauta pauta = pautaOptional.get().forUpdate(pautaDto);
		inserirMutirao(mutirao, pauta);

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

	public boolean validarCriacaoMutirao(List<PautaDto> pautaDtoList) {
		// O objetivo deste método é verificar se alguma das datas passadas pelo DTO se
		// encaixam em algum multirão, e se encaixarem, retorna falso para que não seja
		// criado um novo multirão
		LocalDate dataInicialPauta = pautaDtoList.get(0).getData();
		LocalDate dataFinalPauta = pautaDtoList.get(pautaDtoList.size() - 1).getData();
		List<Mutirao> mutiraoList = mutiraoRepository.findByVara(pautaDtoList.get(0).getVara());

		for (Mutirao mutirao : mutiraoList) {

			LocalDate dataInicialMutirao = mutirao.getDataInicial().minusDays(1);
			LocalDate dataFinalMutirao = mutirao.getDataFinal().plusDays(1);

			if (dataInicialMutirao.isBefore(dataInicialPauta) && dataFinalMutirao.isAfter(dataInicialPauta)
					|| dataInicialMutirao.isBefore(dataFinalPauta) && dataFinalMutirao.isAfter(dataFinalPauta))
				return false;
		}
		return true;
	}

	private Mutirao retornarMutirao(List<PautaDto> listaPautaDto) {

		MutiraoDTO mutiraoDto = new MutiraoDTO();

		// Se a criação do mutirão for válida, retornará o mutirao criado.
		if (validarCriacaoMutirao(listaPautaDto)) {

			mutiraoDto = mutiraoDto.forSave(listaPautaDto);
			mutiraoDto.setStatusPauta(StatusPauta.SEM_ESCALA);
			mutiraoDto.setQuantidaDePautas(0);

			return mutiraoService.save(mutiraoDto).toEntity();

		} else {
			// Se a criação do mutirão não for válida, buscará dentre os mutirões qual o
			// adequado
			List<Mutirao> mutiraoList = mutiraoRepository.findByVara(listaPautaDto.get(0).getVara());
			LocalDate dataInicialPauta = listaPautaDto.get(0).getData();
			LocalDate dataFinalPauta = listaPautaDto.get(listaPautaDto.size() - 1).getData();
			int x = 0;

			for (Mutirao mutirao : mutiraoList) {

				LocalDate dataInicialMutirao = mutirao.getDataInicial().minusDays(1);
				LocalDate dataFinalMutirao = mutirao.getDataFinal().plusDays(1);

				if (dataInicialMutirao.isBefore(dataInicialPauta) && dataFinalMutirao.isAfter(dataFinalPauta)) {
					// Se a condição das datas for verdadeira, retorna o mutirao encontrado

					if (mutirao.getStatusPauta() == StatusPauta.COM_ESCALA)
						mutirao.setStatusPauta(StatusPauta.PARCIAL_ESCALA);

					return mutiraoService.update(mutirao.getId(), mutirao.toDto()).toEntity();
				}
			}
		}
		return null;
	}

	public void inserirMutirao(List<Mutirao> mutirao,
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
		Pauta pautaExistente = pautaRepository.findByProcessoAndTipoPauta(pautaDto.getProcesso(),
				pautaDto.getTipoPauta());
		return (pautaExistente == null || pautaExistente.equals(pauta))
				&& (pauta.getMutirao() != null);
	}
}
