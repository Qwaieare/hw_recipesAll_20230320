package skypro.recipes.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import skypro.recipes.model.Ingredient;
import skypro.recipes.model.Recipe;
import skypro.recipes.service.FilesService;
import skypro.recipes.service.RecipeService;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {
    private static Map<Long, Recipe> recipeL = new TreeMap<>();
    private static Long idRec = 0L;
    private final FilesService filesService;

    public RecipeServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }


    @Override
    public Long addNewRecipe(Recipe recipe) { //Создаем новый рецепт
        recipeL.putIfAbsent(idRec, recipe);
        saveToFile();
        return idRec++;
    }

    @Override
    public Recipe getRecipe(Long idRec) {
        return recipeL.get(idRec);
    } //Получаем рецепт по его id

    @Override
    public Map<Long, Recipe> getAllRecipe() {
        return recipeL;
    } //Получаем список всех рецептов

    @Override
    public Recipe putRecipe(Long idRec, Recipe recipe) {   //Редактируем рецепт по его id
        if (recipeL.containsKey(idRec)) {
            recipeL.putIfAbsent(idRec, recipe);
            saveToFile();
            return recipe;
        }
        return null;
    }

    @Override
    public boolean deleteRecipe(Long idRec) { //Удаляем рецепт по его id
       saveToFile();
        return  recipeL.remove(idRec) != null;
    }

    @Override
    public boolean deleteAllRecipe() {
        recipeL = new TreeMap<>();
        saveToFile();
        return false;
    }

    // методы для json
    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(recipeL);
            filesService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private void readFromFile() {
        String json = filesService.readFromFile();
        try {
            recipeL = new ObjectMapper().readValue(json, new TypeReference<TreeMap<Long, Recipe>>() {
            });
        } catch (JsonMappingException e) {

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void init () {
        readFromFile();
    }


    @Override
    public Path createRecipe() {
        Path path = filesService.createTempFile("allRecipes");

        try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            String symbol = " - ";
            for (Recipe recipe : recipeL.values()) {
                writer.append("\n").append(recipe.getNameOfRecipe()).append("\n");
                writer.append("\n Время приготовления: " + recipe.getTame() + recipe.getPreparationStep());
                writer.append("\n Ингредиенты: \n");

                for (Ingredient ingredient : recipe.getIngredient()) {
                    writer.append(symbol).append(ingredient.getNameIngredient() + " " +
                            ingredient.getNumber() +
                            ingredient.getMeasurement() + "\n");
                }
                writer.append("\n Инструкция приготовления: \n");

                for (String preparationStep : recipe.getPreparationStep()) {
                    writer.append(symbol).append(preparationStep).append("\n");
                }
                writer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
        }




}

