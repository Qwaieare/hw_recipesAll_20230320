package skypro.recipes.controllers;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skypro.recipes.model.Ingredient;
import skypro.recipes.service.IngredientService;

import java.util.Map;


@RestController
@RequestMapping("/Ingredient")
@Tag(name = "Ингредиенты", description = "Ингредиенты для составления рецептов")
public class IngredientController {
    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<Long> addNewIngredient(@RequestBody Ingredient ingredient) {
        Long idIng = ingredientService.addNewIngredient(ingredient);
        return ResponseEntity.ok(idIng);
    }

    @GetMapping("/{idIng}")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable Long idIng) {
        Ingredient ingredient1 = ingredientService.getIngredient(idIng);
        if (ingredient1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredient1 );
    }



    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ингредиенты были найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Ingredient.class))
                            )
                    }
            )
    }
   )
    public ResponseEntity<Map<Long, Ingredient>> getAllIngredient() {
        Map<Long, Ingredient> ingredientsL = ingredientService.getAllIngredient();
        if (ingredientsL == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredientsL);
    }



    @PutMapping("/{idIng}")
    @Parameters(value = {
            @Parameter(name = "idIng", example = "ingredient")
    }
    )
    public ResponseEntity<Ingredient> putIngredient(@PathVariable Long idIng, @RequestBody Ingredient ingredient) {
        Ingredient ingredient1 = ingredientService.putIngredient(idIng, ingredient);
        if (ingredient1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredient);
    }


    @DeleteMapping("/{idIng}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long idIng) {
        if (ingredientService.deleteIngredient(idIng)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }




}
