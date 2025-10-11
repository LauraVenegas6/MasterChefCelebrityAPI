package com.dosw.masterchef.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class representing a Recipe in the MasterChef Celebrity context.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recipes")
public class Recipe {
    
    /**
     * Identifier unique of the recipe
     */
    @Id
    private String id;
    
    /**
     * Number consecutive of the recipe (unique)
     */
    @Indexed(unique = true)
    private Long consecutivo;
    
    /**
     * Title of the recipe
     */
    private String titulo;
    
    /**
     * List of ingredients of the recipe
     */
    private List<String> ingredientes;
    
    /**
     * List of preparation steps of the recipe
     */
    private List<String> pasosPreparacion;
    
    /**
     * Name of the chef who created the recipe
     */
    private String nombreChef;
    
    /**
     * Chef type (TELEVIDENTE, PARTICIPANTE, CHEF_JURADO)
     */
    private ChefType tipoChef;
    
    /**
     * Season number of the MasterChef Celebrity program
     * 
     */
    private Integer temporada;
    
    /**
     * Date and time of recipe creation
     */
    private LocalDateTime fechaCreacion;
}