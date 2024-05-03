package opekope2.recipemanager.services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import androidx.annotation.StringRes;

import java.util.function.Consumer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DialogService {
    private final Context context;

    public void alert(
            @StringRes int title,
            String message,
            @StringRes int okButtonText,
            DialogInterface.OnClickListener buttonClickListener
    ) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(okButtonText, buttonClickListener)
                .show();
    }

    public void confirm(
            @StringRes int title,
            @StringRes int message,
            @StringRes int yesButtonText,
            @StringRes int noButtonText,
            DialogInterface.OnClickListener buttonClickListener
    ) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(yesButtonText, buttonClickListener)
                .setNegativeButton(noButtonText, buttonClickListener)
                .setCancelable(false)
                .show();
    }

    public void prompt(
            @StringRes int title,
            @StringRes int message,
            @StringRes int yesButtonText,
            @StringRes int noButtonText,
            Consumer<String> callback
    ) {
        EditText editText = new EditText(context);
        editText.setHint(message);

        DialogInterface.OnClickListener buttonClickListener = (dialog, button) ->
                callback.accept(button == AlertDialog.BUTTON_POSITIVE ? editText.getText().toString() : null);

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton(yesButtonText, buttonClickListener)
                .setNegativeButton(noButtonText, buttonClickListener)
                .setCancelable(false)
                .show();
    }

    public ProgressDialog progress(@StringRes int message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(message));
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }
}
