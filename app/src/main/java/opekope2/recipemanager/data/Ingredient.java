package opekope2.recipemanager.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Ingredient implements Parcelable {
    private int amount;
    private String unit;
    private String ingredient;

    protected Ingredient(Parcel in) {
        this(
                in.readInt(),
                in.readString(),
                in.readString()
        );
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(amount);
        dest.writeString(unit);
        dest.writeString(ingredient);
    }
}
