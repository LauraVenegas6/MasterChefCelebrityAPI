package com.dosw.masterchef.repository;

import com.dosw.masterchef.model.ChefType;
import com.dosw.masterchef.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Recipe entities in MongoDB.
 */
@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    
    /**
     * Searches for a recipe by its unique consecutivo number.
     * @param consecutivo Number of the recipe
     * @return Optional with the found recipe or empty if not found
     */
    Optional<Recipe> findByConsecutivo(Long consecutivo);
    
    /**
     * Searches for all recipes of a specific chef type.
     * @param tipoChef ChefType (TELEVIDENTE, PARTICIPANTE, CHEF_JURADO)
     * @return List of recipes for the specified chef type
     */
    List<Recipe> findByTipoChef(ChefType tipoChef);
    
    /**
     * searches for all recipes from a specific season.
     * @param temporada Season number
     * @return List of recipes from the specified season
     */
    List<Recipe> findByTemporada(Integer temporada);
    
    /**
     * searches for recipes that contain a specific ingredient (case insensitive).
     * @param ingrediente Ingredient to search for
     * @return List of recipes containing the specified ingredient
     */
    @Query("{ 'ingredientes': { $regex: ?0, $options: 'i' } }")
    List<Recipe> findByIngrediente(String ingrediente);
    
    /**
     * obteins the recipe with the highest consecutivo number.
     * @return Optional with the recipe with the highest consecutivo or empty if no recipes exist
     */
    Optional<Recipe> findTopByOrderByConsecutivoDesc();
    
    /**
     * Checks if a recipe with the given consecutivo number exists.
     * @param consecutivo Number of the recipe
     * @return true if a recipe with the given consecutivo exists, false otherwise
     */
    boolean existsByConsecutivo(Long consecutivo);
}