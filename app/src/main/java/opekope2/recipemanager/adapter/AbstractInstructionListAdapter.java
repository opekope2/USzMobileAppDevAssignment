package opekope2.recipemanager.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class AbstractInstructionListAdapter<TViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<TViewHolder> {
    protected final Context context;
    @Getter
    protected final List<String> instructions;
    @Getter
    @Setter
    private boolean dirty;

    public void addInstruction(String instruction) {
        instructions.add(instruction);
        notifyItemInserted(instructions.size() - 1);
        setDirty(true);
    }

    public void removeInstruction(String instruction) {
        int index = instructions.indexOf(instruction);
        instructions.remove(index);
        notifyItemRemoved(index);
        setDirty(true);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }
}
