package com.ufcg.psoft.tccManager.dto.professor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorResponseDTO {

    @JsonProperty("id")
    @Id
    private UUID id;

    @JsonProperty("perfil")
    private Perfil perfil;

    @JsonProperty("nomeCompleto")
    @NotBlank(message = "Nome completo obrigatorio!")
    private String nomeCompleto;

    @JsonProperty("email")
    @NotBlank(message = "Email obrigatorio!")
    private String email;

    @JsonProperty("laboratorios")
    @NotNull(message = "Laboratórios devem ser preenchido!")
    private HashSet<String> laboratorios;

    @JsonProperty("quota")
    @NotNull(message = "Quota obrigatoria!")
    @Min(value = 0, message = "A quota deve ser positiva!")
    private Integer quota;

    @ManyToMany
    @Builder.Default
    private Set<AreaDeEstudo> areasDeInteresse = new HashSet<>();

    @JsonProperty("temasDeTcc")
    @Builder.Default
    private Set<TemaTCC> temasTcc = new HashSet<>();

    public ProfessorResponseDTO(Professor professor) {
        this.id = professor.getId();
        this.perfil = professor.getPerfil();
        this.nomeCompleto = professor.getNomeCompleto();
        this.email = professor.getEmail();
        this.laboratorios = professor.getLaboratorios();
        this.quota = professor.getQuota();
        this.areasDeInteresse = professor.getAreasDeInteresse();
        this.temasTcc = professor.getTemasTcc();
    }
}
