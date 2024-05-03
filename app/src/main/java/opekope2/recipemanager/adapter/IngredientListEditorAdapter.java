package opekope2.recipemanager.adapter;

import static opekope2.recipemanager.Util.isNullOrEmpty;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import opekope2.recipemanager.R;
import opekope2.recipemanager.data.Ingredient;

public class IngredientListEditorAdapter extends AbstractIngredientListAdapter<IngredientListEditorAdapter.ViewHolder> {
    public IngredientListEditorAdapter(Context context, List<Ingredient> ingredients) {
        super(context, ingredients);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.edit_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient currentIngredient = ingredients.get(position);
        holder.bind(currentIngredient);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final EditText editTextAmount;
        private final EditText editTextUnit;
        private final EditText editTextIngredient;
        private final ImageButton imageButtonDeleteIngredient;
        private Ingredient ingredient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editTextAmount = itemView.findViewById(R.id.editTextAmount);
            editTextAmount.addTextChangedListener(
                    new TextChangeListener(
                            s -> {
                                if (!isNullOrEmpty(s)) {
                                    ingredient.setAmount(Double.parseDouble(s));
                                }
                            }
                    )
            );

            editTextUnit = itemView.findViewById(R.id.editTextUnit);
            editTextUnit.addTextChangedListener(new TextChangeListener(s -> ingredient.setUnit(s)));

            editTextIngredient = itemView.findViewById(R.id.editTextIngredient);
            editTextIngredient.addTextChangedListener(new TextChangeListener(s -> ingredient.setIngredient(s)));

            imageButtonDeleteIngredient = itemView.findViewById(R.id.imageButtonDeleteIngredient);
            imageButtonDeleteIngredient.setOnClickListener(this);
        }

        public void bind(Ingredient ingredient) {
            this.ingredient = ingredient;

            boolean dirty = isDirty();
            editTextAmount.setText(String.format(Locale.getDefault(), "%s", ingredient.getAmount()));
            editTextUnit.setText(ingredient.getUnit());
            editTextIngredient.setText(ingredient.getIngredient());
            setDirty(dirty);
        }

        @Override
        public void onClick(View v) {
            if (v == imageButtonDeleteIngredient) {
                removeIngredient(ingredient);
            }
        }

        @AllArgsConstructor
        private class TextChangeListener implements TextWatcher {
            private Consumer<String> ingredientEditor;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredientEditor.accept(s.toString());
                setDirty(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        }
    }
}
