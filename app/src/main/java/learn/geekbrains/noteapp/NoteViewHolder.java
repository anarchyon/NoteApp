package learn.geekbrains.noteapp;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private final AppCompatTextView header;
    private final AppCompatTextView date;
    private Note note;

    public NoteViewHolder(@NonNull View itemView, NoteAdapter.OnItemClickListener clickListener) {
        super(itemView);

        header = itemView.findViewById(R.id.item_note_header);
        date = itemView.findViewById(R.id.item_note_date);
        itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onItemClick(note);
            }
        });

    }

    public void bind(Note note) {
        this.note = note;
        header.setText(note.getName());
        date.setText(note.getCreationDate());
    }
}
