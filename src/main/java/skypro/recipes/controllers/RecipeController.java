package skypro.recipes.controllers;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skypro.recipes.model.Ingredient;
import skypro.recipes.model.Recipe;
import skypro.recipes.service.RecipeService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;




@RestController
@RequestMapping("/Recipe")
@Tag(name = "Рецепты", description = "CRUD-операции и другие эндпоинты для работы с рецептами")

public class RecipeController {
    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<Long> addNewRecipe(@RequestBody Recipe recipe) {
        Long idRec = recipeService.addNewRecipe(recipe);
        return ResponseEntity.ok(idRec);
    }

    @GetMapping("/{idRec}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long idRec) {
        Recipe recipe1 = recipeService.getRecipe(idRec);
        if (recipe1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipe1);
    }



    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рецепты были найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Recipe.class))
                            )
                    }
            )
    }
  )
    public  ResponseEntity<Map<Long, Recipe>> getAllRecipe()  {
        Map<Long, Recipe> recipeL = recipeService.getAllRecipe();
        if (recipeL == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipeL);
    }


    @PutMapping("/{idRec}")
    @Parameters(value = {
            @Parameter(name = "idRec", example = "recipe")
    }
  )
    public ResponseEntity<Recipe> putRecipe(@PathVariable Long idRec, @RequestBody Recipe recipe) {
        Recipe recipe1 = recipeService.putRecipe(idRec, recipe);
        if (recipe1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipe1);
    }

    @DeleteMapping("/{idRec}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long idRec) {
        if (recipeService.deleteRecipe(idRec)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllRecipe() {
        recipeService.deleteAllRecipe();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/Ingredient")
    public ResponseEntity<Object> getRecipeRead(@RequestBody Ingredient ingredient) {
        try {
            Path path = recipeService.createRecipe(ingredient);
            if (Files.size(path) == 0) {
                return ResponseEntity.noContent().build();
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(Files.size(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ingredient + " -report.txt\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }


}
