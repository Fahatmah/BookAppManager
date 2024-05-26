package com.activity.bookappmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder>{

    private Context context;

    public BookAdapter(Context context, List<BookClass> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    private List<BookClass> bookList;

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Glide.with(context).load(bookList.get(position).getBookCover()).into(holder.bookCover);
        holder.bookTitle.setText(bookList.get(position).getBookTitle());
        holder.bookDesc.setText(bookList.get(position).getBookDesc());
        holder.bookAuthor.setText(bookList.get(position).getBookAuthor());
        holder.recycleItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("Cover", bookList.get(holder.getAdapterPosition()).getBookCover());
                intent.putExtra("Description", bookList.get(holder.getAdapterPosition()).getBookDesc());
                intent.putExtra("Title", bookList.get(holder.getAdapterPosition()).getBookTitle());
                intent.putExtra("Author", bookList.get(holder.getAdapterPosition()).getBookAuthor());
                intent.putExtra("Key",bookList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void searchDataList(ArrayList<BookClass> searchList){
        bookList = searchList;
        notifyDataSetChanged();
    }
}

class BookViewHolder extends RecyclerView.ViewHolder{
    ImageView bookCover;
    TextView bookTitle, bookAuthor, bookDesc;
    CardView recycleItemCard;
    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
        bookCover = itemView.findViewById(R.id.recycleBookCover);
        bookTitle = itemView.findViewById(R.id.recycleBookTitle);
        bookAuthor = itemView.findViewById(R.id.recycleBookAuthor);
        bookDesc = itemView.findViewById(R.id.recycleBookDescription);
        recycleItemCard = itemView.findViewById(R.id.recycleItemCard);
    }
}
