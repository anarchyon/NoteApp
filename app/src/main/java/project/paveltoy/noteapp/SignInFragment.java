package project.paveltoy.noteapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignInFragment extends Fragment {
    private static final String TAG = "GoogleAuth";
    private static final int RC_SIGN_IN = 40404;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;

    private GoogleSignInClient googleSignInClient;
    private TextView textViewAccountEmail, textViewAccountName;
    private ImageView imageViewAccountImage;
    private MaterialButton buttonContinue, signInButton, signOutButton;
    private String accountName, accountEmail;
    private Uri accountImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            disableSign();
            accountName = account.getDisplayName();
            accountEmail = account.getEmail();
            accountImage = account.getPhotoUrl();
            updateUI(accountName, accountEmail, accountImage);
        } catch (ApiException e) {

        }
    }

    private void updateUI(String accountName, String accountEmail, Uri accountImage) {
        textViewAccountName.setText(accountName);
        textViewAccountEmail.setText(accountEmail);
        imageViewAccountImage.setImageURI(accountImage);
    }

    private void enableSign() {
        signInButton.setEnabled(true);
        buttonContinue.setEnabled(false);
        signOutButton.setEnabled(false);
    }

    private void disableSign() {
        signInButton.setEnabled(false);
        buttonContinue.setEnabled(true);
        signOutButton.setEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initGoogleSign();
        initView(view);
        enableSign();
        return view;
    }

    interface SignInController {
        void signedIn(String accountName, String accountEmail, Uri accountImage);
        void signedOut();
    }

    private SignInController getSignInController() {
        return (SignInController) getActivity();
    }

    private void initView(View view) {
        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view1 -> {
            signIn();
        });

        signOutButton = view.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(view1 -> {
            signOut();
            getSignInController().signedOut();
        });

        imageViewAccountImage = view.findViewById(R.id.account_image);
        textViewAccountName = view.findViewById(R.id.account_name);
        textViewAccountEmail = view.findViewById(R.id.account_email);
        
        buttonContinue = view.findViewById(R.id.continue_button);
        buttonContinue.setOnClickListener(view1 -> {
            getSignInController().signedIn(accountName, accountEmail, accountImage);
        });
    }

    private void signOut() {
        googleSignInClient
                .signOut()
                .addOnCompleteListener(task -> {
                    enableSign();
                    updateUI("", "", null);
                });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initGoogleSign() {
        GoogleSignInOptions gso = 
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            disableSign();
            accountName = account.getDisplayName();
            accountEmail = account.getEmail();
            accountImage = account.getPhotoUrl();
            updateUI(accountName, accountEmail, accountImage);
        }
    }
}
