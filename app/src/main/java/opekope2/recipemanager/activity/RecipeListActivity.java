package opekope2.recipemanager.activity;

import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import opekope2.recipemanager.services.RecipeManagerService;

public class RecipeListActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private final RecipeManagerService recipeManager = new RecipeManagerService(firestore);
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

        swipeRefreshRecipeList.setRefreshing(true);
        loadRecipes();
    }

    private void loadRecipes() {
        recipeManager.getRecipes(user)
                .addOnSuccessListener(result -> {
                    List<Pair<String, Recipe>> recipes = StreamSupport.stream(result.spliterator(), false)
                            .map(doc -> new Pair<>(doc.getId(), doc.toObject(Recipe.class)))
                            .collect(Collectors.toList());
                    recipesAdapter.updateRecipes(recipes);

                    swipeRefreshRecipeList.setRefreshing(false);
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(RecipeListActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
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
            // TODO
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
