package com.eaziche.mycloudnotification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by hardik on 29-03-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<MessageModel> messageList;
    private String from;
    ChatAdapter(Context context, ArrayList<MessageModel> messageList,String from) {
        this.context = context;
        this.messageList = messageList;
        this.from = from;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
     if (viewType==1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_layout, parent, false);
     else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.you_layout, parent, false);

        return new MyViewHolder(view, context);
    }

    public int getItemViewType(int position) {
        if(from.equalsIgnoreCase(messageList.get(position).getFrom()))
                return 1;
        else
                return 0;

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(messageList.get(position).getMsg());
        if (!messageList.get(position).getImage().equals("")) {
            holder.simpleDraweeView.setImageURI(messageList.get(position).getImage());
            holder.simpleDraweeView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        TextView textView;
        SimpleDraweeView simpleDraweeView  ;
        MyViewHolder(View itemView, Context context) {
            super(itemView);
                    this.context = context;
            textView = (TextView)itemView.findViewById(R.id.textMessage);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee);
        }
    }
}
