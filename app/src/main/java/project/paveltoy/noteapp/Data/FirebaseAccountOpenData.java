package project.paveltoy.noteapp.Data;

import android.net.Uri;

public class FirebaseAccountOpenData {
    private static FirebaseAccountOpenData instance;

    private String displayName;
    private String email;
    private Uri imageUri;
    private boolean isFirstRequest = true;

    private FirebaseAccountOpenData(){}

    public static FirebaseAccountOpenData getInstance() {
        if (instance == null) {
            instance = new FirebaseAccountOpenData();
        } else instance.isFirstRequest = false;
        return instance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void clearData() {
        setDisplayName(null);
        setEmail(null);
        setImageUri(null);
    }

    public boolean isFirstRequest() {
        return isFirstRequest;
    }
}
