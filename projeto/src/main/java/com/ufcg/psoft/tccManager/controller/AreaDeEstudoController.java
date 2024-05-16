package com.ufcg.psoft.tccManager.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ufcg.psoft.tccManager.dto.area.AreaDeEstudoPostPutRequestDTO;
import com.ufcg.psoft.tccManager.service.area.AreaDeEstudoService;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;

import jakarta.validation.Valid;

@RestController
@RequestMapping(
        value = "/areasEstudo",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class AreaDeEstudoController {

    @Autowired
    AreaDeEstudoService areaDeEstudoService;

    @PostMapping()
    public ResponseEntity<?> criarAreaDeEstudo(
            @RequestBody @Valid AreaDeEstudoPostPutRequestDTO areaDeEstudoPostPutRequestDto, @RequestParam Perfil perfil) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(areaDeEstudoService.criar(areaDeEstudoPostPutRequestDto, perfil));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarAreaDeEstudo(
            @PathVariable Long id, @RequestParam Perfil perfil) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(areaDeEstudoService.recuperar(id, perfil));
    }
    @GetMapping("")
    public ResponseEntity<?> listarAreaDeEstudo(@RequestParam Perfil perfil) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(areaDeEstudoService.listar(perfil));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAreaDeEstudo(
            @PathVariable Long id, @RequestParam Perfil perfil,
            @RequestBody @Valid AreaDeEstudoPostPutRequestDTO areaDeEstudoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(areaDeEstudoService.alterar(id, areaDeEstudoPostPutRequestDto, perfil));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirAreaDeEstudo(
            @PathVariable Long id, @RequestParam Perfil perfil) {
        areaDeEstudoService.remover(id, perfil);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }


}
