package com.ufcg.psoft.tccManager.dto.temaTCC;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemaTCCPostPutRequestDTO {

    @JsonProperty("titulo")
    @NotBlank(message = "Título obrigatório")
    private String titulo;

    @JsonProperty("descricao")
    @NotBlank(message = "Descricao obrigatória")
    private String descricao;

    @JsonProperty("areasDeEstudo")
    @NotEmpty(message = "Area(s) de estudo obrigatoria(s)")
    private Set<AreaDeEstudo> areasDeEstudo;

    @JsonProperty("status")
    private Status status;

}
