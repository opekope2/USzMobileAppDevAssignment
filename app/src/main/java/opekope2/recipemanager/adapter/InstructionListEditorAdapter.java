package opekope2.recipemanager.adapter;

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

import opekope2.recipemanager.R;
import opekope2.recipemanager.data.Instruction;

public class InstructionListEditorAdapter extends AbstractInstructionListAdapter<InstructionListEditorAdapter.ViewHolder> {
    public InstructionListEditorAdapter(Context context, List<Instruction> instructions) {
        super(context, instructions);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.edit_instruction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instruction currentInstruction = instructions.get(position);
        holder.bind(currentInstruction);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final EditText editTextInstruction;
        private final ImageButton imageButtonDeleteInstruction;
        private Instruction instruction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editTextInstruction = itemView.findViewById(R.id.editTextInstruction);
            editTextInstruction.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    instruction.setInstruction(s.toString());
                    setDirty(true);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            imageButtonDeleteInstruction = itemView.findViewById(R.id.imageButtonDeleteInstruction);
            imageButtonDeleteInstruction.setOnClickListener(this);
        }

        public void bind(Instruction instruction) {
            this.instruction = instruction;

            boolean dirty = isDirty();
            editTextInstruction.setText(instruction.getInstruction());
            setDirty(dirty);
        }

        @Override
        public void onClick(View v) {
            if (v == imageButtonDeleteInstruction) {
                removeInstruction(instruction);
            }
        }
    }
}
