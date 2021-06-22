package learn.geekbrains.noteapp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class FragmentObserver implements LifecycleObserver {
    ObserverContract observerContract;

    public FragmentObserver(ObserverContract observerContract) {
        this.observerContract = observerContract;
    }

    public void setObserverContract(ObserverContract observerContract) {
        this.observerContract = observerContract;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void showContextElement() {
        observerContract.switchState(true);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void hideContextElement() {
        observerContract.switchState(false);
    }

    interface ObserverContract {
        void switchState(boolean isActive);
    }


}
