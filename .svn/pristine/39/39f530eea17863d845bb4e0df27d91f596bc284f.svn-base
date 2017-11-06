package com.puxiang.mall.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RxChildren implements Parcelable {
    private int parentId;
    private String channelName;
    private String channelType;
    private String channelCode;
    private int channelId;
    private int contentType;
    private int typeId;

    @Override
    public String toString() {
        return "{" +
                "parentId=" + parentId +
                ", channelName='" + channelName + '\'' +
                ", channelType='" + channelType + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", channelId=" + channelId +
                ", contentType=" + contentType +
                ", typeId=" + typeId +
                '}';
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.parentId);
        dest.writeString(this.channelName);
        dest.writeString(this.channelType);
        dest.writeString(this.channelCode);
        dest.writeInt(this.channelId);
        dest.writeInt(this.contentType);
        dest.writeInt(this.typeId);
    }

    public RxChildren() {
    }

    protected RxChildren(Parcel in) {
        this.parentId = in.readInt();
        this.channelName = in.readString();
        this.channelType = in.readString();
        this.channelCode = in.readString();
        this.channelId = in.readInt();
        this.contentType = in.readInt();
        this.typeId = in.readInt();
    }

    public static final Parcelable.Creator<RxChildren> CREATOR = new Parcelable.Creator<RxChildren>() {
        @Override
        public RxChildren createFromParcel(Parcel source) {
            return new RxChildren(source);
        }

        @Override
        public RxChildren[] newArray(int size) {
            return new RxChildren[size];
        }
    };
}
