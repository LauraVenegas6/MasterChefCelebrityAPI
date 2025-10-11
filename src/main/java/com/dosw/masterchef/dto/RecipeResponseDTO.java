package com.dosw.masterchef.dto;

import com.dosw.masterchef.model.ChefType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for recipe response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con los datos de una receta")
public class RecipeResponseDTO {
    
    @Schema(description = "ID único de MongoDB", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Número consecutivo de la receta", example = "1")
    private Long consecutivo;
    
    @Schema(description = "Título de la receta", example = "Ajiaco Santafereño")
    private String titulo;
    
    @Schema(description = "Lista de ingredientes")
    private List<String> ingredientes;
    
    @Schema(description = "Pasos de preparación")
    private List<String> pasosPreparacion;
    
    @Schema(description = "Nombre del chef", example = "Jorge Rausch")
    private String nombreChef;
    
    @Schema(description = "Tipo de chef", example = "CHEF_JURADO")
    private ChefType tipoChef;
    
    @Schema(description = "Temporada del programa", example = "5")
    private Integer temporada;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de creación", example = "2025-10-10 15:30:00")
    private LocalDateTime fechaCreacion;
}