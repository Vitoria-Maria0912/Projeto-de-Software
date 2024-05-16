package com.ufcg.psoft.tccManager.service.professor;

import com.ufcg.psoft.tccManager.dto.professor.*;
import com.ufcg.psoft.tccManager.exception.*;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    AreaDeEstudoRepository areaDeEstudoRepository;

    @Autowired
    TemaTCCRepository temaTccRepository;

    @Autowired
    ModelMapper modelMapper;
    
    @Override
    public ProfessorResponseDTO criar(ProfessorPostPutRequestDTO professorPostPutRequestDTO, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        Professor professor = modelMapper.map(professorPostPutRequestDTO, Professor.class);
        professorRepository.save(professor);
        return modelMapper.map(professor, ProfessorResponseDTO.class);
    }


    @Override
    public ProfessorResponseDTO recuperar(UUID id, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        Professor professor = professorRepository.findById(id).orElseThrow(ProfessorNaoExisteException::new);
        return new ProfessorResponseDTO(professor);
    }

    @Override
    public List<ProfessorResponseDTO> listar(Perfil perfil) {
        List<Professor> professores = professorRepository.findAll();
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        return professores.stream()
                .map(ProfessorResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProfessorResponseDTO alterarProfessor(UUID id, ProfessorPostPutRequestDTO professorPostPutRequestDTO, Perfil perfil){
        Professor professor = professorRepository.findById(id). orElseThrow(ProfessorNaoExisteException::new);
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        modelMapper.map(professorPostPutRequestDTO, professor);
        if (professorPostPutRequestDTO.getLaboratorios().isEmpty()){
            throw new ProfessorSemLaboratoriosException();
        }
        professorRepository.save(professor);
        return modelMapper.map(professor, ProfessorResponseDTO.class);
    }

    @Override
    public void removerProfessor(UUID id, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        Professor professor = professorRepository.findById(id). orElseThrow(ProfessorNaoExisteException::new);
        professorRepository.delete(professor);
    }
    private boolean checaPerfil(Perfil perfil, Perfil perfilEsperado) {
        return (perfilEsperado.equals(perfil));
    }

    @Override
    public Set<ProfessorResponseNomeEmailDTO> listarProfessoresDisponiveis(Set<AreaDeEstudo> areasDoAluno) {
        List<Professor> professores = professorRepository.getProfessorWithOrientationQuota();
        Set<Professor> professoresDisponiveis = new HashSet<>();

        for (AreaDeEstudo area : areasDoAluno){
            for (Professor professor : professores){
                if (professor.getAreasDeInteresse().contains(area)) {
                    professoresDisponiveis.add(professor);
                }
            }
        }

        return professoresDisponiveis.stream()
                .map(professor -> modelMapper.map(professor, ProfessorResponseNomeEmailDTO.class))
                .collect(Collectors.toSet());
    }

    @Override
    public ProfessorResponseDTO atualizarQuota(UUID idProfessor, Integer novaQuota, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.PROFESSOR)) {
            throw new AcessoNegadoException();
        }

        Professor professor = professorRepository.getProfessorById(idProfessor);

        if (quotaValida(novaQuota))
            professor.setQuota(novaQuota);
        else
            throw new QuotaInvalidaException();

        return modelMapper.map(professor, ProfessorResponseDTO.class);
    }

    @Override
    public void diminuiQuotaEmUm(UUID idProfessor) {
        Professor professor = professorRepository.getProfessorById(idProfessor);

        Integer quotaAntiga = professor.getQuota();
        professor.setQuota(quotaAntiga-1);
    }

    private boolean quotaValida(Integer novaQuota) {
        return novaQuota >= 0;
    }

    @Override
    public void informarAreaDeInteresseProf(UUID idProf, Long idArea) {
        Optional<Professor> profOptional = professorRepository.findById(idProf);
        if(profOptional.isPresent()){
            Professor prof = profOptional.get();
            Optional<AreaDeEstudo> areaOp = areaDeEstudoRepository.findById(idArea);
            if(areaOp.isPresent()) {
                AreaDeEstudo area = areaOp.get();
                prof.getAreasDeInteresse().add(area);
                professorRepository.save(prof);
            } else {
                throw new AreaDeEstudoNaoExisteException();
            }
        } else {
            throw new ProfessorNaoExisteException();
        }
    }

    @Transactional
    @Override
    public void receberNotificacao(String tituloTema, String nomeAluno, String emailAluno, Professor professor) {
        System.out.println("------------------------------------------------------------------------------------------\n"
                + "| Professor " + professor.getNomeCompleto() + ", \n"
                + "| Voce tem uma nova notificacao!\n| \n"
                + "| Uma nova solicitação de orientação em tema de TCC foi criada \n"
                + "| No tema: " + tituloTema + "\n"
                + "| Pelo aluno: " + nomeAluno + "   Email:" + emailAluno + "\n"
                + "------------------------------------------------------------------------------------------");
    }


}

