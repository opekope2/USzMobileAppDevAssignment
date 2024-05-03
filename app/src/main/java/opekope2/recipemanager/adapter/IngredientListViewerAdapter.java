package opekope2.recipemanager.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import opekope2.recipemanager.data.Ingredient;

public class IngredientListViewerAdapter extends AbstractIngredientListAdapter<IngredientListViewerAdapter.ViewHolder> {
    public IngredientListViewerAdapter(Context context, List<Ingredient> ingredients) {
        super(context, ingredients);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient currentIngredient = ingredients.get(position);
        holder.bind(currentIngredient);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewIngredient;

        public ViewHolder(@NonNull TextView itemView) {
            super(itemView);

            textViewIngredient = itemView;
        }

        public void bind(Ingredient ingredient) {
            textViewIngredient.setText(
                    String.format(
                            Locale.getDefault(),
                            "%s%s %s",
                            ingredient.getAmount(),
                            ingredient.getUnit(),
                            ingredient.getIngredient()
                    )
            );
        }
    }
}
