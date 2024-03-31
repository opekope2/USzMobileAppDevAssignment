package opekope2.recipemanager.adapter;

import static opekope2.recipemanager.Util.RECIPE_REFERENCE_EXTRA_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import opekope2.recipemanager.data.RecipeReference;

@AllArgsConstructor
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private final Context context;
    @Getter
    private List<RecipeReference> recipes;

    @SuppressLint("NotifyDataSetChanged")
    public void updateRecipes(List<RecipeReference> recipes) {
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
        RecipeReference currentReference = recipes.get(position);
        holder.bind(currentReference);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;
        private final TextView textViewRecipeName;
        private RecipeReference recipeReference;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;

            itemView.setOnClickListener(this);
            textViewRecipeName = itemView.findViewById(R.id.textViewRecipeName);
        }

        public void bind(RecipeReference recipeReference) {
            this.recipeReference = recipeReference;
            Recipe recipe = recipeReference.getRecipe();
            textViewRecipeName.setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RecipeViewActivity.class);
            intent.putExtra(RECIPE_REFERENCE_EXTRA_KEY, recipeReference);
            context.startActivity(intent);
        }
    }
}
