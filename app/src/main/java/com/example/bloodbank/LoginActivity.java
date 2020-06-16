package com.example.bloodbank;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.bloodbank.MainActivity.mAuth;


public class LoginActivity extends AppCompatActivity {
EditText email,pass;


ProgressDialog progressDialog;
    TextView feedback;
Button signup,login;
FirebaseUser user;
    private boolean doubleBackToExitPressedOnce =false;

    //Intent i=getIntent();
//    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);

        login=findViewById(R.id.login);


        feedback=findViewById(R.id.feedback);
        signup=findViewById(R.id.signup);


        login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 try {
                     progressDialog = new ProgressDialog(LoginActivity.this);
                     progressDialog.setTitle("Logging in...");
                     progressDialog.show();
                     mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                             .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful()) {



                                                         progressDialog.dismiss();
                                                         startActivity(new Intent(LoginActivity.this, HomePageActivity.class));





                                     } else {
                                         progressDialog.dismiss();
                                         AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(LoginActivity.this));

                                         builder.setMessage("Wrong email or password")
                                                 .setTitle("please try again");

                                         AlertDialog dialog = builder.create();
                                         dialog.show();

                                     }

                                 }
                             });

                 } catch (Exception e) {
                     progressDialog.dismiss();
                     DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             switch (which) {
                                 case DialogInterface.BUTTON_POSITIVE:
                                     //Yes button clicked
                                     email.setText("");
                                     pass.setText("");

                                     break;


                             }
                         }

                     };

                     AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                     builder.setMessage("Something went wrong please try again").setPositiveButton("OK", dialogClickListener)
                             .show();
                 }
             }

      });

    }



    @Override
    protected void onResume() {
        super.onResume();
        email.setText("");
        pass.setText("");
    }
    @Override

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();

            finish();

        }
        if(!this.doubleBackToExitPressedOnce)
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        this.doubleBackToExitPressedOnce = true;


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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