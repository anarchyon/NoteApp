package project.paveltoy.noteapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class SignInFragment extends Fragment {
    private GoogleSignInClient googleSignInClient;
    private TextView textViewAccountEmail, textViewAccountName;
    private ImageView imageViewAccountImage;
    private MaterialButton buttonContinue, signInButton, signOutButton;
    private String accountName, accountEmail;
    private Uri accountImage;
    private FirebaseAccountOpenData firebaseAccountOpenData;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        signInOk(firebaseUser);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(view);
        initUI();
        return view;
    }

    private void initView(View view) {
        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view1 -> {
//            signIn();
            createSignInIntent();
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
            getSignInController().signedIn();
        });
    }

    private void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(intent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == Activity.RESULT_OK) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            signInOk(firebaseUser);
        }
    }

    private void handleSignInResult(FirebaseUser firebaseUser) {
//        try {
//            GoogleSignInAccount account = firebaseUser.getResult(ApiException.class);
            disableSign();
            accountName = firebaseUser.getDisplayName();
            accountEmail = firebaseUser.getEmail();
            accountImage = firebaseUser.getPhotoUrl();
            updateUI(accountName, accountEmail, accountImage);
//        } catch (ApiException e) {
//
//        }
    }

    private void signInOk(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            boolean isProgramJustLaunched = false;
            if (firebaseAccountOpenData == null) isProgramJustLaunched = true;
            setFirebaseAccountOpenData(firebaseUser);
            if (isProgramJustLaunched) getSignInController().signedIn();
        }
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void setFirebaseAccountOpenData(FirebaseUser firebaseUser) {
        firebaseAccountOpenData = FirebaseAccountOpenData.getInstance();
        firebaseAccountOpenData.setDisplayName(firebaseUser.getDisplayName());
        firebaseAccountOpenData.setEmail(firebaseUser.getEmail());
        firebaseAccountOpenData.setImageUri(firebaseUser.getPhotoUrl());
    }

    private void initUI() {
        textViewAccountName.setText(accountName);
        textViewAccountEmail.setText(accountEmail);
        imageViewAccountImage.setImageURI(accountImage);

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
    interface SignInController {


        void signedIn();

        void signedOut();

    }

    private SignInController getSignInController() {
        return (SignInController) getActivity();
    }

    private void signOut() {
        googleSignInClient
                .signOut()
                .addOnCompleteListener(task -> {
                    enableSign();
                    updateUI("", "", null);
                });
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
