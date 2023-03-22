package skypro.recipes.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import skypro.recipes.model.Ingredient;
import skypro.recipes.model.Recipe;
import skypro.recipes.service.FilesService;
import skypro.recipes.service.IngredientService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.TreeMap;

@Service
public class IngredientServiceImpl implements IngredientService {

    private static Map<Long, Ingredient> ingredientsL = new TreeMap<>();
    private static Long idIng = 0L;
    private final FilesService filesService;

    public IngredientServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

@Override
    public Long addNewIngredient(Ingredient ingredient) {
        ingredientsL.putIfAbsent(idIng, ingredient);
        saveToFile1();
        return idIng++;
    }

@Override
    public Ingredient getIngredient(Long idIng) {
        saveToFile1();
        return ingredientsL.get(idIng);
    }

@Override
    public Map<Long, Ingredient> getAllIngredient() {
        return ingredientsL;
    }

@Override
    public Ingredient putIngredient(Long idIng, Ingredient ingredient) {
        ingredientsL.putIfAbsent(idIng, ingredient);
        saveToFile1();
        return ingredient;
    }

@Override
    public boolean deleteIngredient(Long idIng) {
        saveToFile1();
        return ingredientsL.remove(idIng) != null;
    }
@Override
    public boolean deleteAllIngredient() {
        saveToFile1();
        ingredientsL = new TreeMap<>();
        return false;
    }

    // методы для json
    private void saveToFile1() {
        try {
            String json = new ObjectMapper().writeValueAsString(ingredientsL);
            filesService.saveToFile1(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile1() {
        String json = filesService.readFromFile1();
        try {
            ingredientsL = new ObjectMapper().readValue(json, new TypeReference<TreeMap<Long, Ingredient>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void init () {
        readFromFile1();
    }





}
