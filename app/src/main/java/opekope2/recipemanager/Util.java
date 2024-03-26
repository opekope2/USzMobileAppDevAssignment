package opekope2.recipemanager;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

public final class Util {
    private Util() {
    }

    public static void alert(Context context, @StringRes int messageId) {
        new AlertDialog.Builder(context).setMessage(messageId).setPositiveButton(android.R.string.ok, (dialog, which) -> {
        }).show();
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
