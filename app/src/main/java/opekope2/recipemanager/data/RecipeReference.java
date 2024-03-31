package opekope2.recipemanager.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecipeReference implements Parcelable {
    private String documentId;
    private Recipe recipe;

    protected RecipeReference(Parcel in) {
        this(
                in.readString(),
                in.readParcelable(Recipe.class.getClassLoader())
        );
    }

    public static final Creator<RecipeReference> CREATOR = new Creator<RecipeReference>() {
        @Override
        public RecipeReference createFromParcel(Parcel in) {
            return new RecipeReference(in);
        }

        @Override
        public RecipeReference[] newArray(int size) {
            return new RecipeReference[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeParcelable(recipe, flags);
    }
}
