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
    public Long addNewRecipe(Recipe recipe) {
        recipeL.putIfAbsent(idRec, recipe);
        saveToFile();
        return idRec++;
    }

    @Override
    public Recipe getRecipe(Long idRec) {
        return recipeL.get(idRec);
    }

    @Override
    public Map<Long, Recipe> getAllRecipe() {
        return recipeL;
    }

    @Override
    public Recipe putRecipe(Long idRec, Recipe recipe) {
        recipeL.putIfAbsent(idRec, recipe);
        saveToFile();
        return recipe;
    }

    @Override
    public boolean deleteRecipe(Long idRec) {
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
    public Path createRecipe(Ingredient ingredient) {
        Recipe recipe =  recipeL.getOrDefault(idRec, new Recipe());
       Path path = filesService.createTempFile("suffix");

            try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.append(recipe.getNameOfRecipe() + recipe.getTame() + recipe.getIngredient() + recipe.getPreparationStep());
                writer.append("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return path;
        }



}

