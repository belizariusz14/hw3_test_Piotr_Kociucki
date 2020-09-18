package com.example.hw3_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookEditFragment extends Fragment {
    private int id;
    private  String Name;
    private  String Surname;
    private  String Title;
    private  String Publisher;
    private  String ISBN;

    private EditText idEditTxt;
    private EditText nameEditTxt;
    private EditText surnameEditTxt;
    private EditText titleEditTxt;
    private EditText publisherEditTxt;
    private EditText isbnEditTxt;
    private Button saveChanges;

    public BookEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_edit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();

        //idEditTxt = activity.findViewById(R.id.Id);
        nameEditTxt = activity.findViewById(R.id.Name);
        surnameEditTxt = activity.findViewById(R.id.Surname);
        titleEditTxt = activity.findViewById(R.id.Title);
        publisherEditTxt = activity.findViewById(R.id.Publisher);
        isbnEditTxt = activity.findViewById(R.id.ISBN);

        saveChanges = activity.findViewById(R.id.saveChangesButton);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        Intent intent = getActivity().getIntent();
        if(intent != null){
            BookListContent.Book receivedBook= intent.getParcelableExtra(MainActivity.BookExtra);
            if(receivedBook != null) {
                displaybook(receivedBook);
            }
        }

    }

    public void saveChanges(){
        //id = idEditTxt.getText().toString();
        Title = titleEditTxt.getText().toString();
        Name = nameEditTxt.getText().toString();
        Surname = surnameEditTxt.getText().toString();
        Publisher = publisherEditTxt.getText().toString();
        ISBN = isbnEditTxt.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Books").document(Integer.toString(id));

        docRef.update("Name", Name,
                "Surname", Surname,
                "Title", Title,
                "Publisher", Publisher,
                "ISBN", ISBN,
                "id", id
                );

        getActivity().finish();
    }

    public void displaybook(BookListContent.Book book){

        id = book.id;
        Name = book.Name;
        Surname = book.Surname;
        Title = book.Title;
        Publisher = book.Publisher;
        ISBN = book.ISBN;

        idEditTxt.setText(id);
        titleEditTxt.setText(Title);
        nameEditTxt.setText(Name);
        surnameEditTxt.setText(Surname);
        publisherEditTxt.setText(Publisher);
        isbnEditTxt.setText(ISBN);
    }
}
