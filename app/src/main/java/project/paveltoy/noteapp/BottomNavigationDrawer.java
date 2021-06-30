package project.paveltoy.noteapp;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

public class BottomNavigationDrawer extends BottomSheetDialogFragment {
    public static final String TAG = "bottom_navigation_drawer";
    private FirebaseAccountOpenData accountOpenData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_navigation, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        assert dialog != null;
        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        NavigationView navigationView = view.findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(view1 -> {
            getNavController().showFragment(R.id.header_view);
            this.dismiss();
        });
        initUserAccountData(headerView);
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            getNavController().showFragment(itemId);
            this.dismiss();
            return true;
        });
    }

    private void initUserAccountData(View view) {
        ImageView userImage = view.findViewById(R.id.account_image);
        TextView userName = view.findViewById(R.id.account_name);
        TextView userEmail = view.findViewById(R.id.account_email);

        accountOpenData = FirebaseAccountOpenData.getInstance();
        Uri imageUri = accountOpenData.getImageUri();
        if (imageUri != null) {
            userImage.setImageURI(imageUri);
        }
        userName.setText(accountOpenData.getDisplayName());
        userEmail.setText(accountOpenData.getEmail());
    }

    interface NavController {
        void showFragment(int idFragment);
    }

    private NavController getNavController() {
        return (NavController) getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof NavController)) {
            throw new IllegalStateException(
                    "Activity must implements BottomNavigationDrawer.NavController"
            );
        }
    }
}
