package com.example.hw3_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw3_test.BookFragment.OnListFragmentInteractionListener;
import com.example.hw3_test.BookListContent.Book;

import java.util.List;


public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private final List<Book> mValues;
    private final BookFragment.OnListFragmentInteractionListener mListener;


    public MyTaskRecyclerViewAdapter(List<Book> items, BookFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Book book = mValues.get(position);
        holder.mItem = book;
        holder.mTitleView.setText(book.Title);
        holder.mNameView.setText(book.Name);
        holder.mSurnameView.setText(book.Surname);
        holder.mPublisherView.setText(book.Publisher);
        holder.mISBNView.setText(book.ISBN);

       // Context context= holder.mView.getContext();



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentClickInteraction(holder.mItem,v);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentLongClickInteraction(holder.mItem);
                //mListener.onListFragmentClickInteraction(holder.mItem,position);
                return false;

            }

        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentTrashClickInteraction(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mNameView;
        public final TextView mSurnameView;
        public final TextView mPublisherView;
        public final TextView mISBNView;
        public Book mItem;
        public ImageButton deleteButton;




        public ViewHolder(View view) {
            super(view);
            mView = view;
            //view.setBackgroundColor(Color.rgb(119, 250, 250));
            //mAvatarView = view.findViewById(R.id.contact_avatar);
            mTitleView = (TextView) view.findViewById(R.id.InfoTitle);
            mNameView = (TextView) view.findViewById(R.id.InfoName);
            mSurnameView = (TextView) view.findViewById(R.id.Surname);
            mPublisherView = (TextView) view.findViewById(R.id.InfoPublisher);
            mISBNView = (TextView) view.findViewById(R.id.InfoISBN);

            deleteButton = view.findViewById(R.id.book_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}


