package learn.geekbrains.noteapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomNavigationDrawer extends BottomSheetDialogFragment {
    public static final String TAG = "bottom_navigation_drawer";

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
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_menu_home) {
                getNavController().showFragment(R.id.nav_menu_home);
            } else if (itemId == R.id.nav_menu_important) {
                getNavController().showFragment(R.id.nav_menu_important);
            } else if (itemId == R.id.nav_menu_settings) {
                getNavController().showFragment(R.id.nav_menu_settings);
            } else if (itemId == R.id.nav_menu_about) {
                getNavController().showFragment(R.id.nav_menu_about);
            }
            this.dismiss();
            return true;
        });
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
