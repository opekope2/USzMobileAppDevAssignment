package opekope2.recipemanager.activity;

import static opekope2.recipemanager.Util.RECIPE_REFERENCE_EXTRA_KEY;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import opekope2.recipemanager.R;
import opekope2.recipemanager.data.Ingredient;
import opekope2.recipemanager.data.Recipe;
import opekope2.recipemanager.data.RecipeReference;

public class RecipeViewActivity extends AppCompatActivity {
    private RecipeReference recipeReference;
    private TextView textViewIngredients;
    private TextView textViewInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        if (!getIntent().hasExtra(RECIPE_REFERENCE_EXTRA_KEY)) {
            finish();
            return;
        }

        textViewIngredients = findViewById(R.id.textViewIngredients);
        textViewInstructions = findViewById(R.id.textViewInstructions);

        recipeReference = Objects.requireNonNull(getIntent().getParcelableExtra(RECIPE_REFERENCE_EXTRA_KEY));
        bind();
    }

    private void bind() {
        Recipe recipe = recipeReference.getRecipe();

        Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());

        StringBuilder ingredientListBuilder = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientListBuilder
                    .append(ingredient.getAmount())
                    .append(ingredient.getUnit())
                    .append(' ')
                    .append(ingredient.getIngredient())
                    .append('\n');
        }
        textViewIngredients.setText(ingredientListBuilder.toString());

        StringBuilder instructionListBuilder = new StringBuilder();
        for (String instruction : recipe.getInstructions()) {
            instructionListBuilder
                    .append(instruction)
                    .append('\n');
        }
        textViewInstructions.setText(instructionListBuilder.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editRecipe) {
            // TODO
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
