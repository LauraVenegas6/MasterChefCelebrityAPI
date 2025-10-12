package com.dosw.masterchef.service.impl;

import com.dosw.masterchef.dto.RecipeRequestDTO;
import com.dosw.masterchef.dto.RecipeResponseDTO;
import com.dosw.masterchef.exception.RecipeNotFoundException;
import com.dosw.masterchef.model.ChefType;
import com.dosw.masterchef.model.Recipe;
import com.dosw.masterchef.repository.RecipeRepository;
import com.dosw.masterchef.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of RecipeService
 * Contains business logic for managing recipes
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    
    private final RecipeRepository recipeRepository;
    
    @Override
    @Transactional
    public RecipeResponseDTO crearReceta(RecipeRequestDTO request) {
        log.info("Creando nueva receta: {}", request.getTitulo());
        
        validarReceta(request);
        
        Recipe recipe = Recipe.builder()
                .consecutivo(generarConsecutivo())
                .titulo(request.getTitulo())
                .ingredientes(request.getIngredientes())
                .pasosPreparacion(request.getPasosPreparacion())
                .nombreChef(request.getNombreChef())
                .tipoChef(request.getTipoChef())
                .temporada(request.getTemporada())
                .fechaCreacion(LocalDateTime.now())
                .build();
        
        Recipe saved = recipeRepository.save(recipe);
        log.info("Receta creada exitosamente con consecutivo: {}", saved.getConsecutivo());
        
        return convertToDTO(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponseDTO> obtenerTodasLasRecetas() {
        log.info("Obteniendo todas las recetas");
        return recipeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public RecipeResponseDTO obtenerRecetaPorConsecutivo(Long consecutivo) {
        log.info("Buscando receta con consecutivo: {}", consecutivo);
        Recipe recipe = recipeRepository.findByConsecutivo(consecutivo)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "No se encontró la receta con consecutivo: " + consecutivo));
        return convertToDTO(recipe);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponseDTO> obtenerRecetasPorTipo(ChefType tipoChef) {
        log.info("Obteniendo recetas de tipo: {}", tipoChef);
        return recipeRepository.findByTipoChef(tipoChef).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponseDTO> obtenerRecetasPorTemporada(Integer temporada) {
        log.info("Obteniendo recetas de temporada: {}", temporada);
        return recipeRepository.findByTemporada(temporada).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponseDTO> buscarPorIngrediente(String ingrediente) {
        log.info("Buscando recetas con ingrediente: {}", ingrediente);
        return recipeRepository.findByIngrediente(ingrediente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void eliminarReceta(Long consecutivo) {
        log.info("Eliminando receta con consecutivo: {}", consecutivo);
        Recipe recipe = recipeRepository.findByConsecutivo(consecutivo)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "No se encontró la receta con consecutivo: " + consecutivo));
        recipeRepository.delete(recipe);
        log.info("Receta eliminada exitosamente");
    }
    
    @Override
    @Transactional
    public RecipeResponseDTO actualizarReceta(Long consecutivo, RecipeRequestDTO request) {
        log.info("Actualizando receta con consecutivo: {}", consecutivo);
        
        Recipe recipe = recipeRepository.findByConsecutivo(consecutivo)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "No se encontró la receta con consecutivo: " + consecutivo));
        
        validarReceta(request);
        
        recipe.setTitulo(request.getTitulo());
        recipe.setIngredientes(request.getIngredientes());
        recipe.setPasosPreparacion(request.getPasosPreparacion());
        recipe.setNombreChef(request.getNombreChef());
        recipe.setTipoChef(request.getTipoChef());
        recipe.setTemporada(request.getTemporada());
        
        Recipe updated = recipeRepository.save(recipe);
        log.info("Receta actualizada exitosamente");
        
        return convertToDTO(updated);
    }
    
    /**
     * generates the next consecutivo number for a new recipe
     */
    private Long generarConsecutivo() {
        return recipeRepository.findTopByOrderByConsecutivoDesc()
                .map(recipe -> recipe.getConsecutivo() + 1)
                .orElse(1L);
    }
    
    /**
     * Validates the recipe data according to business rules
     */
    private void validarReceta(RecipeRequestDTO request) {
        if (request.getTipoChef() == ChefType.PARTICIPANTE && request.getTemporada() == null) {
            throw new IllegalArgumentException(
                    "La temporada es obligatoria para recetas de participantes");
        }
        
        if (request.getTipoChef() != ChefType.PARTICIPANTE && request.getTemporada() != null) {
            log.warn("Se proporcionó temporada para un chef que no es participante. Se ignorará.");
        }
    }
    
    /**
     * Converts a Recipe entity to a RecipeResponseDTO
     */
    private RecipeResponseDTO convertToDTO(Recipe recipe) {
        return RecipeResponseDTO.builder()
                .id(recipe.getId())
                .consecutivo(recipe.getConsecutivo())
                .titulo(recipe.getTitulo())
                .ingredientes(recipe.getIngredientes())
                .pasosPreparacion(recipe.getPasosPreparacion())
                .nombreChef(recipe.getNombreChef())
                .tipoChef(recipe.getTipoChef())
                .temporada(recipe.getTemporada())
                .fechaCreacion(recipe.getFechaCreacion())
                .build();
    }
}