package com.dosw.masterchef.controller;

import com.dosw.masterchef.dto.RecipeRequestDTO;
import com.dosw.masterchef.dto.RecipeResponseDTO;
import com.dosw.masterchef.model.ChefType;
import com.dosw.masterchef.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing recipes in the MasterChef Celebrity program
 */
@Slf4j
@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
@Tag(name = "Recetas", description = "Endpoints para gestión de recetas del programa MasterChef Celebrity")
public class RecipeController {
    
    private final RecipeService recipeService;
    
    @PostMapping("/televidente")
    @Operation(summary = "Registrar receta de televidente", 
               description = "Permite a un televidente del programa registrar una nueva receta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Receta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<RecipeResponseDTO> crearRecetaTeleevidente(
            @Valid @RequestBody RecipeRequestDTO request) {
        log.info("POST /api/recetas/televidente - Creando receta de televidente");
        request.setTipoChef(ChefType.TELEVIDENTE);
        request.setTemporada(null); // viewers do not have seasons
        RecipeResponseDTO response = recipeService.crearReceta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/participante")
    @Operation(summary = "Registrar receta de participante", 
               description = "Permite a un participante del programa registrar una receta. La temporada es obligatoria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Receta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o falta temporada")
    })
    public ResponseEntity<RecipeResponseDTO> crearRecetaParticipante(
            @Valid @RequestBody RecipeRequestDTO request) {
        log.info("POST /api/recetas/participante - Creando receta de participante");
        request.setTipoChef(ChefType.PARTICIPANTE);
        RecipeResponseDTO response = recipeService.crearReceta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/chef")
    @Operation(summary = "Registrar receta de chef jurado", 
               description = "Permite a un chef jurado del programa registrar una nueva receta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Receta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<RecipeResponseDTO> crearRecetaChef(
            @Valid @RequestBody RecipeRequestDTO request) {
        log.info("POST /api/recetas/chef - Creando receta de chef jurado");
        request.setTipoChef(ChefType.CHEF_JURADO);
        request.setTemporada(null); // judges do not have seasons
        RecipeResponseDTO response = recipeService.crearReceta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Obtener todas las recetas", 
               description = "Retorna la lista completa de todas las recetas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de recetas obtenida exitosamente")
    public ResponseEntity<List<RecipeResponseDTO>> obtenerTodasLasRecetas() {
        log.info("GET /api/recetas - Obteniendo todas las recetas");
        List<RecipeResponseDTO> recetas = recipeService.obtenerTodasLasRecetas();
        return ResponseEntity.ok(recetas);
    }
    
    @GetMapping("/{consecutivo}")
    @Operation(summary = "Obtener receta por consecutivo", 
               description = "Busca y retorna una receta específica por su número consecutivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta encontrada"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    })
    public ResponseEntity<RecipeResponseDTO> obtenerRecetaPorConsecutivo(
            @Parameter(description = "Número consecutivo de la receta", example = "1")
            @PathVariable Long consecutivo) {
        log.info("GET /api/recetas/{} - Obteniendo receta por consecutivo", consecutivo);
        RecipeResponseDTO receta = recipeService.obtenerRecetaPorConsecutivo(consecutivo);
        return ResponseEntity.ok(receta);
    }
    
    @GetMapping("/participantes")
    @Operation(summary = "Obtener recetas de participantes", 
               description = "Retorna todas las recetas creadas por participantes del programa")
    @ApiResponse(responseCode = "200", description = "Lista de recetas de participantes")
    public ResponseEntity<List<RecipeResponseDTO>> obtenerRecetasParticipantes() {
        log.info("GET /api/recetas/participantes - Obteniendo recetas de participantes");
        List<RecipeResponseDTO> recetas = recipeService.obtenerRecetasPorTipo(ChefType.PARTICIPANTE);
        return ResponseEntity.ok(recetas);
    }
    
    @GetMapping("/televidentes")
    @Operation(summary = "Obtener recetas de televidentes", 
               description = "Retorna todas las recetas creadas por televidentes")
    @ApiResponse(responseCode = "200", description = "Lista de recetas de televidentes")
    public ResponseEntity<List<RecipeResponseDTO>> obtenerRecetasTelevidentes() {
        log.info("GET /api/recetas/televidentes - Obteniendo recetas de televidentes");
        List<RecipeResponseDTO> recetas = recipeService.obtenerRecetasPorTipo(ChefType.TELEVIDENTE);
        return ResponseEntity.ok(recetas);
    }
    
    @GetMapping("/chefs")
    @Operation(summary = "Obtener recetas de chefs jurados", 
               description = "Retorna todas las recetas creadas por chefs jurados del programa")
    @ApiResponse(responseCode = "200", description = "Lista de recetas de chefs jurados")
    public ResponseEntity<List<RecipeResponseDTO>> obtenerRecetasChefs() {
        log.info("GET /api/recetas/chefs - Obteniendo recetas de chefs jurados");
        List<RecipeResponseDTO> recetas = recipeService.obtenerRecetasPorTipo(ChefType.CHEF_JURADO);
        return ResponseEntity.ok(recetas);
    }
    
    @GetMapping("/temporada/{temporada}")
    @Operation(summary = "Obtener recetas por temporada", 
               description = "Retorna todas las recetas de participantes de una temporada específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de recetas de la temporada"),
        @ApiResponse(responseCode = "400", description = "Temporada inválida")
    })
    public ResponseEntity<List<RecipeResponseDTO>> obtenerRecetasPorTemporada(
            @Parameter(description = "Número de temporada", example = "5")
            @PathVariable Integer temporada) {
        log.info("GET /api/recetas/temporada/{} - Obteniendo recetas por temporada", temporada);
        List<RecipeResponseDTO> recetas = recipeService.obtenerRecetasPorTemporada(temporada);
        return ResponseEntity.ok(recetas);
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar recetas por ingrediente", 
               description = "Busca recetas que contengan un ingrediente específico (búsqueda no sensible a mayúsculas)")
    @ApiResponse(responseCode = "200", description = "Lista de recetas que contienen el ingrediente")
    public ResponseEntity<List<RecipeResponseDTO>> buscarPorIngrediente(
            @Parameter(description = "Ingrediente a buscar", example = "Papa")
            @RequestParam String ingrediente) {
        log.info("GET /api/recetas/buscar?ingrediente={} - Buscando recetas por ingrediente", ingrediente);
        List<RecipeResponseDTO> recetas = recipeService.buscarPorIngrediente(ingrediente);
        return ResponseEntity.ok(recetas);
    }
    
    @DeleteMapping("/{consecutivo}")
    @Operation(summary = "Eliminar receta", 
               description = "Elimina una receta por su número consecutivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Receta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    })
    public ResponseEntity<Void> eliminarReceta(
            @Parameter(description = "Número consecutivo de la receta a eliminar", example = "1")
            @PathVariable Long consecutivo) {
        log.info("DELETE /api/recetas/{} - Eliminando receta", consecutivo);
        recipeService.eliminarReceta(consecutivo);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{consecutivo}")
    @Operation(summary = "Actualizar receta", 
               description = "Actualiza todos los datos de una receta existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<RecipeResponseDTO> actualizarReceta(
            @Parameter(description = "Número consecutivo de la receta a actualizar", example = "1")
            @PathVariable Long consecutivo,
            @Valid @RequestBody RecipeRequestDTO request) {
        log.info("PUT /api/recetas/{} - Actualizando receta", consecutivo);
        RecipeResponseDTO receta = recipeService.actualizarReceta(consecutivo, request);
        return ResponseEntity.ok(receta);
    }
}