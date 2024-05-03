package opekope2.recipemanager.services;

import static opekope2.recipemanager.Util.RECIPES_COLLECTION_NAME;
import static opekope2.recipemanager.Util.USERS_COLLECTION_NAME;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import lombok.AllArgsConstructor;
import opekope2.recipemanager.data.Recipe;

@AllArgsConstructor
public class RecipeManagerService {
    private final FirebaseFirestore firestore;

    public Task<String> createRecipe(FirebaseUser user, Recipe recipe) {
        DocumentReference recipeDocument = firestore
                .collection(USERS_COLLECTION_NAME)
                .document(user.getUid())
                .collection(RECIPES_COLLECTION_NAME)
                .document();

        return recipeDocument.set(
                new Recipe(
                        recipeDocument.getId(),
                        recipe.getName(),
                        recipe.getDescription(),
                        recipe.getIngredients(),
                        recipe.getInstructions()
                )
        ).onSuccessTask(nothing -> Tasks.forResult(recipeDocument.getId()));
    }

    public Task<DocumentSnapshot> getRecipe(FirebaseUser user, String recipeId) {
        return firestore
                .collection(USERS_COLLECTION_NAME)
                .document(user.getUid())
                .collection(RECIPES_COLLECTION_NAME)
                .document(recipeId)
                .get();
    }

    public Task<QuerySnapshot> getRecipes(FirebaseUser user) {
        return firestore
                .collection(USERS_COLLECTION_NAME)
                .document(user.getUid())
                .collection(RECIPES_COLLECTION_NAME)
                .orderBy("name")
                .get();
    }

    public Task<Void> updateRecipe(FirebaseUser user, Recipe recipe) {
        assert recipe.getId() != null;
        return firestore
                .collection(USERS_COLLECTION_NAME)
                .document(user.getUid())
                .collection(RECIPES_COLLECTION_NAME)
                .document(recipe.getId())
                .set(recipe);
    }

    public Task<Void> deleteRecipe(FirebaseUser user, Recipe recipe) {
        assert recipe.getId() != null;
        return firestore
                .collection(USERS_COLLECTION_NAME)
                .document(user.getUid())
                .collection(RECIPES_COLLECTION_NAME)
                .document(recipe.getId())
                .delete();
    }
}
