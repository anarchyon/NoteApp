package learn.geekbrains.noteapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RootLandFragment extends Fragment {
    public static final String TAG = "root_land_fragment";
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.container_root, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bottomAppBar = requireActivity().findViewById(R.id.bottom_menu);
        fab = requireActivity().findViewById(R.id.fab);
    }

    @Override
    public void onStop() {
        if (bottomAppBar != null) {
            bottomAppBar.getMenu().findItem(R.id.menu_main_search_button).setVisible(false);
            bottomAppBar.getMenu().findItem(R.id.menu_bottom_delete_note).setVisible(false);
            bottomAppBar.getMenu().findItem(R.id.menu_bottom_save_note).setVisible(false);
            bottomAppBar.setNavigationIcon(null);
        }
        if (fab != null) {
            fab.setImageResource(R.drawable.ic_baseline_reply_24);
            fab.setOnClickListener(view -> requireActivity().onBackPressed());
        }
        super.onStop();
    }
}
