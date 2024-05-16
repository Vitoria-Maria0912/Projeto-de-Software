package com.ufcg.psoft.tccManager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "areas de estudo")
public class AreaDeEstudo {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @Override
    public String toString(){
        return ("\n                       " +
                "- Nome: " + this.getNome()
        );
    }
}
