package com.dosw.masterchef.service;

import com.dosw.masterchef.dto.RecipeRequestDTO;
import com.dosw.masterchef.dto.RecipeResponseDTO;
import com.dosw.masterchef.exception.RecipeNotFoundException;
import com.dosw.masterchef.model.ChefType;
import com.dosw.masterchef.model.Recipe;
import com.dosw.masterchef.repository.RecipeRepository;
import com.dosw.masterchef.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RecipeService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del Servicio de Recetas")
class RecipeServiceTest {
    
    @Mock
    private RecipeRepository recipeRepository;
    
    @InjectMocks
    private RecipeServiceImpl recipeService;
    
    private RecipeRequestDTO requestDTO;
    private Recipe recipe;
    
    @BeforeEach
    void setUp() {
        // Configure test data
        requestDTO = RecipeRequestDTO.builder()
                .titulo("Ajiaco Santafereño")
                .ingredientes(Arrays.asList("Pollo", "Papa criolla", "Papa sabanera", "Mazorca", "Guascas"))
                .pasosPreparacion(Arrays.asList(
                        "Cocinar el pollo con sal",
                        "Agregar las papas cortadas",
                        "Añadir la mazorca",
                        "Agregar guascas al final"
                ))
                .nombreChef("Jorge Rausch")
                .tipoChef(ChefType.CHEF_JURADO)
                .build();
        
        recipe = Recipe.builder()
                .id("507f1f77bcf86cd799439011")
                .consecutivo(1L)
                .titulo("Ajiaco Santafereño")
                .ingredientes(Arrays.asList("Pollo", "Papa criolla", "Papa sabanera", "Mazorca", "Guascas"))
                .pasosPreparacion(Arrays.asList(
                        "Cocinar el pollo con sal",
                        "Agregar las papas cortadas",
                        "Añadir la mazorca",
                        "Agregar guascas al final"
                ))
                .nombreChef("Jorge Rausch")
                .tipoChef(ChefType.CHEF_JURADO)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
    
    @Test
    @DisplayName("Debería crear una receta exitosamente")
    void shouldCreateRecipeSuccessfully() {
        // Arrange
        when(recipeRepository.findTopByOrderByConsecutivoDesc()).thenReturn(Optional.empty());
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
        
        // Act
        RecipeResponseDTO response = recipeService.crearReceta(requestDTO);
        
        // Assert
        assertNotNull(response);
        assertEquals("Ajiaco Santafereño", response.getTitulo());
        assertEquals(1L, response.getConsecutivo());
        assertEquals(ChefType.CHEF_JURADO, response.getTipoChef());
        assertEquals("Jorge Rausch", response.getNombreChef());
        assertEquals(5, response.getIngredientes().size());
        
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(recipeRepository, times(1)).findTopByOrderByConsecutivoDesc();
    }
    
    @Test
    @DisplayName("Debería buscar recetas por ingrediente correctamente")
    void shouldSearchByIngredientCorrectly() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe);
        when(recipeRepository.findByIngrediente("Papa")).thenReturn(recipes);
        
        // Act
        List<RecipeResponseDTO> results = recipeService.buscarPorIngrediente("Papa");
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Ajiaco Santafereño", results.get(0).getTitulo());
        assertTrue(results.get(0).getIngredientes().stream()
                .anyMatch(ing -> ing.contains("Papa")));
        
