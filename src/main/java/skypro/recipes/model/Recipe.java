package skypro.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    private String nameOfRecipe; // название рецепта
    private int tame; // время приготовления
    private List<Ingredient> ingredient; // список ингредиентов
    private List<String> preparationStep; // шаги приготовления


}
