package com.example.hw3_test;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw3_test.BookListContent;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookInfoFragment extends Fragment {

    public BookInfoFragment() {
        // Required empty public constructor
    }

    public void displayBook(BookListContent.Book book) {
        FragmentActivity activity = getActivity();

        TextView bookInfoTitle = activity.findViewById(R.id.Title);
        TextView bookInfoAuthorName = activity.findViewById(R.id.Name);
        TextView bookInfoAuthorSurname = activity.findViewById(R.id.Surname);
        TextView bookInfoPublisher = activity.findViewById(R.id.Publisher);
        //TextView bookInfoid = activity.findViewById(R.id.Id);
        TextView bookInfoISBN = activity.findViewById(R.id.ISBN);

        String tit = book.Title;
        String pub = book.Publisher;
        String sur = book.Surname;
        String nam = book.Name;
        //String id = book.id;
        String isb = book.ISBN;


        bookInfoTitle.setText("Title: "+tit);
        bookInfoPublisher.setText("Publisher: "+pub);
        bookInfoAuthorSurname.setText("Author's surname: "+sur);
        bookInfoAuthorName.setText("Author's name: "+nam);
        bookInfoISBN.setText("ISBN: "+isb);
        //bookInfoid.setText("id: "+id);
    }

    public void displayEmptyBook() {
        FragmentActivity activity = getActivity();

        TextView bookInfoTitle = activity.findViewById(R.id.Title);
        TextView bookInfoAuthorName = activity.findViewById(R.id.Name);
        TextView bookInfoAuthorSurname = activity.findViewById(R.id.Surname);
        TextView bookInfoPublisher = activity.findViewById(R.id.Publisher);
        //TextView bookInfoid = activity.findViewById(R.id.Id);
        TextView bookInfoisbn = activity.findViewById(R.id.ISBN);

        bookInfoTitle.setText("");
        bookInfoPublisher.setText("P");
        bookInfoAuthorSurname.setText("");
        bookInfoAuthorName.setText("");
        //bookInfoid.setText("");
        bookInfoisbn.setText("");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)  {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if(intent != null) {
            BookListContent.Book receivedBook = intent.getParcelableExtra(MainActivity.BookExtra);
            if(receivedBook != null)    {
                displayBook(receivedBook);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_info, container, false);
    }

}


