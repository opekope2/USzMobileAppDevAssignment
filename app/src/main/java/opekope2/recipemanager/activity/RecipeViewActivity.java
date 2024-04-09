package opekope2.recipemanager.activity;

import static opekope2.recipemanager.Util.RECIPE_DOCUMENT_ID_EXTRA_KEY;
import static opekope2.recipemanager.Util.RECIPE_EXTRA_KEY;

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

public class RecipeViewActivity extends AppCompatActivity {
    private String recipeDocumentId;
    private Recipe recipe;
    private TextView textViewIngredients;
    private TextView textViewInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        if (!getIntent().hasExtra(RECIPE_DOCUMENT_ID_EXTRA_KEY) || !getIntent().hasExtra(RECIPE_EXTRA_KEY)) {
            finish();
            return;
        }

        textViewIngredients = findViewById(R.id.textViewIngredients);
        textViewInstructions = findViewById(R.id.textViewInstructions);

        recipeDocumentId = getIntent().getStringExtra(RECIPE_DOCUMENT_ID_EXTRA_KEY);
        recipe = Objects.requireNonNull(getIntent().getParcelableExtra(RECIPE_EXTRA_KEY));
        bind();
    }

    private void bind() {
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
