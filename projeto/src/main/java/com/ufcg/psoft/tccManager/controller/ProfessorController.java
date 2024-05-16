package com.ufcg.psoft.tccManager.controller;

import com.ufcg.psoft.tccManager.dto.professor.ProfessorPostPutRequestDTO;
import com.ufcg.psoft.tccManager.service.professor.ProfessorService;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(
        value = "/professores",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarProfessor(
            @PathVariable UUID id, @RequestParam Perfil perfil) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorService.recuperar(id, perfil));
    }

    @GetMapping("")
    public ResponseEntity<?> listarProfessores(@RequestParam Perfil perfil) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorService.listar(perfil));
    }

    @PostMapping()
    public ResponseEntity<?> criarProfessor(
            @RequestBody @Valid ProfessorPostPutRequestDTO professorPostPutRequestDto,
            @RequestParam Perfil perfil) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(professorService.criar(professorPostPutRequestDto, perfil));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfessor(
                @PathVariable UUID id,
                @RequestBody @Valid ProfessorPostPutRequestDTO professorPostPutRequestDTO, @RequestParam Perfil perfil) {    
        return ResponseEntity.status(HttpStatus.OK).body(professorService.alterarProfessor(id, professorPostPutRequestDTO, perfil));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removerProfessor(@PathVariable(value="id") UUID id, @RequestParam Perfil perfil){
        professorService.removerProfessor(id, perfil);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

    @PutMapping("/atualizarQuota/{id}")
    public ResponseEntity<?> atualizarQuota(@PathVariable(value = "id") UUID idProfessor,
                                            @RequestParam Integer novaQuota,
                                            @RequestParam Perfil perfil){
        return ResponseEntity.status(HttpStatus.OK).body(professorService.atualizarQuota(idProfessor, novaQuota, perfil));
    }

    @PutMapping("/interesseProf/{id}")
        public ResponseEntity<?> informarAreaDeInteresseProf(
                @PathVariable UUID id,
                @RequestParam Long idArea) {

                professorService.informarAreaDeInteresseProf(id, idArea);
                return ResponseEntity.ok().build();
        }
}
