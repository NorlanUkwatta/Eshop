package lk.jiat.eshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lk.jiat.eshop.R;
import lk.jiat.eshop.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
//        findViewById(R.id.sign_in_layout_sign_up_button).setOnClickListener(view -> { //This can be null. Instead that can use viewBinding
//            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
//            startActivity(intent);
//        });

        binding.signInLayoutSignUpButton.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            finish();
        });

        binding.signInLayoutSignInButton.setOnClickListener(view -> {
            String email = binding.signInEmail.getText().toString();
            String password = binding.signInPassword.getText().toString();

            if (email.isEmpty()) {
                binding.signInEmail.setError("Email is required");
                binding.signInEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.signInPassword.setError("Enter valid email");
                binding.signInPassword.requestFocus();
            } else if (password.isEmpty()) {
                binding.signInPassword.setError("Password required");
                binding.signInPassword.requestFocus();
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    updateUI(firebaseAuth.getCurrentUser());
                                } else {
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                                }
                            }
                        });
            }

        });

    }

    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }

}