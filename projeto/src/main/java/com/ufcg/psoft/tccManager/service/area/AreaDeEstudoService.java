package com.ufcg.psoft.tccManager.service.area;

import com.ufcg.psoft.tccManager.dto.area.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import java.util.List;

public interface AreaDeEstudoService {

    AreaDeEstudoResponseDTO criar(AreaDeEstudoPostPutRequestDTO areaDeEstudoPostPutRequestDTO, Perfil perfil);
    List<AreaDeEstudoResponseDTO> listar(Perfil perfil);
    AreaDeEstudoResponseDTO alterar(Long id, AreaDeEstudoPostPutRequestDTO areaDeEstudoPostPutRequestDTO, Perfil perfil);
    AreaDeEstudoResponseDTO recuperar(Long id, Perfil perfil);
    void remover(Long id, Perfil perfil);
}