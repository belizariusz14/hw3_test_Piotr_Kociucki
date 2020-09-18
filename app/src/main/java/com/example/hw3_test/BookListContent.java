package com.example.hw3_test;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListContent {

    public static final List<Book> Books = new ArrayList<Book>();

    public static int lastID;

    public static void addItem(Book item) {
        Books.add(item);
    }

    public static void deleteItem(int position) {
        Book temp = Books.get(position);
        int id = temp.id;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books").document(Integer.toString(id)).delete();

        Books.remove(position);
    }

   /* private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/
    public static class Book implements Parcelable{
        public  int id;
        public  String Name;
        public  String Surname;
        public  String Title;
        public  String Publisher;
        public  String ISBN;

        @Exclude
        public int getid() {
            return id;
        }

        public void setid(int idd) {
            id = idd;
        }



        public String getName() {
            return Name;
        }

        public String getSurname() {
            return Surname;
        }

        public String getTitle() {
            return Title;
        }

        public String getPublisher() {
            return Publisher;
        }

        public String getISBN() {
            return ISBN;
        }

        public Book(){

        }

        public Book(int id, String name, String surname, String title, String publisher, String isbn) {
            this.id = id;
            this.Name = name; //author
            this.Surname = surname;//author
            this.Title = title;
            this.Publisher = publisher;
            this.ISBN = isbn;
        }

       public Book( String name, String surname, String title, String publisher, String isbn) {
           this.id = lastID;
           this.Name = name; //author
           this.Surname = surname;//author
           this.Title = title;
           this.Publisher = publisher;
           this.ISBN = isbn;
       }

        protected Book(Parcel in) {
            id = in.readInt();
            Name = in.readString();
            Surname = in.readString();
            Title = in.readString();
            Publisher = in.readString();
            ISBN = in.readString();
        }


        public static final Creator<Book> CREATOR = new Creator<Book>() {
            @Override
            public Book createFromParcel(Parcel in) {
                return new Book(in);
            }

            @Override
            public Book[] newArray(int size) {
                return new Book[size];
            }
        };

        public static void clear() {
        }

        @Override
        public String toString() {

            return Title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(Name);
            dest.writeString(Surname);
            dest.writeString(Title);
            dest.writeString(Publisher);
            dest.writeString(ISBN);
        }
    }


}

