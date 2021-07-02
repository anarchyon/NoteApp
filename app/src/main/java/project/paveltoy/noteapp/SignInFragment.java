package project.paveltoy.noteapp;

import android.app.Activity;
import android.content.Intent;
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
    public static final String TAG = "sign_in_fragment";
    private TextView textViewAccountEmail, textViewAccountName;
    private ImageView imageViewAccountImage;
    private MaterialButton buttonContinue, signInButton, signOutButton;
    private FirebaseAccountOpenData firebaseAccountOpenData;
    private boolean isUserLoggedIn;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            isUserLoggedIn = true;
            setFirebaseAccountOpenData(user);
            if (firebaseAccountOpenData.isFirstRequest()) getSignInController().signedIn();
        } else {
            isUserLoggedIn = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        setFirebaseAccountOpenData(firebaseUser);
//        checkSignDataAndStartProgram(firebaseUser);
        initView(view);
        updateUI();
//        setButtonState(firebaseUser != null);
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
            setFirebaseAccountOpenData(firebaseUser);
            getSignInController().signedIn();
//            updateUI();
//            setButtonState(true);
//            checkSignDataAndStartProgram(firebaseUser);
        } else {
            if (response != null) {
                response.getError().getMessage();
            }
        }
    }

    private void handleSignInResult(FirebaseUser firebaseUser) {
//        try {
//            GoogleSignInAccount account = firebaseUser.getResult(ApiException.class);
//        disableSign();
//        accountName = firebaseUser.getDisplayName();
//        accountEmail = firebaseUser.getEmail();
//        accountImage = firebaseUser.getPhotoUrl();
//            updateUI(accountName, accountEmail, accountImage);
//        } catch (ApiException e) {
//
//        }
    }

//    private void checkSignDataAndStartProgram(FirebaseUser firebaseUser) {
//        if (firebaseUser != null) {
//            if (firebaseAccountOpenData.getRequestCounter() <= 1)
//                getSignInController().signedIn();
//        }
////        Intent signInIntent = googleSignInClient.getSignInIntent();
////        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

    private void setFirebaseAccountOpenData(@Nullable FirebaseUser firebaseUser) {
        firebaseAccountOpenData = FirebaseAccountOpenData.getInstance();
        if (firebaseUser == null) {
            firebaseAccountOpenData.clearData();
        } else {
            firebaseAccountOpenData.setDisplayName(firebaseUser.getDisplayName());
            firebaseAccountOpenData.setEmail(firebaseUser.getEmail());
            firebaseAccountOpenData.setImageUri(firebaseUser.getPhotoUrl());
        }
    }

    private void updateUI() {
        if (isUserLoggedIn) {
            textViewAccountName.setText(firebaseAccountOpenData.getDisplayName());
            textViewAccountEmail.setText(firebaseAccountOpenData.getEmail());
            imageViewAccountImage.setImageURI(firebaseAccountOpenData.getImageUri());
        }
        signInButton.setEnabled(!isUserLoggedIn);
        signOutButton.setEnabled(isUserLoggedIn);
        buttonContinue.setEnabled(isUserLoggedIn);
    }

    private void setButtonState(boolean isUserSignedIn) {
        signInButton.setEnabled(!isUserSignedIn);
        buttonContinue.setVisibility(isUserSignedIn ? View.VISIBLE : View.INVISIBLE);
        signOutButton.setEnabled(isUserSignedIn);
    }

//    private void enableSign() {
//        signInButton.setEnabled(true);
//        buttonContinue.setEnabled(false);
//        signOutButton.setEnabled(false);
//    }
//    private void disableSign() {
//        signInButton.setEnabled(false);
//        buttonContinue.setEnabled(true);
//        signOutButton.setEnabled(true);
//    }

    interface SignInController {
        void signedIn();

        void signedOut();
    }

    private SignInController getSignInController() {
        return (SignInController) getActivity();
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(requireActivity())
                .addOnSuccessListener(unused -> {
                    isUserLoggedIn = false;
                    setFirebaseAccountOpenData(null);
                    updateUI();
                    getSignInController().signedOut();
                });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
//        if (account != null) {
//            disableSign();
//            accountName = account.getDisplayName();
//            accountEmail = account.getEmail();
//            accountImage = account.getPhotoUrl();
//            updateUI(accountName, accountEmail, accountImage);
//        }
//    }
}
