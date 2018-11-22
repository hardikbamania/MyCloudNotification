package com.eaziche.mycloudnotification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hardik on 13-02-2017.
 */

public class MessageModel implements Parcelable {
   private String to,from,msg,image,date,time;
    public MessageModel(){

    }
    public MessageModel(String to, String from, String msg, String date, String image, String time) {
        this.to = to;
        this.from = from;
        this.msg = msg;
        this.date = date;
        this.image = image;
        this.time = time;
    }

    protected MessageModel(Parcel in) {
        to = in.readString();
        from = in.readString();
        msg = in.readString();
        image = in.readString();
        date = in.readString();
        time = in.readString();
    }

    public MessageModel(String you, String me) {
        to = you;
        from = me;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(to);
        dest.writeString(from);
        dest.writeString(msg);
        dest.writeString(image);
        dest.writeString(date);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
