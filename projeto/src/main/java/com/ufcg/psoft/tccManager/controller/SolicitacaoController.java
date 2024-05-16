package com.ufcg.psoft.tccManager.controller;

import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.service.Solicitacao.SolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(
        value = "/solicitacoesOrientacoes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class SolicitacaoController {

    @Autowired
    SolicitacaoService solicitacaoService;
    @PutMapping("/responderSolicitacaoOrientacaoTCCTemaProfessor/{id}")
    public ResponseEntity<?> responderSolicitacaoOrientacaoTCC(@PathVariable (value = "id") Long idOrientacao,
                                                               @RequestParam Boolean respostaSolicitacaoOrientacao,
                                                               @RequestParam Perfil perfil,
                                                               @RequestParam String mensagemResposta) throws Exception {
        this.solicitacaoService.analisaSolicitacaoTemaProfessor(idOrientacao, respostaSolicitacaoOrientacao ,perfil, mensagemResposta);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PutMapping("/responderSolicitacaoTccTemaAluno/{id}")
    public ResponseEntity<?> responderSolicitacaoTccTemaAlnuo(@PathVariable(value = "id") Long idSolicitacao,
                                                              @RequestParam Boolean respostaSolicitacao,
                                                              @RequestParam String mensagem) throws Exception {
        this.solicitacaoService.analisaSolicitacaoTemaAluno(idSolicitacao, respostaSolicitacao, mensagem);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PutMapping("/solicitacaoTemaProf/{id}")
    public ResponseEntity<?> listarSolicitacoesTemaProf(
            @PathVariable(value="id") UUID idProf){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(solicitacaoService.listarSolicitacoesTemaTCCProf(idProf));
    }



}
