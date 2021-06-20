package learn.geekbrains.noteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private List<Note> data = new ArrayList<>();
    private Note note;
    private OnItemClickListener onItemClickListener;
    private final Fragment fragment;

    public NoteAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setData(List<Note> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardViewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(cardViewItem, onItemClickListener, fragment);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(Note note);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (position < data.size()) {
               holder.bind(data.get(position));
               holder.setSendIdLongClickedItem(this::setNote);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setNote(int index) {
        this.note = data.get(index);
    }

    public Note getNote() {
        return note;
    }
}
