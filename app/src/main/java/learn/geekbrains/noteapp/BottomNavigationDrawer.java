package learn.geekbrains.noteapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_menu_home) {
                Toast.makeText(getContext(), getResources().getString(R.string.home), Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.nav_menu_important) {
                Toast.makeText(getContext(), getResources().getString(R.string.important), Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.nav_menu_settings) {
                Toast.makeText(getContext(), getResources().getString(R.string.settings), Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.nav_menu_about) {
                Toast.makeText(getContext(), getResources().getString(R.string.about), Toast.LENGTH_LONG).show();
            }
            return true;
        });
    }
}
