package com.puxiang.mall.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RxChannel implements Parcelable {

    /**
     * parentId : null
     * channelName : 视频
     * channelType : 1
     * channelCode : shipin
     * channelId : 35
     * children : [{"parentId":35,"channelName":"英雄联盟","channelType":"1","channelCode":"shipin1","channelId":36,
     * "contentType":2,"typeId":26},{"parentId":35,"channelName":"守望先锋","channelType":"1",
     * "channelCode":"shouwangxianfeng","channelId":37,"contentType":2,"typeId":27}]
     * contentType : 2
     * typeId : 25
     */

    public final static int CONTENT_TYPE_HOME = 1;
    public final static int CONTENT_TYPE_VIDEO = 2;
    public final static int CONTENT_TYPE_PLATE = 1000;

    private String parentId;
    private String channelName;
    private String channelType;
    private String channelCode;
    private int channelId;
    private int contentType;
    private int typeId;

    @Override
    public String toString() {
        return "{" +
                "parentId='" + parentId + '\'' +
                ", channelName='" + channelName + '\'' +
                ", channelType='" + channelType + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", channelId=" + channelId +
                ", contentType=" + contentType +
                ", typeId=" + typeId +
                ", children=" + children +
                '}';
    }

    /**
     * parentId : 35
     * channelName : 英雄联盟
     * channelType : 1
     * channelCode : shipin1
     * channelId : 36
     * contentType : 2
     * typeId : 26
     */


    private ArrayList<RxChildren> children;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
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

    public ArrayList<RxChildren> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<RxChildren> children) {
        this.children = children;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.parentId);
        dest.writeString(this.channelName);
        dest.writeString(this.channelType);
        dest.writeString(this.channelCode);
        dest.writeInt(this.channelId);
        dest.writeInt(this.contentType);
        dest.writeInt(this.typeId);
        dest.writeTypedList(this.children);
    }

    public RxChannel() {
    }

    protected RxChannel(Parcel in) {
        this.parentId = in.readString();
        this.channelName = in.readString();
        this.channelType = in.readString();
        this.channelCode = in.readString();
        this.channelId = in.readInt();
        this.contentType = in.readInt();
        this.typeId = in.readInt();
        this.children = in.createTypedArrayList(RxChildren.CREATOR);
    }

    public static final Parcelable.Creator<RxChannel> CREATOR = new Parcelable.Creator<RxChannel>() {
        @Override
        public RxChannel createFromParcel(Parcel source) {
            return new RxChannel(source);
        }

        @Override
        public RxChannel[] newArray(int size) {
            return new RxChannel[size];
        }
    };
}
