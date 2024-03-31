package opekope2.recipemanager.data;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Recipe {
    String name;
    Collection<Ingredient> ingredients;
    Collection<String> instructions;
}
