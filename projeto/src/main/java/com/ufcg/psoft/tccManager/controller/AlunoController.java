package com.ufcg.psoft.tccManager.controller;

import com.ufcg.psoft.tccManager.dto.aluno.AlunoPostPutRequestDTO;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.service.aluno.AlunoService;
import com.ufcg.psoft.tccManager.service.area.AreaDeEstudoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(
        value = "/alunos",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class AlunoController {

        @Autowired
        AlunoService alunoService;

        @Autowired
        AreaDeEstudoService areaDeEstudoService;


        @GetMapping("/{id}")
        public ResponseEntity<?> recuperarAluno(
                @PathVariable UUID id, @RequestParam Perfil perfil) {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(alunoService.recuperar(id, perfil));
        }

        @GetMapping("")
        public ResponseEntity<?> listarAlunos(@RequestParam Perfil perfil) {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(alunoService.listar(perfil));
        }

        @PostMapping()
        public ResponseEntity<?> criarAluno(
                        @RequestBody @Valid AlunoPostPutRequestDTO alunoPostPutRequestDto,
                        @RequestParam Perfil perfil) {
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(alunoService.criar(alunoPostPutRequestDto, perfil));
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> atualizarAluno(
                        @PathVariable @Valid UUID id,
                        @RequestBody @Valid AlunoPostPutRequestDTO alunoPostPutRequestDTO,
                        @RequestParam Perfil perfil) {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(alunoService.atualizaAluno(id, alunoPostPutRequestDTO, perfil));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> removerAluno(
                        @PathVariable UUID id,
                        @RequestParam Perfil perfil) {
                                this.alunoService.removerAluno(id, perfil);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }

        @PutMapping("/interesse/{id}")
        public ResponseEntity<?> informarAreaDeInteresseAluno(
                @PathVariable UUID id,
                @RequestParam Long idArea) {

                alunoService.informarAreaDeInteresse(id, idArea);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/professoresDisponiveis/{id}")
        public ResponseEntity<?> listarProfessoresDisponiveis(@PathVariable UUID id) {

                return ResponseEntity.status(HttpStatus.OK).body(alunoService.listarProfessoresDisponiveis(id));
        }

}