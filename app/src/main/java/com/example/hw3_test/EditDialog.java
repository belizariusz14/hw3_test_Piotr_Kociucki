package com.example.hw3_test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditDialog extends DialogFragment {

    private String name;
    private String surname;
    private String publisher;
    private BookListContent.Book mBook;

    public EditDialog(BookListContent.Book book) {
        this.publisher = book.Publisher;
        this.mBook = book;
    }

    static EditDialog newInstance(BookListContent.Book book){
        return new EditDialog(book);
    }

    public interface OnEditDialogInteractionListener {
        void onEditDialogPositiveClick(DialogFragment dialog, BookListContent.Book book);
        void onEditDialogNegativeClick(DialogFragment dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Edit "+ publisher +" publisher?");

        builder.setPositiveButton(getString(R.string.edit_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OnEditDialogInteractionListener mListener =  (OnEditDialogInteractionListener) getActivity();
                mListener.onEditDialogPositiveClick(EditDialog.this, mBook);
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OnEditDialogInteractionListener mListener =  (OnEditDialogInteractionListener) getActivity();
                mListener.onEditDialogNegativeClick(EditDialog.this);
            }
        });
        return builder.create();
    }
}