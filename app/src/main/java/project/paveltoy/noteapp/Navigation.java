package project.paveltoy.noteapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class Navigation {
    private static final String BACK_STACK_NAME = "back_stack";
    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(int idContainer, Fragment fragment, String tag, boolean useBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(idContainer, fragment, tag);
        if (useBackStack) {
            fragmentTransaction.addToBackStack(BACK_STACK_NAME);
        }
        fragmentTransaction.commit();
    }

    public void moveFragmentToAnotherContainer(int idContainer, Fragment fragment, String tag, boolean useBackStack) {
        fragmentManager.beginTransaction()
                .remove(fragment)
                .runOnCommit(() -> addFragment(idContainer, fragment, tag, useBackStack))
                .commit();
    }

    public void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() >= 0) {
            fragmentManager.popBackStack(BACK_STACK_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void setFragmentPrimary(Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .setPrimaryNavigationFragment(fragment)
                .commit();
    }

    public List<Fragment> getFragments() {
        return fragmentManager.getFragments();
    }

    public Fragment getFragmentByContainerId(int fragmentContainerId) {
        return fragmentManager.findFragmentById(fragmentContainerId);
    }
}
