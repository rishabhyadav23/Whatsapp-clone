package com.example.android.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.whatsapp.Models.User;
import com.example.android.whatsapp.databinding.ActivitySignInBinding;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("We're login your Account");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("328848232978-vb2ktmk5eq9e3iva9clsapuadb2ogco3.apps.googleusercontent.com").requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.emailSignin.getText().toString(),binding.passSignin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignIn.this,HomeActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.goForSignupSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        binding.btnGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressDialog.show();
                signIn();
            }
        });

        if(auth.getCurrentUser()!=null){
            Intent intent = new Intent(SignIn.this,HomeActivity.class);
            startActivity(intent);
        }
    }
    int RC_SIGN_IN = 65;
    private  void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG","firebase"+account.getId());
                firebaseAuthwithGoogle(account.getIdToken());
            }catch (ApiException e){
                Log.w("TAG","Google sign in failed",e);
            }
        }
    }

    private void firebaseAuthwithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //progressDialog.dismiss();//box close
                if(task.isSuccessful()){
                    Log.d("TAG", "signInWithCredential:success");
                    FirebaseUser user = auth.getCurrentUser();

                    User user1 = new User();
                    user1.setUserId(user.getUid());
                    user1.setUserName(user.getDisplayName());
                    user1.setProfilePic(user.getPhotoUrl().toString());
                    firebaseDatabase.getReference().child("Users").child(user.getUid()).setValue(user1);

                    Intent intent = new Intent(SignIn.this,HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(SignIn.this, "Sign in with Google", Toast.LENGTH_SHORT).show();
                }else {
                    Log.w("TAG","signInWithCredential:failure",task.getException());
                    Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar.make(binding.getRoot(),"Authentication Failed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //925632218808902
        // finish signin activity


    }
}