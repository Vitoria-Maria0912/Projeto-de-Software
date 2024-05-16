package com.ufcg.psoft.tccManager.service.area;


import com.ufcg.psoft.tccManager.exception.AreaDeEstudoNaoExisteException;
import com.ufcg.psoft.tccManager.repository.AlunoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.tccManager.exception.AcessoNegadoException;
import com.ufcg.psoft.tccManager.dto.area.*;
import com.ufcg.psoft.tccManager.model.AreaDeEstudo;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.repository.AreaDeEstudoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaDeEstudoServiceImpl implements AreaDeEstudoService{

    @Autowired
    AreaDeEstudoRepository areaDeEstudoRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AlunoRepository alunoRepository;

    @Override
    public AreaDeEstudoResponseDTO criar(AreaDeEstudoPostPutRequestDTO areaDeEstudoPostPutRequestDTO, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        AreaDeEstudo area = modelMapper.map(areaDeEstudoPostPutRequestDTO, AreaDeEstudo.class);
        areaDeEstudoRepository.save(area);
        return modelMapper.map(area, AreaDeEstudoResponseDTO.class);
    }

    @Override
    public List<AreaDeEstudoResponseDTO> listar(Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        List<AreaDeEstudo> areasDeEstudo = areaDeEstudoRepository.findAll();
        return areasDeEstudo.stream()
                .map(AreaDeEstudoResponseDTO::new)
                .collect(Collectors.toList());
    }
    @Override
    public AreaDeEstudoResponseDTO alterar(Long id, AreaDeEstudoPostPutRequestDTO areaDeEstudoPostPutRequestDTO, Perfil perfil) {
        AreaDeEstudo areaDeEstudo = areaDeEstudoRepository.findById(id).orElseThrow(AreaDeEstudoNaoExisteException::new);
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        modelMapper.map(areaDeEstudoPostPutRequestDTO, areaDeEstudo);
        areaDeEstudoRepository.save(areaDeEstudo);
        return modelMapper.map(areaDeEstudo, AreaDeEstudoResponseDTO.class);
    }
    @Override
    public AreaDeEstudoResponseDTO recuperar(Long id, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        AreaDeEstudo areaDeEstudo = areaDeEstudoRepository.findById(id).orElseThrow(AreaDeEstudoNaoExisteException::new);
        return new AreaDeEstudoResponseDTO(areaDeEstudo);
    }
    public void remover(Long id, Perfil perfil) {
        if (!checaPerfil(perfil, perfil.COORDENADOR)) {
            throw new AcessoNegadoException();
        }
        AreaDeEstudo area = areaDeEstudoRepository.findById(id).orElseThrow(AreaDeEstudoNaoExisteException::new);
        
        areaDeEstudoRepository.delete(area);
    }

    private boolean checaPerfil(Perfil perfil, Perfil perfilEsperado) {
        return (perfilEsperado.equals(perfil));
    }

}

