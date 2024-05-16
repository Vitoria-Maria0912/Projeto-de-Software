package com.ufcg.psoft.tccManager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "temaTCC")
public class TemaTCC {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("titulo")
    @Column(nullable = false)
    private String titulo;

    @JsonProperty("descricao")
    @Column(nullable = false)
    private String descricao;

    @JsonProperty("areasDeConhecimento")
    @Column(nullable = false)
    @NotEmpty
    @ManyToMany
    @JoinTable(
            name = "temaTcc_areaDeEstudo",
            joinColumns = @JoinColumn(name = "temaTCC_id"),
            inverseJoinColumns = @JoinColumn(name = "areaDeEstudo_id")
    )
    private Set<AreaDeEstudo> areasDeEstudo;

    @JsonProperty("status")
    @Column(nullable = true)
    private Status status;

    @PrePersist
    private void setStatus() {
        setStatus(Status.NOVO);
    }

    @Override
    public String toString() {
        return ("\n                 " +
                "- Titulo: " + this.getTitulo()
        );
    }
}
