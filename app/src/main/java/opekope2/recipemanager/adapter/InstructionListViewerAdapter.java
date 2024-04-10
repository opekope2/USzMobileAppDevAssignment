package opekope2.recipemanager.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import opekope2.recipemanager.data.Instruction;

public class InstructionListViewerAdapter extends AbstractInstructionListAdapter<InstructionListViewerAdapter.ViewHolder> {
    public InstructionListViewerAdapter(Context context, List<Instruction> instructions) {
        super(context, instructions);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instruction currentInstruction = instructions.get(position);
        holder.bind(currentInstruction);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewInstruction;

        public ViewHolder(@NonNull TextView itemView) {
            super(itemView);

            textViewInstruction = itemView;
        }

        public void bind(Instruction instruction) {
            textViewInstruction.setText(instruction.getInstruction());
        }
    }
}
