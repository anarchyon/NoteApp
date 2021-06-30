package project.paveltoy.noteapp;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseNoteService implements NoteService {
    public static final String NOTES_COLLECTION = "notes";

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = firestore.collection(NOTES_COLLECTION);
    private List<Note> notes = new ArrayList<>();

    @Override
    public NoteService init(NoteSourceCallback noteSourceCallback) {
        collectionReference
                .orderBy(NoteMapping.Fields.CREATION_DATE, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notes = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Map<String, Object> document = documentSnapshot.getData();
                            String id = documentSnapshot.getId();
                            Note note = NoteMapping.parseFirestoreDocumentToNote(id, document);
                            notes.add(note);
                        }
                        noteSourceCallback.initialized(this);
                    } else {

                    }
                })
                .addOnFailureListener(e -> {

                });
        return this;
    }

    @Override
    public void deleteNote(int position) {
        collectionReference.document(notes.get(position).getId()).delete();
        notes.remove(position);
    }

    @Override
    public void deleteNote(Note note) {
        int position = notes.indexOf(note);
        deleteNote(position);
    }

    @Override
    public void editNote(int position, Note note) {
        String id = note.getId();
        collectionReference.document(id).set(NoteMapping.exportNoteToFirestoreDocument(note));
        notes.set(position, note);
    }

    @Override
    public void editNote(Note note) {
        int position = notes.indexOf(note);
        editNote(position, note);
    }

    @Override
    public void addNote(Note newNote) {
        collectionReference.add(NoteMapping.exportNoteToFirestoreDocument(newNote))
                .addOnSuccessListener(doc -> newNote.setId(doc.getId()));
        notes.add(newNote);
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        if (notes == null) return 0;
        return notes.size();
    }

    @Override
    public int getPosition(Note note) {
        return notes.indexOf(note);
    }
}
