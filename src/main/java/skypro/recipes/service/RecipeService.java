package skypro.recipes.service;
import skypro.recipes.model.Ingredient;
import skypro.recipes.model.Recipe;

import java.nio.file.Path;
import java.util.Map;

public interface RecipeService {
    Long addNewRecipe(Recipe recipe);
    Recipe getRecipe(Long idRec);

    Map<Long, Recipe> getAllRecipe();

    Recipe putRecipe(Long idRec, Recipe recipe);

    boolean deleteRecipe(Long idRec);


    boolean deleteAllRecipe();


    Path createRecipe();
}