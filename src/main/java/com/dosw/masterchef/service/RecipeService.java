package com.dosw.masterchef.service;

import com.dosw.masterchef.dto.RecipeRequestDTO;
import com.dosw.masterchef.dto.RecipeResponseDTO;
import com.dosw.masterchef.model.ChefType;

import java.util.List;

/**
 * Interface defining the contract for RecipeService operations.
 */
public interface RecipeService {
    
    /**
     * Creates a new recipe
     * @param request Data for the new recipe
     * @return Created recipe
     */
    RecipeResponseDTO crearReceta(RecipeRequestDTO request);
    
    /**
     * obtains all recipes
     * @return List of all recipes
     */
    List<RecipeResponseDTO> obtenerTodasLasRecetas();
    
    /**
     * obtains a recipe by its unique consecutivo number
     * @param consecutivo Number of the recipe
     * @return Recipe with the specified consecutivo
     * @throws com.dosw.masterchef.exception.RecipeNotFoundException    if not found
     */
    RecipeResponseDTO obtenerRecetaPorConsecutivo(Long consecutivo);
    
    /**
     * Obtains all recipes of a specific chef type
     * @param tipoChef chef type (TELEVIDENTE, PARTICIPANTE, CHEF_JURADO)
     * @return List of recipes for the specified chef type
     */
    List<RecipeResponseDTO> obtenerRecetasPorTipo(ChefType tipoChef);
    
    /**
     * Obtains all recipes from a specific season
     * @param temporada Number of the season
     * @return List of recipes from the specified season
     */
    List<RecipeResponseDTO> obtenerRecetasPorTemporada(Integer temporada);
    
    /**
     * searches for recipes that contain a specific ingredient (case insensitive)
     * @param ingrediente Ingredient to search for
     * @return List of recipes containing the specified ingredient
     */
    List<RecipeResponseDTO> buscarPorIngrediente(String ingrediente);
    
    /**
     * deletes a recipe by its unique consecutivo number
     * @param consecutivo Number of the recipe to delete
     * @throws com.dosw.masterchef.exception.RecipeNotFoundException if not found
     */
    void eliminarReceta(Long consecutivo);
    
    /**
     * updates an existing recipe identified by its unique consecutivo number
     * @param consecutivo Number of the recipe to update
     * @param request Data to update the recipe
     * @return Updated recipe
     * @throws com.dosw.masterchef.exception.RecipeNotFoundException if not found
     */
    RecipeResponseDTO actualizarReceta(Long consecutivo, RecipeRequestDTO request);
}