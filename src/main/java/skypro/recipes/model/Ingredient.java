package skypro.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

    private String nameIngredient; // название ингредиента
    private int number; // количество
    private String measurement; // мера измерения

}
