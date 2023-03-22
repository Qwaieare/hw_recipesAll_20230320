package skypro.recipes.controllers;
import io.swagger.v3.oas.annotations.Operation;
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
import java.time.LocalDateTime;
import java.util.Map;




@RestController
@RequestMapping("/Recipe")
@Tag(name = "Рецепты", description = "CRUD-операции и другие эндпоинты для работы с рецептами")

public class RecipeController {
    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @Operation(
            summary = "Создаем новый рецепт"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рецепт был добавлен"
            )})
    @PostMapping
    public ResponseEntity<Long> addNewRecipe(@RequestBody Recipe recipe) { // создаем новый рецепт
        Long idRec = recipeService.addNewRecipe(recipe);
        return ResponseEntity.ok(idRec);
    }


    @Operation(
            summary = "Получаем рецепт по его id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рецепт найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Рецепт не найден"
            ) } )
    @GetMapping("/{idRec}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long idRec) { // получаем рецепт по его id
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
                            ) } ) } )
    public  ResponseEntity<Map<Long, Recipe>> getAllRecipe()  { //Получаем список всех рецептов
        Map<Long, Recipe> recipeL = recipeService.getAllRecipe();
        if (recipeL == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipeL);
    }


    @Operation(
            summary = "Редактируем рецепт по его id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рецепт был отредактирован"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Рецепт не отредактирован"
            )})
    @PutMapping("/{idRec}")
    public ResponseEntity<Recipe> putRecipe(@PathVariable Long idRec, @RequestBody Recipe recipe) { //Редактируем рецепт по его id
         recipe = recipeService.putRecipe(idRec, recipe);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipe);
    }



    @Operation(
            summary = "Удаляем рецепт по его id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рецепт был удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Рецепт не удален"
            )})
    @DeleteMapping("/{idRec}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long idRec) { //Удаляем рецепт по его id
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

    @Operation(
            summary = "Загружаем список рецептов в формате txt"
    )
    @GetMapping(value = "/getAllRecipe")
    public ResponseEntity<Object> getRecipeRead() {
        try {
            Path path = recipeService.createRecipe();
            if (Files.size(path) == 0) {   //Если файл пустой
                return ResponseEntity.noContent().build();   //Статус 204
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));  //Берем у файла входной поток, заворачиваем его в ресурс
            return ResponseEntity.ok()    //Формируем и возвращаем HTTP ответ
                    .contentType(MediaType.TEXT_PLAIN)    //Задаем тип файла
                    .contentLength(Files.size(path))    //Узнаем длину файла
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + LocalDateTime.now() + "-report.txt\"")  //Задаем название файла
                    .body(resource);

        } catch (IOException e) {       //При исключении отправляем код
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString()); // код 500
        }
    }


}
