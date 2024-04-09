package opekope2.recipemanager.adapter;

import static opekope2.recipemanager.Util.RECIPE_DOCUMENT_ID_EXTRA_KEY;
import static opekope2.recipemanager.Util.RECIPE_EXTRA_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import opekope2.recipemanager.R;
import opekope2.recipemanager.activity.RecipeViewActivity;
import opekope2.recipemanager.data.Recipe;

@AllArgsConstructor
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private final Context context;
    @Getter
    private List<Pair<String, Recipe>> recipes;

    @SuppressLint("NotifyDataSetChanged")
    public void updateRecipes(List<Pair<String, Recipe>> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.recipe_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, Recipe> currentReference = recipes.get(position);
        holder.bind(currentReference.first, currentReference.second);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;
        private final TextView textViewRecipeName;
        private String recipeDocumentId;
        private Recipe recipe;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;

            itemView.setOnClickListener(this);
            textViewRecipeName = itemView.findViewById(R.id.textViewRecipeName);
        }

        public void bind(String recipeDocumentId, Recipe recipe) {
            this.recipeDocumentId = recipeDocumentId;
            this.recipe = recipe;

            textViewRecipeName.setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RecipeViewActivity.class);
            intent.putExtra(RECIPE_DOCUMENT_ID_EXTRA_KEY, recipeDocumentId);
            intent.putExtra(RECIPE_EXTRA_KEY, recipe);
            context.startActivity(intent);
        }
    }
}
