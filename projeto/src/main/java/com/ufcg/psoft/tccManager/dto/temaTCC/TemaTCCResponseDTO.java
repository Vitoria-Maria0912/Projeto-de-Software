package com.ufcg.psoft.tccManager.dto.temaTCC;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemaTCCResponseDTO {

    @JsonProperty("id")
    @Id
    private Long id;
    @JsonProperty("titulo")
    @NotBlank(message = "Título obrigatório")
    private String titulo;

    @JsonProperty("descricao")
    @NotBlank(message = "Descricao obrigatória")
    private String descricao;

    @JsonProperty("areasDeConhecimento")
    @NotEmpty
    private Set<AreaDeEstudo> areasDeEstudo;

    @JsonProperty("status")
    private Status status;

    public TemaTCCResponseDTO(TemaTCC temaTCC){
        this.id = temaTCC.getId();
        this.titulo = temaTCC.getTitulo();
        this.descricao = temaTCC.getDescricao();
        this.areasDeEstudo = temaTCC.getAreasDeEstudo();
        this.status = temaTCC.getStatus();

    }

}
