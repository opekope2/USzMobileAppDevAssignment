package opekope2.recipemanager.activity;

import static opekope2.recipemanager.Util.isNullOrEmpty;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.function.BiFunction;

import opekope2.recipemanager.R;
import opekope2.recipemanager.fragment.LoginFragment;
import opekope2.recipemanager.fragment.RegisterFragment;

public class MainActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private void configureTabs(TabLayout.Tab tab, int i) {
        tab.setText(LoginRegisterFragmentAdapter.TAB_TEXT_IDS[i]);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayoutMain = findViewById(R.id.tabLayoutMain);
        ViewPager2 viewPagerMain = findViewById(R.id.viewPagerMain);
        viewPagerMain.setAdapter(new LoginRegisterFragmentAdapter(getSupportFragmentManager(), getLifecycle()));
        new TabLayoutMediator(tabLayoutMain, viewPagerMain, this::configureTabs).attach();
    }

    private void handleAuth(View button, BiFunction<String, String, Task<AuthResult>> authMethod, Runnable onSuccess, Runnable onFail) {
        EditText editTextUsername = findViewById(R.id.editTextEmail),
                editTextPassword = findViewById(R.id.editTextPassword);
        String username = editTextUsername.getText().toString(),
                password = editTextPassword.getText().toString();

        if (isNullOrEmpty(username) || isNullOrEmpty(password)) {
            onFail.run();
            return;
        }

        button.setEnabled(false);
        authMethod.apply(username, password).addOnCompleteListener(this, task -> {
            button.setEnabled(true);
            if (task.isSuccessful()) {
                onSuccess.run();
            } else {
                onFail.run();
            }
        });
    }

    private void alert(@StringRes int messageId) {
        new AlertDialog.Builder(this)
                .setMessage(messageId)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                })
                .show();
    }

    public void logIn(View view) {
        handleAuth(
                view,
                auth::signInWithEmailAndPassword,
                () -> Toast.makeText(this, "Login successful, to do: continue", Toast.LENGTH_LONG).show(),
                () -> alert(R.string.login_failed)
        );
    }

    public void register(View view) {
        handleAuth(
                view,
                auth::createUserWithEmailAndPassword,
                () -> Toast.makeText(this, "Registration successful, to do: continue", Toast.LENGTH_LONG).show(),
                () -> alert(R.string.register_failed)
        );
    }

    private static class LoginRegisterFragmentAdapter extends FragmentStateAdapter {
        public static final int[] TAB_TEXT_IDS = {R.string.log_in, R.string.register};

        private final Fragment[] fragments = {new LoginFragment(), new RegisterFragment()};

        public LoginRegisterFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments[position];
        }

        @Override
        public int getItemCount() {
            return fragments.length;
        }
    }
}