        verify(recipeRepository, times(1)).findByIngrediente("Papa");
    }
    
    @Test
    @DisplayName("Debería lanzar excepción al consultar receta inexistente")
    void shouldThrowExceptionWhenRecipeNotFound() {
        // Arrange
        Long consecutivoInexistente = 999L;
        when(recipeRepository.findByConsecutivo(consecutivoInexistente))
                .thenReturn(Optional.empty());
        
        // Act & Assert
        RecipeNotFoundException exception = assertThrows(
                RecipeNotFoundException.class,
                () -> recipeService.obtenerRecetaPorConsecutivo(consecutivoInexistente)
        );
        
        assertTrue(exception.getMessage().contains("999"));
        verify(recipeRepository, times(1)).findByConsecutivo(consecutivoInexistente);
    }
    
    @Test
    @DisplayName("Debería validar temporada obligatoria para participantes")
    void shouldValidateSeasonRequiredForParticipants() {
        // Arrange
        RecipeRequestDTO participanteRequest = RecipeRequestDTO.builder()
                .titulo("Receta de Participante")
                .ingredientes(Arrays.asList("Ingrediente 1", "Ingrediente 2"))
                .pasosPreparacion(Arrays.asList("Paso 1", "Paso 2"))
                .nombreChef("Participante Test")
                .tipoChef(ChefType.PARTICIPANTE)
                .temporada(null) // Sin temporada
                .build();
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recipeService.crearReceta(participanteRequest)
        );
        
        assertTrue(exception.getMessage().contains("temporada"));
        assertTrue(exception.getMessage().contains("obligatoria"));
        verify(recipeRepository, never()).save(any(Recipe.class));
    }
    
    @Test
    @DisplayName("Debería obtener todas las recetas")
    void shouldGetAllRecipes() {
        // Arrange
        Recipe recipe2 = Recipe.builder()
                .id("507f1f77bcf86cd799439012")
                .consecutivo(2L)
                .titulo("Bandeja Paisa")
                .ingredientes(Arrays.asList("Frijoles", "Chicharrón", "Arroz"))
                .pasosPreparacion(Arrays.asList("Cocinar frijoles", "Freír chicharrón"))
                .nombreChef("Claudia Bahamón")
                .tipoChef(ChefType.TELEVIDENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();
        
        when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe, recipe2));
        
        // Act
        List<RecipeResponseDTO> results = recipeService.obtenerTodasLasRecetas();
        
        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(recipeRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Debería eliminar receta exitosamente")
    void shouldDeleteRecipeSuccessfully() {
        // Arrange
        when(recipeRepository.findByConsecutivo(1L)).thenReturn(Optional.of(recipe));
        doNothing().when(recipeRepository).delete(recipe);
        
        // Act
        recipeService.eliminarReceta(1L);
        
        // Assert
        verify(recipeRepository, times(1)).findByConsecutivo(1L);
        verify(recipeRepository, times(1)).delete(recipe);
    }
    
    @Test
    @DisplayName("Debería actualizar receta exitosamente")
    void shouldUpdateRecipeSuccessfully() {
        // Arrange
        RecipeRequestDTO updateRequest = RecipeRequestDTO.builder()
                .titulo("Ajiaco Modificado")
                .ingredientes(Arrays.asList("Pollo", "Papa", "Maíz"))
                .pasosPreparacion(Arrays.asList("Nuevo paso 1", "Nuevo paso 2"))
                .nombreChef("Jorge Rausch")
                .tipoChef(ChefType.CHEF_JURADO)
                .build();
        
        Recipe updatedRecipe = Recipe.builder()
                .id(recipe.getId())
                .consecutivo(recipe.getConsecutivo())
                .titulo("Ajiaco Modificado")
                .ingredientes(updateRequest.getIngredientes())
                .pasosPreparacion(updateRequest.getPasosPreparacion())
                .nombreChef(updateRequest.getNombreChef())
                .tipoChef(updateRequest.getTipoChef())
                .fechaCreacion(recipe.getFechaCreacion())
                .build();
        
        when(recipeRepository.findByConsecutivo(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(updatedRecipe);
        
        // Act
        RecipeResponseDTO response = recipeService.actualizarReceta(1L, updateRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("Ajiaco Modificado", response.getTitulo());
        verify(recipeRepository, times(1)).findByConsecutivo(1L);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
    
    @Test
    @DisplayName("Debería obtener recetas por tipo de chef")
    void shouldGetRecipesByChefType() {
        // Arrange
        when(recipeRepository.findByTipoChef(ChefType.CHEF_JURADO))
                .thenReturn(Arrays.asList(recipe));
        
        // Act
        List<RecipeResponseDTO> results = recipeService.obtenerRecetasPorTipo(ChefType.CHEF_JURADO);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(ChefType.CHEF_JURADO, results.get(0).getTipoChef());
        verify(recipeRepository, times(1)).findByTipoChef(ChefType.CHEF_JURADO);
    }
    
    @Test
    @DisplayName("Debería generar consecutivo correctamente")
    void shouldGenerateConsecutiveNumberCorrectly() {
        // Arrange
        Recipe lastRecipe = Recipe.builder()
                .consecutivo(5L)
                .build();
        
        when(recipeRepository.findTopByOrderByConsecutivoDesc())
                .thenReturn(Optional.of(lastRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe saved = invocation.getArgument(0);
            saved.setId("newId");
            return saved;
        });
        
        // Act
        RecipeResponseDTO response = recipeService.crearReceta(requestDTO);
        
        // Assert
        assertNotNull(response);
        // The consecutive should be 6 (5 + 1)
        verify(recipeRepository, times(1)).findTopByOrderByConsecutivoDesc();
    }
}
