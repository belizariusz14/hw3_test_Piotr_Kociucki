package com.example.hw3_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firebase.firestore.core.Query;

import java.util.HashMap;
import java.util.Map;

import static com.example.hw3_test.BookListContent.Books;

//import com.example.hw3_test.tasks.TaskListContent;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity  implements BookFragment.OnListFragmentInteractionListener, DeleteDialog.OnDeleteDialogInteractionListener,
        EditDialog.OnEditDialogInteractionListener

{
    private static final String TAG = "MainActivity";
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
private CollectionReference mQuery= mFirestore.collection("Books");
private DocumentReference noteRef = mFirestore.document("Books/My book");
private String key_title = "Title";
private String key_name = "Name";
private String key_surname = "Surname";
private String key_publisher= "Publisher";
private String key_isbn = "ISBN";
private TextView textViewData;
private ListenerRegistration noteListener;
//fragment_book potrzebuje id content
int selectedDeleteItem = -1;
    public static  String BookExtra = "BookExtra";


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textViewData.findViewById(R.id.content);
        Button fab = findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFirebaseData();
    }

    private void loadFirebaseData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Books.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> book = new HashMap<>();
                                book = document.getData();

                                Object oId = book.get("id");
                                Object oName = book.get("Name");
                                Object oSurname = book.get("Surname");
                                Object oTitle = book.get("Title");
                                Object oPublisher = book.get("Publisher");
                                Object oISBN = book.get("ISBN");

                                int id = Integer.parseInt(oId.toString());
                                String name = oName.toString();
                                String surname = oSurname.toString();
                                String title = oTitle.toString();
                                String publisher = oPublisher.toString();
                                String isbn = oISBN.toString();

                                BookListContent.Book bookFromFireBase = new BookListContent.Book(id, name, surname, title, publisher, isbn);

                                BookListContent.addItem(bookFromFireBase);
                                BookListContent.lastID = id+1;
                            }
                            ((BookFragment) getSupportFragmentManager().findFragmentById(R.id.BookFragment)).notifyDataCharge();
                        }
                    }
                });
    }

    public void startBookInfoActivity(BookListContent.Book book){
        Intent intent = new Intent(this, BookInfoActivity.class);
        intent.putExtra(BookExtra, book);
        startActivity(intent);
    }

    public void startEditBookActivity(BookListContent.Book book){
        Intent intent = new Intent(this, EditBookActivity.class);
        intent.putExtra(BookExtra, book);
        startActivity(intent);
    }

    @Override
    public void onListFragmentClickInteraction(BookListContent.Book book, View view) { //zmiana elementu listy
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            displayBookInFragment(book);
        }else{
            startBookInfoActivity(book);
        }
    }

    @Override
    public void onListFragmentLongClickInteraction(BookListContent.Book book) {
        EditDialog.newInstance(book).show(getSupportFragmentManager(), getString(R.string.editdialog));
    }

    @Override
    public void onListFragmentTrashClickInteraction(int position) {
        selectedDeleteItem  = position;
        DeleteDialog.newInstance().show(getSupportFragmentManager(), getString(R.string.delete_dialog_tag));
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (selectedDeleteItem >= 0 && selectedDeleteItem < Books.size()) {
            BookListContent.deleteItem(selectedDeleteItem);
            ((BookFragment) getSupportFragmentManager().findFragmentById(R.id.BookFragment)).notifyDataCharge();

            BookInfoFragment bookInfoFragment = ( (BookInfoFragment) getSupportFragmentManager().findFragmentById(R.id.BookInfoFragment));
            if(bookInfoFragment != null){
                bookInfoFragment.displayEmptyBook();
            }
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onEditDialogPositiveClick(DialogFragment dialog, BookListContent.Book book) {
        startEditBookActivity(book);
    }

    @Override
    public void onEditDialogNegativeClick(DialogFragment dialog) {

    }

    private void displayBookInFragment(BookListContent.Book book) {
        BookInfoFragment bookInfoFragment = ((BookInfoFragment) getSupportFragmentManager().findFragmentById(R.id.BookInfoFragment));
        if(bookInfoFragment != null){
            bookInfoFragment.displayBook(book);
        }
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        mQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    return;
                }
                String data ="";
                for(QueryDocumentSnapshot documentSnapshot : value)
                {
                    BookListContent.Book book = documentSnapshot.toObject(BookListContent.Book.class);
                    book.setid(documentSnapshot.getId());

                    String documentId = book.getid();
                    String title = book.getTitle();
                    String name = book.getName();
                    String surname = book.getSurname();
                    String publisher = book.getPublisher();
                    String ISBN = book.getISBN();

                    data +="ID: "+documentId+ "Title: "+title+"\n"+"Author: "+ name+" "+surname+"\n"+"Publisher: "+publisher+"\n"+"ISBN: "+ISBN +"\n\n";
                    textViewData.setText("Title: "+title+"\n"+"Author: "+ name+" "+surname+"\n"+"Publisher: "+publisher+"\n"+"ISBN: "+ISBN);
                    //mQuery.document(documentId);
                }
            }
        });

        /*noteListener=noteRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(error!=null) {
                    Toast.makeText(MainActivity.this, "error while loading", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error.toString());
                    return;
                }
                if(documentSnapshot.exists()){
                    BookListContent.Book book = documentSnapshot.toObject(BookListContent.Book.class);

                    String title = book.getTitle();
                    String name = book.getName();
                    String surname = book.getSurname();
                    String publisher = book.getPublisher();
                    String ISBN = book.getISBN();

                    textViewData.setText("Title: "+title+"\n"+"Author: "+ name+" "+surname+"\n"+"Publisher: "+Publisher+"\n"+"ISBN: "+ISBN);
                } else {
                    textViewData.setText("");
                }
            }
        });*/
   /* }

    @Override
    protected void onStop() {
        super.onStop();
        noteListener.remove();
    }

    private void onAddItemsClicked() {
        // Get a reference to the restaurants collection
        CollectionReference books = mFirestore.collection("Books");

       /* for (int i = 0; i < 10; i++) {
            // Get a random Restaurant POJO
            BookListContent.Book book = BookUtil.getRandom(this);

            // Add a new document to the restaurants collection
            books.add(book);
        }*/
   /* }
    public static final String bookExtra = "bookExtra";
    private int currentItemPosition = -1;
    private EditText bookIdEditTxt= findViewById(R.id.Id);
    private EditText bookTitleEditTxt= findViewById(R.id.Title);
    private EditText bookNameEditTxt= findViewById(R.id.Name);
    private EditText bookSurnameEditTxt= findViewById(R.id.Surname);
    private EditText bookPublisherEditTxt= findViewById(R.id.Publisher);
    private EditText bookISBNEditTxt= findViewById(R.id.ISBN);
    @SuppressLint("ResourceType")
    public void addClick(View view){

        //Spinner drawableSpinner = findViewById(R.id.drawableSpinner);
        String bookid = bookIdEditTxt.getText().toString();
        String bookTitle = bookTitleEditTxt.getText().toString();
        String bookName = bookNameEditTxt.getText().toString();
        String bookSurname = bookSurnameEditTxt.getText().toString();
        String bookPublisher = bookPublisherEditTxt.getText().toString();
        String bookISBN = bookISBNEditTxt.getText().toString();

        BookListContent.Book book = new BookListContent.Book(bookid, bookName, bookSurname, bookTitle, bookPublisher, bookISBN);

        mQuery.add(book);
        /*noteRef.set(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });*/

        /*if(bookTitle.isEmpty() && taskDescription.isEmpty()){
            TaskListContent.addItem(new TaskListContent.Task("Task" + TaskListContent.ITEMS.size() + 1,
                    getString(R.string.default_title),
                    getString(R.string.default_description),
                    selectedImage));
        }else{
            if(taskTitle.isEmpty())
                taskTitle=getString(R.string.default_title);
            if(taskDescription.isEmpty())
                taskDescription= getString(R.string.default_description);

            TaskListContent.addItem(new TaskListContent.Task("Task" + TaskListContent.ITEMS.size() + 1,
                    taskTitle,
                    taskDescription,
                    selectedImage));
        }*/

       /* bookTitleEditTxt.setText("");
        bookNameEditTxt.setText("");
        bookSurnameEditTxt.setText("");
        bookPublisherEditTxt.setText("");
        bookISBNEditTxt.setText("");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);*/
   /* }



    private void startSecondActivity(BookListContent.Book book, int position) {
        Intent intent = new Intent(this,BookInfoActivity.class);
        intent.putExtra(bookExtra,book);
        startActivity(intent);
    }



    /*@Override
    public void onFragmentClickAdderInteraction(int position)
    {
        displayAddInContact();

    }*/








   /* private void startSecondActivity(BookListContent.Book book, int position) {
        Intent intent = new Intent(this,BookInfoActivity.class);
        intent.putExtra(bookExtra,book);
        startActivity(intent);
    }*/

   /* private void displayTaskInFragment(BookListContent.Book book){
        BookInfoFragment bookInfoFragment = ((BookInfoFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment));
        if(bookInfoFragment != null)    {
            bookInfoFragment.displayBook(book);
        }
    }

    public void displayAddInContact()
    {

    }

    private void showDeleteDialog(){
        DeleteDialog.newInstance().show(getSupportFragmentManager(),getString(R.string.delete_dialog_tag));
    }

    public void loadBooks(View view) {
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    BookListContent.Book book = documentSnapshot.toObject(BookListContent.Book.class);
                    book.setid(documentSnapshot.getId());

                    String documentId = book.getid();
                    String title = book.getTitle();
                    String name = book.getName();
                    String surname = book.getSurname();
                    String publisher = book.getPublisher();
                    String ISBN = book.getISBN();

                    data += "ID: "+documentId+"Title: "+title+"\n"+"Author: "+ name+" "+surname+"\n"+"Publisher: "+publisher+"\n"+"ISBN: "+ISBN +"\n\n";
                }
                textViewData.setText(data);
            }
        });
        /*noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    BookListContent.Book book = documentSnapshot.toObject(BookListContent.Book.class);

                    String title = book.getTitle();
                    String name = book.getName();
                    String surname = book.getSurname();
                    String publisher = book.getPublisher();
                    String ISBN = book.getISBN();
                    //Map<String, Object> note = documentSnapshot.getData();

                    textViewData.setText("Title: "+title+"\n"+"Author: "+ name+" "+surname+"\n"+"Publisher: "+Publisher+"\n"+"ISBN: "+ISBN);
                }else {
                    Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });*/
   /* }

    public void update(View view) {
        String bookTitle = bookTitleEditTxt.getText().toString();
        String bookName = bookNameEditTxt.getText().toString();
        String bookSurname = bookSurnameEditTxt.getText().toString();
        String bookPublisher = bookPublisherEditTxt.getText().toString();
        String bookISBN = bookISBNEditTxt.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(key_title,bookTitle);//bez id
        note.put(key_name,bookName);
        note.put(key_surname,bookSurname);
        note.put(key_publisher,bookPublisher);
        note.put(key_isbn,bookISBN);
        //noteRef.set(note, SetOptions.merge());
        noteRef.update(key_title,bookTitle);//bez id
        noteRef.update(key_name,bookName);
        noteRef.update(key_surname,bookSurname);
        noteRef.update(key_publisher,bookPublisher);
        noteRef.update(key_isbn,bookISBN);
    }

    public void delete(View view) {
        noteRef.delete();
    }





    /*  @Override
      public void onDialogPositiveClick(DialogFragment dialog) {

          if(currentItemPosition != -1 && currentItemPosition < TaskListContent.ITEMS.size()){
              TaskListContent.removeItem(currentItemPosition);
              ((TaskFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment)).notifyDataCharge();

          }
      }

      @Override
      public void onClick(View v) {
          showDeleteDialog();
      }*/
    //@Override
    //public void onDialogNegativeClick(DialogFragment dialog) {
        /*View v= findViewById(R.id.addButton);
        if(v != null){
            Snackbar.make(v,getString(R.string.delete_cancel_msg),Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry_msg), new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            showDeleteDialog();
                        }
                    }).show();
        }*/
   // }
}
