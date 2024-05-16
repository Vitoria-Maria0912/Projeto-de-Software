package com.ufcg.psoft.tccManager.service.aluno;

import com.ufcg.psoft.tccManager.dto.professor.ProfessorResponseNomeEmailDTO;
import com.ufcg.psoft.tccManager.exception.*;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.repository.AlunoRepository;
import com.ufcg.psoft.tccManager.dto.aluno.*;
import com.ufcg.psoft.tccManager.repository.*;
import com.ufcg.psoft.tccManager.service.orientacaoTCC.OrientacaoTccService;
import com.ufcg.psoft.tccManager.service.professor.ProfessorService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AlunoServiceImpl implements AlunoService {

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AreaDeEstudoRepository areaDeEstudoRepository;

    @Autowired
    ProfessorService professorService;

    @Override
    public AlunoResponseDTO criar(AlunoPostPutRequestDTO alunoPostPutRequestDTO, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        Aluno aluno = modelMapper.map(alunoPostPutRequestDTO, Aluno.class);
        alunoRepository.save(aluno);
        return modelMapper.map(aluno, AlunoResponseDTO.class);
    }

    @Override
    public AlunoResponseDTO recuperar(UUID id, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        Aluno aluno = alunoRepository.findById(id).orElseThrow(AlunoNaoExisteException::new);
        return new AlunoResponseDTO(aluno);
    }

    @Override
    public List<AlunoResponseDTO> listar(Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.stream()
                .map(AlunoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public AlunoResponseDTO atualizaAluno(@Valid UUID id, AlunoPostPutRequestDTO alunoPostPutRequestDTO, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        Aluno aluno = alunoRepository.findById(id).orElseThrow(AlunoNaoExisteException::new);
        modelMapper.map(alunoPostPutRequestDTO, aluno);
        alunoRepository.save(aluno);
        return modelMapper.map(aluno, AlunoResponseDTO.class);
    }

    @Transactional
    @Override
    public void receberNotificacao (String titulo, AreaDeEstudo areaDeEstudo) {

        for (AlunoResponseDTO alunoResponseDTO : (this.listar(Perfil.COORDENADOR))) {
            if(alunoResponseDTO.getAreasDeInteresse().contains(areaDeEstudo)) {
                printarNotificacao(titulo, alunoResponseDTO, areaDeEstudo);
            }
        }
    }

    @Override
    public void printarNotificacao(String titulo, AlunoResponseDTO aluno, AreaDeEstudo areaDeEstudo) {
        System.out.println("------------------------------------------------------------------------------------------\n"
                + "| Voce tem uma nova notificacao!\n| \n"
                + "| " + aluno.getNomeCompleto() + ", \n"
                + "| um novo tema de TCC: " + titulo + "\n"
                + "| foi cadastrado por um professor.\n"
                + "| Na area: " + areaDeEstudo.getNome() +"\n"
                + "| (area de seu interesse)\n"
                + "------------------------------------------------------------------------------------------");
    }

    @Override
    public void removerAluno(UUID id, Perfil perfil) {
         if (!checaPerfil(perfil, Perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        Aluno aluno = alunoRepository.findById(id).orElseThrow(AlunoNaoExisteException::new);
        alunoRepository.delete(aluno);
    }

    private boolean checaPerfil(Perfil perfil, Perfil perfilEsperado) {
        return (perfilEsperado.equals(perfil));
    }

    @Override
    public void informarAreaDeInteresse(UUID idAluno, Long idArea) {

        Optional<Aluno> alunoOp = alunoRepository.findById(idAluno);
        if(alunoOp.isPresent()){
            Aluno aluno = alunoOp.get();
            Optional<AreaDeEstudo> areaOp = areaDeEstudoRepository.findById(idArea);
            if(areaOp.isPresent()) {
                AreaDeEstudo area = areaOp.get();
                aluno.getAreasDeInteresse().add(area);
                alunoRepository.save(aluno);
            } else {
                throw new AreaDeEstudoNaoExisteException();
            }
        } else {
            throw new AlunoNaoExisteException();
        }
    }

    @Override
    public Set<ProfessorResponseNomeEmailDTO> listarProfessoresDisponiveis(UUID id) {
        Set<AreaDeEstudo> areasDoAluno = alunoRepository.getAlunoById(id).getAreasDeInteresse();
        if (areasDoAluno.isEmpty()) throw new NaoHaAreasDeEstudoException();
        else return professorService.listarProfessoresDisponiveis(areasDoAluno);
    }

}