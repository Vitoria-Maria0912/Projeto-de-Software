package com.ufcg.psoft.tccManager.controller;

import com.ufcg.psoft.tccManager.dto.temaTCC.TemaTCCPostPutRequestDTO;
import com.ufcg.psoft.tccManager.service.temaTCC.TemaTCCService;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@RestController
@RequestMapping(
        value = "/temasTCC",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TemaTCCController {

    @Autowired
    TemaTCCService temaTCCService;


    @PostMapping()
    public ResponseEntity<?> criaTemaTCC(
            @RequestBody @Valid TemaTCCPostPutRequestDTO temaTCCPostPutRequestDto,
            @RequestParam UUID id) throws Exception {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(temaTCCService.criar(temaTCCPostPutRequestDto, id));
    }

    @GetMapping("")
    public ResponseEntity<?> listarTemasCadastrados(@RequestParam Perfil perfil) throws Exception{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(temaTCCService.listarTemasCadastrados(perfil));
    }

    @GetMapping("/listaTemaTccProfessor/{id}")
    public ResponseEntity<?> listarTemasCadastradosPeloProfessor(@RequestParam Perfil perfil,@RequestParam UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(temaTCCService.listarTemasCadastradosPeloProfessor(perfil, id));
    }

    @PostMapping("/orientacaoTemaAluno")
    public ResponseEntity<?> solicitarOrientacaoTemaTccAluno(
            @RequestParam UUID idAluno,
            @RequestParam UUID idProfessor,
            @RequestParam Long idTema){

        this.temaTCCService.solicitarOrientacaoTemaTccAluno(idAluno, idProfessor, idTema);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @PostMapping("/orientacaoTemaProf")
    public ResponseEntity<?> solicitarOrientacaoTemaProf(
            @RequestParam UUID idAluno,
            @RequestParam Long idTema,
            @RequestParam UUID idProfessor){

        this.temaTCCService.solicitarOrientacaoTemaProfessor(idAluno, idTema, idProfessor);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @GetMapping("/gerarRelatorio")
    public ResponseEntity<?> gerarRelatorio(@RequestParam Perfil perfil) throws Exception {
        temaTCCService.gerarRelatorio(perfil);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
