package project.paveltoy.noteapp.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import project.paveltoy.noteapp.Data.Note;
import project.paveltoy.noteapp.Data.NoteRepository;
import project.paveltoy.noteapp.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> implements Filterable {
    private NoteRepository data;
    private Note note;
    private OnItemClickListener onItemClickListener;
    private final Fragment fragment;
    private int itemPosition;

    public NoteAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setData(NoteRepository data) {
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

    @Override
    public Filter getFilter() {
        return null;
    }

    interface OnItemClickListener {
        void onItemClick(Note note);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
//        if (position < data.size()) {
               holder.bind(data.getNote(position));
               holder.setSendIdLongClickedItem(this::setItemPosition);
//        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public int getItemPosition() {
        return itemPosition;
    }
}
