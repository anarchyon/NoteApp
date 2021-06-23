package project.paveltoy.noteapp;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class TextFilter extends Filter {
    private final NoteAdapter adapter;
    private final List<Note> originalList;
    private final List<Note> filteredList;

    public TextFilter (NoteAdapter adapter, List<Note> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = new ArrayList<>(originalList);
        filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        return null;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

    }
}
