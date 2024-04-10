package opekope2.recipemanager.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import opekope2.recipemanager.data.Ingredient;

@RequiredArgsConstructor
public abstract class AbstractIngredientListAdapter<TViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<TViewHolder> {
    protected final Context context;
    @Getter
    protected final List<Ingredient> ingredients;
    @Getter
    @Setter
    private boolean dirty = false;

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        notifyItemInserted(ingredients.size() - 1);
        setDirty(true);
    }

    public void removeIngredient(Ingredient ingredient) {
        int index = ingredients.indexOf(ingredient);
        ingredients.remove(index);
        notifyItemRemoved(index);
        setDirty(true);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
