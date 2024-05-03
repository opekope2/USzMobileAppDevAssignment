package opekope2.recipemanager.activity;

import static opekope2.recipemanager.Util.isNullOrEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.function.Consumer;

import opekope2.recipemanager.R;
import opekope2.recipemanager.fragment.LoginFragment;
import opekope2.recipemanager.fragment.RegisterFragment;
import opekope2.recipemanager.services.DialogService;

public class MainActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DialogService dialogService = new DialogService(this);

    private void configureTabs(TabLayout.Tab tab, int i) {
        tab.setText(LoginRegisterFragmentAdapter.TAB_TEXT_IDS[i]);
    }

    private void viewRecipes() {
        startActivity(new Intent(this, RecipeListActivity.class));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayoutMain = findViewById(R.id.tabLayoutMain);
        ViewPager2 viewPagerMain = findViewById(R.id.viewPagerMain);
        viewPagerMain.setAdapter(new LoginRegisterFragmentAdapter(getSupportFragmentManager(), getLifecycle()));
        new TabLayoutMediator(tabLayoutMain, viewPagerMain, this::configureTabs).attach();

        if (auth.getCurrentUser() != null) {
            viewRecipes();
        }
    }

    private void authenticate(View button, BiFunction<String, String, Task<AuthResult>> authMethod, Runnable onSuccess, Consumer<String> onFail) {
        EditText editTextUsername = findViewById(R.id.editTextEmail),
                editTextPassword = findViewById(R.id.editTextPassword);
        String username = editTextUsername.getText().toString(),
                password = editTextPassword.getText().toString();

        if (isNullOrEmpty(username) || isNullOrEmpty(password)) {
            onFail.accept(getString(R.string.empty_username_or_password));
            return;
        }

        button.setEnabled(false);
        authMethod.apply(username, password)
                .addOnSuccessListener(authResult -> {
                    button.setEnabled(true);
                    onSuccess.run();
                })
                .addOnFailureListener(this, exception -> {
                    button.setEnabled(true);
                    onFail.accept(exception.getMessage());
                });
    }

    public void logIn(View view) {
        authenticate(
                view,
                auth::signInWithEmailAndPassword,
                this::viewRecipes,
                error -> dialogService.alert(R.string.login_failed, error, android.R.string.ok, (dialog, which) -> {
                })
        );
    }

    public void register(View view) {
        EditText editTextPassword = findViewById(R.id.editTextPassword),
                editTextPassword2 = findViewById(R.id.editTextPassword2);
        String password = editTextPassword.getText().toString(),
                confirmPassword = editTextPassword2.getText().toString();

        if (!password.equals(confirmPassword)) {
            dialogService.alert(R.string.register_failed, getString(R.string.password_mismatch), android.R.string.ok, (dialog, which) -> {
            });
            return;
        }

        authenticate(
                view,
                auth::createUserWithEmailAndPassword,
                this::viewRecipes,
                error -> dialogService.alert(R.string.register_failed, error, android.R.string.ok, (dialog, which) -> {
                })
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
