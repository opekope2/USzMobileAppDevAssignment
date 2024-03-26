package opekope2.recipemanager.activity;

import android.os.Bundle;
import android.util.Log;
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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import opekope2.recipemanager.R;
import opekope2.recipemanager.fragment.LoginFragment;
import opekope2.recipemanager.fragment.RegisterFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayoutMain = findViewById(R.id.tabLayoutMain);
        ViewPager2 viewPagerMain = findViewById(R.id.viewPagerMain);
        viewPagerMain.setAdapter(new LoginRegisterFragmentAdapter(getSupportFragmentManager(), getLifecycle()));
        new TabLayoutMediator(tabLayoutMain, viewPagerMain, this::configureTabs).attach();
    }

    private void configureTabs(TabLayout.Tab tab, int i) {
        tab.setText(LoginRegisterFragmentAdapter.TAB_TEXT_IDS[i]);
    }

    public void logIn(View view) {
        EditText editTextUsername = findViewById(R.id.editTextUsername),
                editTextPassword = findViewById(R.id.editTextPassword);
        String username = editTextUsername.getText().toString(),
                password = editTextPassword.getText().toString();

        Log.d(TAG, "Trying to log in as " + username);
    }

    public void register(View view) {
        EditText editTextUsername = findViewById(R.id.editTextUsername),
                editTextPassword = findViewById(R.id.editTextPassword);
        String username = editTextUsername.getText().toString(),
                password = editTextPassword.getText().toString();

        Log.d(TAG, "Trying to register as " + username);
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
