package com.example.paymenttracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Transaction> transaction;
    private Context context;
    private LayoutInflater mInflater;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    RecyclerViewAdapter(Context context, ArrayList<Transaction> t){
        mInflater = LayoutInflater.from(context);
        transaction = t;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(v);
        //different layout for debts and receivables (red/green)
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(holder.itemView.getResources().getString(R.string.date_format));
        holder.recipientTextView.setText(transaction.get(position).getRecipient());
        holder.amountTextView.setText(decimalFormat.format(transaction.get(position).getAmount()));
        holder.dateTextView.setText(dateFormat.format(transaction.get(position).getDate()));
        holder.descriptionTextView.setText(transaction.get(position).getDescription());

       switch (transaction.get(position).getCategory()){
           case 0:
               holder.categoryImageView.setImageResource(R.drawable.groceries);
               break;
           case 1:
               holder.categoryImageView.setImageResource(R.drawable.food);
               break;
           case 2:
               holder.categoryImageView.setImageResource(R.drawable.apparel);
               break;
           case 3:
               holder.categoryImageView.setImageResource(R.drawable.bill);
               break;
           case 4:
               holder.categoryImageView.setImageResource(R.drawable.entertainment);
               break;
           case 5:
               holder.categoryImageView.setImageResource(R.drawable.transportation);
               break;
           case 6:
               holder.categoryImageView.setImageResource(R.drawable.health);
               break;
           case 7:
               holder.categoryImageView.setImageResource(R.drawable.home);
               break;
           case 8:
               holder.categoryImageView.setImageResource(R.drawable.gift);
               break;
           case 9:
               holder.categoryImageView.setImageResource(R.drawable.other);
               break;
       }

        holder.moreImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.remove, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.removeMenuItem:
                                transaction.remove(position);
                                notifyItemRemoved(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return transaction.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView recipientTextView;
        private TextView amountTextView;
        private TextView dateTextView;
        private TextView descriptionTextView;
        private ImageButton moreImageButton;
        private ImageView categoryImageView;

        public ViewHolder(View view) {
            super(view);
            recipientTextView = (TextView) view.findViewById(R.id.recipientTextView);
            amountTextView = (TextView) view.findViewById(R.id.amountTextView);
            dateTextView = (TextView) view.findViewById(R.id.dateTextView);
            descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
            moreImageButton = (ImageButton) view.findViewById(R.id.moreImageButton);
            categoryImageView = (ImageView) view.findViewById(R.id.categoryImageView);

        }
    }

}
