package com.example.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import static  com.example.bloodbank.MainActivity.mAuth;

public class LoginActivity extends AppCompatActivity {
EditText email,pass;
TextView feedback;
Button signup,login;
FirebaseUser user;
//Intent i=getIntent();
//    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        mAuth = FirebaseAuth.getInstance();

//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build());
//
//// Create and launch sign-in intent
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                123);

        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);

        login=findViewById(R.id.login);
//        if(i.getStringExtra("status").equals("signup")) {
//            login.setText("Sign Up");
//        }else
//            login.setText("Sign in");

        feedback=findViewById(R.id.feedback);
        signup=findViewById(R.id.signup);


        login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
                 mAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                         .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()) {
                                     // Sign in: success
                                     // update UI for current User
                                     startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
//                                     FirebaseUser user = mAuth.getCurrentUser();
//                                     updateUI(user,Email);
                                 } else {
                                     // Sign in: fail
                                                            feedback.setText("invalid user");
//                                                            updateUI(null);
                                 }

                                 // ...
                             }
                         });
                 }

      });

    }
//    @Override
//    public void onStart() {
//        // ...
//
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        email.setText("");
        pass.setText("");
    }

    public void signup(View view) {
        startActivity(new Intent(this,SignUp.class));
    }
}
















//login.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
////                Task<QuerySnapshot> query=db.collection("users")
////                        .whereEqualTo("email",email.getText().toString())
////                        .whereEqualTo("password",pass.getText().toString())
////                        .get()
////                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////                            @Override
////                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////                                if (!queryDocumentSnapshots.isEmpty()) {
////
////                                }
////
////
////                            }})
////                        .addOnFailureListener(new OnFailureListener() {
////                            @Override
////                            public void onFailure(@NonNull Exception e) {
////                                TextView t=findViewById(R.id.feedback);
////                                t.setText("incorrect email or password");
////                            }
////                        }).
////            #    db.collection("users")
////                        .whereEqualTo("email",email.getText().toString())
////                         .whereEqualTo("password",pass.getText().toString())
////                        .get()
////                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                            @Override
////                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                                if (task.isSuccessful()&&!task.getResult().isEmpty()) {
////                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
//
////                                } else {
////                                    TextView t=findViewById(R.id.feedback);
////                                     t.setText("incorrect email or password");
//////                                    Log.d(TAG, "Error getting documents: ", task.getException());
////                                }
////                            }
////           #             });
//
////              #          Log.d("query output",query.toString());
//
//        }
//        });
//
////        signup.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent i = new Intent(LoginActivity.this,SignUp.class);
////                startActivity(i);
////            }
////        });
//
//        }
//
//public void signup(View view) {
//        Intent i = new Intent(this,SignUp.class);
//        startActivity(i);
//        }
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (requestCode == 123) {
////            IdpResponse response = IdpResponse.fromResultIntent(data);
////
////            if (resultCode == RESULT_OK) {
////                // Successfully signed in
////                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////                // ...
////            } else {
////                // Sign in failed. If response is null the user canceled the
////                // sign-in flow using the back button. Otherwise check
////                // response.getError().getErrorCode() and handle the error.
////                // ...
////            }
////        }
//
//        }