package com.ufcg.psoft.tccManager.dto.area;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.AreaDeEstudo;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaDeEstudoResponseDTO {

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;
    
    @JsonProperty("id")
    @Id
    private Long id;
    
    
    public AreaDeEstudoResponseDTO(AreaDeEstudo area) {
        this.id = area.getId();
        this.nome = area.getNome();
    }
}
