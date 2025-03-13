package isis.projet.backend.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

// Un exemple d'entité
// On utilise Lombok pour auto-générer getter / setter / toString...
// cf. https://examples.javacodegeeks.com/spring-boot-with-lombok/
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
@Entity // Une entité JPA
public class Indicateur {
    // Identifiant technique (clé primaire, auto-générée)
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;
    // Identifiant métier (code ISO)
    @NotBlank // Validation : ne doit pas contenir uniquement des espaces
    @NotEmpty // Validation : ne doit pas être vide
    @Column(unique=true)
    @NonNull // Lombok : génère un constructeur avec ce paramètre
    // Identifiant métier (code ISO)
    private String nom;
    
    @Column(unique=true)
    @NonNull // Lombok : génère un constructeur avec ce paramètre
    @NotBlank // Validation : ne doit pas contenir uniquement des espaces
    @NotEmpty // Validation : ne doit pas être vide
    private String valeur;

    @Column(unique=true)
    @NonNull // Lombok : génère un constructeur avec ce paramètre
    @NotBlank // Validation : ne doit pas contenir uniquement des espaces
    @NotEmpty // Validation : ne doit pas être vide
    private String unite;

    @Column
    @NonNull // Lombok : génère un constructeur avec ce paramètre
    @NotBlank // Validation : ne doit pas contenir uniquement des espaces
    @NotEmpty // Validation : ne doit pas être vide
    private String categorie;
}
