package opekope2.recipemanager.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import opekope2.recipemanager.R;
import opekope2.recipemanager.adapter.RecipeListAdapter;
import opekope2.recipemanager.data.Recipe;

public class RecipeListActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private RecyclerView recyclerViewRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        user = auth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        List<Recipe> testData = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            testData.add(
                    new Recipe("Recipe " + i, Collections.emptyList(), Collections.emptyList())
            );
        }

        GridLayoutManager recipesLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.recipe_columns));
        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        recyclerViewRecipes.setLayoutManager(recipesLayoutManager);
        recyclerViewRecipes.setAdapter(new RecipeListAdapter(this, testData));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_list, menu);
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
