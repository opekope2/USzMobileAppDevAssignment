package opekope2.recipemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.AllArgsConstructor;
import opekope2.recipemanager.R;
import opekope2.recipemanager.data.Recipe;

@AllArgsConstructor
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private final Context context;
    private final List<Recipe> recipes;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.recipe_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe current = recipes.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;
        private final TextView textViewRecipeName;
        private Recipe recipe;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;

            itemView.setOnClickListener(this);
            textViewRecipeName = itemView.findViewById(R.id.textViewRecipeName);
        }

        public void bind(Recipe recipe) {
            this.recipe = recipe;
            textViewRecipeName.setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {
            // TODO
        }
    }
}
