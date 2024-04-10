package opekope2.recipemanager.activity;

import static opekope2.recipemanager.Util.RECIPE_DOCUMENT_ID_EXTRA_KEY;
import static opekope2.recipemanager.Util.RECIPE_EXTRA_KEY;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import opekope2.recipemanager.R;
import opekope2.recipemanager.adapter.IngredientListViewerAdapter;
import opekope2.recipemanager.adapter.InstructionListViewerAdapter;
import opekope2.recipemanager.data.Recipe;

public class RecipeViewActivity extends AppCompatActivity {
    private String recipeDocumentId;
    private Recipe recipe;
    private RecyclerView recyclerViewIngredients;
    private RecyclerView recyclerViewInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        if (!getIntent().hasExtra(RECIPE_DOCUMENT_ID_EXTRA_KEY) || !getIntent().hasExtra(RECIPE_EXTRA_KEY)) {
            finish();
            return;
        }

        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(this);
        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        recyclerViewIngredients.setLayoutManager(ingredientsLayoutManager);

        RecyclerView.LayoutManager instructionsLayoutManager = new LinearLayoutManager(this);
        recyclerViewInstructions = findViewById(R.id.recyclerViewInstructions);
        recyclerViewInstructions.setLayoutManager(instructionsLayoutManager);

        recipeDocumentId = getIntent().getStringExtra(RECIPE_DOCUMENT_ID_EXTRA_KEY);
        recipe = Objects.requireNonNull(getIntent().getParcelableExtra(RECIPE_EXTRA_KEY));
        bind();
    }

    private void bind() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());

        recyclerViewIngredients.setAdapter(new IngredientListViewerAdapter(this, recipe.getIngredients()));
        recyclerViewInstructions.setAdapter(new InstructionListViewerAdapter(this, recipe.getInstructions()));
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
