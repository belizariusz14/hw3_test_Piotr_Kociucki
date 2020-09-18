package com.example.hw3_test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        addbook();
    }
    @SuppressLint("WrongViewCast")
    private void addbook() {
        Button addbutton;
        addbutton = findViewById(R.id.addButton);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText bookIdEditTxt = findViewById(R.id.Id);
                EditText bookTitleEditTxt = findViewById(R.id.Title);
                EditText bookNameEditTxt = findViewById(R.id.Name);
                EditText bookSurnameEditTxt = findViewById(R.id.Surname);
                EditText bookPublisherEditTxt = findViewById(R.id.Publisher);
                EditText bookISBNEditTxt = findViewById(R.id.ISBN);

                //bookIdEditTxt = findViewById(R.id.Id);
                bookTitleEditTxt = findViewById(R.id.Title);
                bookNameEditTxt = findViewById(R.id.Name);
                bookSurnameEditTxt = findViewById(R.id.Surname);
                bookPublisherEditTxt = findViewById(R.id.Publisher);
                bookISBNEditTxt = findViewById(R.id.ISBN);


                //String id;
                String title;
                String name;
                String surname;
                String publisher;
                String isbn;

                //id = bookIdEditTxt.getText().toString();
                title = bookTitleEditTxt.getText().toString();
                name = bookNameEditTxt.getText().toString();
                surname = bookSurnameEditTxt.getText().toString();
                publisher = bookPublisherEditTxt.getText().toString();
                isbn = bookISBNEditTxt.getText().toString();

                if(title.isEmpty()){
                    bookTitleEditTxt.setError(getString(R.string.thisFieldCannotBeEmpty));
                    return;
                }


                BookListContent.Book newBook = new BookListContent.Book(name, surname, title, publisher, isbn);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Books").document(Integer.toString(newBook.id)).set(newBook);


                //bookIdEditTxt.setText("");
                bookTitleEditTxt.setText("");
                bookNameEditTxt.setText("");
                bookSurnameEditTxt.setText("");
                bookPublisherEditTxt.setText("");
                bookISBNEditTxt.setText("");

                finish(); //close this activity and go bac to main activity
            }
        });
    }

    /*public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentClickAdderInteraction(int position);
    }*/
}
