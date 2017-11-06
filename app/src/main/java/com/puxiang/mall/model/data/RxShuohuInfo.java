package com.puxiang.mall.model.data;

import android.os.Parcel;
import android.os.Parcelable;


public class RxShuohuInfo implements Parcelable {

    @Override
    public String toString() {
        return "RxShuohuInfo{" +
                "birthday='" + birthday + '\'' +
                ", weight='" + weight + '\'' +
                ", maker='" + maker + '\'' +
                ", introduce='" + introduce + '\'' +
                ", parent='" + parent + '\'' +
                ", robotName='" + robotName + '\'' +
                ", father='" + father + '\'' +
                ", height='" + height + '\'' +
                ", mother='" + mother + '\'' +
                ", birthplace='" + birthplace + '\'' +
                ", userName='" + userName + '\'' +
                ", gender=" + gender +
                ", linkName='" + linkName + '\'' +
                ", linkPic='" + linkPic + '\'' +
                ", robotIcon='" + robotIcon + '\'' +
                '}';
    }

    /**
     * birthday : 2016-02-01
     * weight : 约重3斤
     * maker : 硕美科科技
     * introduce : 你好，我是硕虎，人类朋友都喜欢叫我：虎子或猫猫，我和我的主人钱包住在广州，平时我喜欢抓老鼠，听音乐，玩耍，叫主人起床。电视里要是演《猫和老鼠》我一定不会错过。汤姆如果换成我的话，吉米早就被抓住了。不过，就算抓了吉米，我也不会吃它。而是放走……然后再抓……再放走……再抓……那才过瘾，喵呜！我也能帮主人和帅哥美女制造牵线搭桥的机会，只要我过去卖个萌，主人就有机会上前搭讪了。不过，事后要给我买个两居室哦。不然，我就捣乱了……
     * parent : 人类
     * robotName : 小硕
     * father : 广州动物园4岁的雄性东北虎
     * height : 28厘米
     * mother : 广州动物园3岁雌性东北虎，分娩硕虎时因为难产，去世。
     * birthplace : 广州
     * userName : 硕虎
     * gender : 0
     * linkName : 智能问答
     * linkPic :
     * robotIcon :
     */

    private String birthday;
    private String weight;
    private String maker;
    private String introduce;
    private String parent;
    private String robotName;
    private String father;
    private String height;
    private String mother;
    private String birthplace;
    private String userName;
    private int gender;
    private String linkName;
    private String linkPic;
    private String robotIcon;
    private String welcome;
    private String voice;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkPic() {
        return linkPic;
    }

    public void setLinkPic(String linkPic) {
        this.linkPic = linkPic;
    }

    public String getRobotIcon() {
        return robotIcon;
    }

    public void setRobotIcon(String robotIcon) {
        this.robotIcon = robotIcon;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.birthday);
        dest.writeString(this.weight);
        dest.writeString(this.maker);
        dest.writeString(this.introduce);
        dest.writeString(this.parent);
        dest.writeString(this.robotName);
        dest.writeString(this.father);
        dest.writeString(this.height);
        dest.writeString(this.mother);
        dest.writeString(this.birthplace);
        dest.writeString(this.userName);
        dest.writeInt(this.gender);
        dest.writeString(this.linkName);
        dest.writeString(this.linkPic);
        dest.writeString(this.robotIcon);
        dest.writeString(this.welcome);
        dest.writeString(this.voice);
    }

    public RxShuohuInfo() {
    }

    protected RxShuohuInfo(Parcel in) {
        this.birthday = in.readString();
        this.weight = in.readString();
        this.maker = in.readString();
        this.introduce = in.readString();
        this.parent = in.readString();
        this.robotName = in.readString();
        this.father = in.readString();
        this.height = in.readString();
        this.mother = in.readString();
        this.birthplace = in.readString();
        this.userName = in.readString();
        this.gender = in.readInt();
        this.linkName = in.readString();
        this.linkPic = in.readString();
        this.robotIcon = in.readString();
        this.welcome = in.readString();
        this.voice = in.readString();
    }

    public static final Parcelable.Creator<RxShuohuInfo> CREATOR = new Parcelable.Creator<RxShuohuInfo>() {
        @Override
        public RxShuohuInfo createFromParcel(Parcel source) {
            return new RxShuohuInfo(source);
        }

        @Override
        public RxShuohuInfo[] newArray(int size) {
            return new RxShuohuInfo[size];
        }
    };
}
