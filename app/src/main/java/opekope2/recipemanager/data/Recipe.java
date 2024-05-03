package opekope2.recipemanager.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Recipe implements Parcelable {
    @Nullable
    private String id;
    private String name;
    private String description;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;

    protected Recipe(Parcel in) {
        this(
                in.readString(),
                in.readString(),
                in.readString(),
                in.createTypedArrayList(Ingredient.CREATOR),
                in.createTypedArrayList(Instruction.CREATOR)
        );
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(instructions);
    }
}
