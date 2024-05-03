package opekope2.recipemanager.activity;

import static opekope2.recipemanager.Util.RECIPE_ID_EXTRA_KEY;
import static opekope2.recipemanager.Util.isNullOrEmpty;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.BiConsumer;

import lombok.AllArgsConstructor;
import opekope2.recipemanager.R;
import opekope2.recipemanager.adapter.IngredientListEditorAdapter;
import opekope2.recipemanager.adapter.InstructionListEditorAdapter;
import opekope2.recipemanager.data.Ingredient;
import opekope2.recipemanager.data.Instruction;
import opekope2.recipemanager.data.Recipe;
import opekope2.recipemanager.services.DialogService;
import opekope2.recipemanager.services.RecipeManagerService;

public class RecipeEditActivity extends AppCompatActivity {
    private static final String RECIPE_EXTRA_KEY = "Recipe";

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private final RecipeManagerService recipeManager = new RecipeManagerService(firestore);
    private final DialogService dialogService = new DialogService(this);
    private final OnBackPressedCallback backButtonCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            dialogService.confirm(R.string.unsaved, R.string.discard, android.R.string.cancel, (dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    returnToRecipeViewActivity();
                }
            });
        }
    };

    private EditText editTextRecipeName;
    private EditText editTextRecipeDescription;
    private RecyclerView recyclerViewIngredients;
    private RecyclerView recyclerViewInstructions;
    private EditText editTextInstruction;
    private ImageButton imageButtonAddInstruction;
    private EditText editTextAmount;
    private EditText editTextUnit;
    private EditText editTextIngredient;
    private ImageButton imageButtonAddIngredient;
    private IngredientListEditorAdapter ingredientsAdapter;
    private InstructionListEditorAdapter instructionsAdapter;

    private String recipeId;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit);

        user = auth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextRecipeDescription = findViewById(R.id.editTextRecipeDescription);

        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(this);
        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        recyclerViewIngredients.setLayoutManager(ingredientsLayoutManager);

        RecyclerView.LayoutManager instructionsLayoutManager = new LinearLayoutManager(this);
        recyclerViewInstructions = findViewById(R.id.recyclerViewInstructions);
        recyclerViewInstructions.setLayoutManager(instructionsLayoutManager);

        editTextInstruction = findViewById(R.id.editTextInstruction);

        imageButtonAddInstruction = findViewById(R.id.imageButtonAddInstruction);
        imageButtonAddInstruction.setOnClickListener(this::addInstruction);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextIngredient = findViewById(R.id.editTextIngredient);

        imageButtonAddIngredient = findViewById(R.id.imageButtonAddIngredient);
        imageButtonAddIngredient.setOnClickListener(this::addIngredient);

        editTextRecipeName.addTextChangedListener(new RecipeEditorTextWatcherListener(Recipe::setName));
        editTextRecipeDescription.addTextChangedListener(new RecipeEditorTextWatcherListener(Recipe::setDescription));

        getOnBackPressedDispatcher().addCallback(this, backButtonCallback);

        recipeId = getIntent().getStringExtra(RECIPE_ID_EXTRA_KEY);
        ProgressDialog progressDialog = dialogService.progress(R.string.loading_recipe);
        recipeManager.getRecipe(user, recipeId)
                .addOnSuccessListener(documentSnapshot -> {
                    recipe = documentSnapshot.toObject(Recipe.class);
                    progressDialog.dismiss();
                    bind();
                })
                .addOnFailureListener(exception -> {
                    progressDialog.dismiss();
                    dialogService.alert(
                            R.string.recipe_loading_failed,
                            exception.getMessage(),
                            android.R.string.ok,
                            (dialog, which) -> finish()
                    );
                });
    }

    private void bind() {
        editTextRecipeName.setText(recipe.getName());

        ingredientsAdapter = new IngredientListEditorAdapter(this, recipe.getIngredients());
        instructionsAdapter = new InstructionListEditorAdapter(this, recipe.getInstructions());

        recyclerViewIngredients.setAdapter(ingredientsAdapter);
        recyclerViewInstructions.setAdapter(instructionsAdapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RECIPE_EXTRA_KEY, recipe);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        recipe = savedInstanceState.getParcelable(RECIPE_EXTRA_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if (isNullOrEmpty(recipe.getName())) {
                dialogService.toast(R.string.recipe_name_empty);
                return true;
            }

            ProgressDialog progressDialog = dialogService.progress(R.string.saving_recipe);
            recipeManager.updateRecipe(user, recipe)
                    .addOnSuccessListener(asd -> {
                        progressDialog.dismiss();
                        dialogService.toast(R.string.recipe_saved);
                        returnToRecipeViewActivity();
                    })
                    .addOnFailureListener(error -> {
                        progressDialog.dismiss();
                        dialogService.toast(error.getMessage());
                    });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnToRecipeViewActivity() {
        Intent intent = new Intent(this, RecipeViewActivity.class);
        intent.putExtra(RECIPE_ID_EXTRA_KEY, recipeId);
        startActivity(intent);
        finish();
    }

    private void addIngredient(View v) {
        String amountString = editTextAmount.getText().toString();
        if (isNullOrEmpty(amountString)) {
            dialogService.toast(R.string.amount_empty);
            return;
        }
        double amount = Double.parseDouble(amountString);
        String unit = editTextUnit.getText().toString();
        if (isNullOrEmpty(unit)) {
            dialogService.toast(R.string.unit_empty);
            return;
        }
        String ingredient = editTextIngredient.getText().toString();
        if (isNullOrEmpty(ingredient)) {
            dialogService.toast(R.string.ingredient_empty);
            return;
        }

        ingredientsAdapter.addIngredient(new Ingredient(amount, unit, ingredient));

        editTextAmount.setText("");
        editTextUnit.setText("");
        editTextIngredient.setText("");
    }

    private void addInstruction(View v) {
        String instruction = editTextInstruction.getText().toString();
        if (isNullOrEmpty(instruction)) {
            dialogService.toast(R.string.instruction_empty);
            return;
        }

        instructionsAdapter.addInstruction(new Instruction(instruction));
        editTextInstruction.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        dialogService.confirm(R.string.unsaved, R.string.discard, android.R.string.cancel, (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                super.onSupportNavigateUp();
            }
        });
        return false;
    }

    @AllArgsConstructor
    private class RecipeEditorTextWatcherListener implements TextWatcher {
        private final BiConsumer<Recipe, String> recipePropertySetter;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            recipePropertySetter.accept(recipe, s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
