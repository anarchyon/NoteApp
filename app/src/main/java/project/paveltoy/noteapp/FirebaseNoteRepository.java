package project.paveltoy.noteapp;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseNoteRepository implements NoteRepository {
    private final FirebaseFirestore db;
    private CollectionReference collectionReference;
    private List<Note> notes;

    public FirebaseNoteRepository() {
        db = FirebaseFirestore.getInstance();
        notes = new ArrayList<>();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);
    }

    @Override
    public NoteRepository init(NoteSourceCallback noteSourceCallback) {
        FirebaseAccountOpenData accountOpenData = FirebaseAccountOpenData.getInstance();
        collectionReference = db.collection(accountOpenData.getEmail());
        collectionReference
                .orderBy(NoteMapping.Fields.CREATION_DATE, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        loadNotes(task.getResult());
                        noteSourceCallback.initialized(this);
                    }
                })
                .addOnFailureListener(e -> {

                });
        collectionReference.addSnapshotListener((value, error) -> {
            if (value != null) {
                loadNotes(value);
            }
        });
        return this;
    }

    private void loadNotes(QuerySnapshot documentSnapshots) {
        notes = new ArrayList<>();
        for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
            Map<String, Object> document = documentSnapshot.getData();
            String id = documentSnapshot.getId();
            Note note = NoteMapping.parseFirestoreDocumentToNote(id, document);
            notes.add(note);
        }
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

    @Override
    public void insertNote(int position, Note note) {
        collectionReference.add(NoteMapping.exportNoteToFirestoreDocument(note));
        notes.set(position, note);
    }
}
