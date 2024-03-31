package opekope2.recipemanager.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Ingredient {
    private int amount;
    private String unit;
    private String ingredient;
}
