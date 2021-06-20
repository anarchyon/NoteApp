package learn.geekbrains.noteapp;

import android.view.View;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private final AppCompatTextView header;
    private final AppCompatTextView date;
    private final AppCompatImageView isImportant;
    private Note note;
    private sendIdLongClickedItem sendIdLongClickedItem;

    public NoteViewHolder(@NonNull View itemView,
                          NoteAdapter.OnItemClickListener clickListener,
                          Fragment fragment) {
        super(itemView);

        header = itemView.findViewById(R.id.item_note_header);
        date = itemView.findViewById(R.id.item_note_date);
        isImportant = itemView.findViewById(R.id.item_note_is_important);

        itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onItemClick(note);
            }
        });

        if (fragment != null) {
            itemView.setOnLongClickListener(view -> {
                sendIdLongClickedItem.sendId(getLayoutPosition());
                return false;
            });
            fragment.registerForContextMenu(itemView);
        }
    }

    public void bind(Note note) {
        this.note = note;
        header.setText(note.getName());
        date.setText(note.getCreationDate());
        if (note.getIsImportant() != 0) {
            isImportant.setImageResource(R.drawable.ic_btn_toggle_on_24);
        }
    }

    interface sendIdLongClickedItem {
        void sendId(int position);
    }

    public void setSendIdLongClickedItem(NoteViewHolder.sendIdLongClickedItem sendIdLongClickedItem) {
        this.sendIdLongClickedItem = sendIdLongClickedItem;
    }
}
