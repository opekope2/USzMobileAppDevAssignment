package opekope2.recipemanager.activity;

import static opekope2.recipemanager.Util.RECIPE_ID_EXTRA_KEY;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import opekope2.recipemanager.R;
import opekope2.recipemanager.adapter.RecipeListAdapter;
import opekope2.recipemanager.data.Recipe;
import opekope2.recipemanager.services.DialogService;
import opekope2.recipemanager.services.RecipeManagerService;

public class RecipeListActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private final RecipeManagerService recipeManager = new RecipeManagerService(firestore);
    private final DialogService dialogService = new DialogService(this);

    private SwipeRefreshLayout swipeRefreshRecipeList;
    private RecyclerView recyclerViewRecipes;
    private RecipeListAdapter recipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        user = auth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        GridLayoutManager recipesLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.recipe_columns));
        recipesAdapter = new RecipeListAdapter(this, Collections.emptyList());
        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        recyclerViewRecipes.setLayoutManager(recipesLayoutManager);
        recyclerViewRecipes.setAdapter(recipesAdapter);

        swipeRefreshRecipeList = findViewById(R.id.swipeRefreshRecipeList);
        swipeRefreshRecipeList.setOnRefreshListener(this::loadRecipes);
    }

    @Override
    protected void onStart() {
        super.onStart();

        swipeRefreshRecipeList.setRefreshing(true);
        loadRecipes();
    }

    private void loadRecipes() {
        recipeManager.getRecipes(user)
                .addOnSuccessListener(result -> {
                    List<Recipe> recipes = StreamSupport.stream(result.spliterator(), false)
                            .map(doc -> doc.toObject(Recipe.class))
                            .collect(Collectors.toList());
                    recipesAdapter.updateRecipes(recipes);

                    swipeRefreshRecipeList.setRefreshing(false);
                })
                .addOnFailureListener(exception -> {
                    dialogService.toast(exception.getMessage());
                    swipeRefreshRecipeList.setRefreshing(false);
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Thanks Google for removing switch-case, I hate it
        if (item.getItemId() == R.id.addRecipe) {
            dialogService.prompt(R.string.create_recipe, R.string.recipe_name, R.string.create_recipe, android.R.string.cancel, result -> {
                if (result == null) return;

                ProgressDialog progressDialog = dialogService.progress(R.string.creating_recipe);
                recipeManager.createRecipe(user, new Recipe(null, result, "", Collections.emptyList(), Collections.emptyList()))
                        .addOnSuccessListener(id -> {
                            progressDialog.dismiss();
                            Intent intent = new Intent(this, RecipeEditActivity.class);
                            intent.putExtra(RECIPE_ID_EXTRA_KEY, id);
                            startActivity(intent);
                        })
                        .addOnFailureListener(error -> {
                            progressDialog.dismiss();
                            dialogService.alert(R.string.recipe_creation_failed, error.getMessage(), android.R.string.ok, (dialog, which) -> {
                            });
                        });
            });
            return true;
        } else if (item.getItemId() == R.id.logOut) {
            auth.signOut();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
