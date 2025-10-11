package com.dosw.masterchef.dto;

import com.dosw.masterchef.model.ChefType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating or updating a recipe.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear o actualizar una receta")
public class RecipeRequestDTO {
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 200, message = "El título debe tener entre 3 y 200 caracteres")
    @Schema(description = "Título de la receta", example = "Ajiaco Santafereño")
    private String titulo;
    
    @NotEmpty(message = "Debe incluir al menos un ingrediente")
    @Schema(description = "Lista de ingredientes", example = "[\"Pollo\", \"Papa criolla\", \"Mazorca\", \"Guascas\"]")
    private List<String> ingredientes;
    
    @NotEmpty(message = "Debe incluir al menos un paso de preparación")
    @Schema(description = "Pasos de preparación", example = "[\"Cocinar el pollo\", \"Agregar las papas\", \"Servir con crema\"]")
    private List<String> pasosPreparacion;
    
    @NotBlank(message = "El nombre del chef es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre del chef que crea la receta", example = "Jorge Rausch")
    private String nombreChef;
    
    @NotNull(message = "El tipo de chef es obligatorio")
    @Schema(description = "Tipo de chef", example = "CHEF_JURADO")
    private ChefType tipoChef;
    
    @Schema(description = "Temporada del programa (obligatorio solo para PARTICIPANTE)", example = "5")
    private Integer temporada;
}