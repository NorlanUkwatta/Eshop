package lk.jiat.eshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

import lk.jiat.eshop.R;
import lk.jiat.eshop.databinding.ActivitySignUpBinding;
import lk.jiat.eshop.model.User;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.signUpLayoutSignInButton.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });

        binding.signUpLayoutSignUpButton.setOnClickListener(view -> {
            String name = binding.signUpName.getText().toString();
            String email = binding.signUpEmail.getText().toString();
            String password = binding.signUpPassword.getText().toString();
            String rePassword = binding.signUpRePassword.getText().toString();

            if (name.isEmpty()) {
                binding.signUpName.setError("Name is required");
                binding.signUpName.requestFocus();
            } else if (email.isEmpty()) {
                binding.signUpEmail.setError("Email is required");
                binding.signUpEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.signUpEmail.setError("Please enter a valid email");
                binding.signUpEmail.requestFocus();
            } else if (password.isEmpty()) {
                binding.signUpPassword.setError("Enter a password");
                binding.signUpPassword.requestFocus();
            } else if (password.length() < 6) {
                binding.signUpPassword.setError("Password should contain at least 6 characters");
                binding.signUpPassword.requestFocus();
            } else if (!Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$").matcher(password).matches()) {
                binding.signUpPassword.setError("Password is too weak");
                binding.signUpPassword.requestFocus();
            } else if (rePassword.isEmpty()) {
                binding.signUpRePassword.setError("Enter your password again");
                binding.signUpRePassword.requestFocus();
            } else if (!password.equals(rePassword)) {
                binding.signUpRePassword.setError("Password not matched");
                binding.signUpRePassword.requestFocus();
            } else {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            User user = User.builder().uid(uid).name(name).email(email).build();

                            //save the user in firestore
                            firebaseFirestore.collection("users")
                                    .document(uid)
                                    .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "Account successfully created ", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    }
                });
            }
        });

    }
}