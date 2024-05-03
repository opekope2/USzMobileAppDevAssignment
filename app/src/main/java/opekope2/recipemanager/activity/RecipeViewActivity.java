package opekope2.recipemanager.activity;

import static opekope2.recipemanager.Util.RECIPE_ID_EXTRA_KEY;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import opekope2.recipemanager.R;
import opekope2.recipemanager.adapter.IngredientListViewerAdapter;
import opekope2.recipemanager.adapter.InstructionListViewerAdapter;
import opekope2.recipemanager.data.Recipe;
import opekope2.recipemanager.services.DialogService;
import opekope2.recipemanager.services.RecipeManagerService;

public class RecipeViewActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private final RecipeManagerService recipeManager = new RecipeManagerService(firestore);
    private final DialogService dialogService = new DialogService(this);

    private String recipeId;
    private Recipe recipe;
    private TextView textViewDescription;
    private RecyclerView recyclerViewIngredients;
    private RecyclerView recyclerViewInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        user = auth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        if (!getIntent().hasExtra(RECIPE_ID_EXTRA_KEY)) {
            finish();
            return;
        }

        textViewDescription = findViewById(R.id.textViewDescription);

        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(this);
        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        recyclerViewIngredients.setLayoutManager(ingredientsLayoutManager);

        RecyclerView.LayoutManager instructionsLayoutManager = new LinearLayoutManager(this);
        recyclerViewInstructions = findViewById(R.id.recyclerViewInstructions);
        recyclerViewInstructions.setLayoutManager(instructionsLayoutManager);

        recipeId = getIntent().getStringExtra(RECIPE_ID_EXTRA_KEY);

        ProgressDialog progressDialog = dialogService.progress(R.string.loading_recipe);
        recipeManager.getRecipe(user, recipeId)
                .addOnSuccessListener(documentSnapshot -> {
                    recipe = documentSnapshot.toObject(Recipe.class);
                    progressDialog.dismiss();
                    bind();
                })
                .addOnFailureListener(
                        exception -> dialogService.alert(
                                R.string.recipe_loading_failed,
                                exception.getMessage(),
                                android.R.string.ok,
                                (dialog, which) -> finish()
                        )
                );
    }

    private void bind() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());

        textViewDescription.setText(recipe.getDescription());
        recyclerViewIngredients.setAdapter(new IngredientListViewerAdapter(this, recipe.getIngredients()));
        recyclerViewInstructions.setAdapter(new InstructionListViewerAdapter(this, recipe.getInstructions()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_view, menu);
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
