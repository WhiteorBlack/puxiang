package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.puxiang.mall.BR;


public class RxPlate extends BaseObservable {
    private String createTime;
    private String plateNavigationPic;
    private int sort;
    private boolean isAttented;
    private String postQty;
    private String id;
    private String plateTypeCode;
    private PlateTypeBean plateType;
    private String platePic;
    private String plateName;
    private String description;
    private String attentionQty;
    private int plateTypeId;
    private String plateIntroduce;
    private boolean select;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPlateNavigationPic() {
        return plateNavigationPic;
    }

    public void setPlateNavigationPic(String plateNavigationPic) {
        this.plateNavigationPic = plateNavigationPic;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Bindable
    public boolean getIsAttented() {
        return isAttented;
    }

    public void setIsAttented(boolean isAttented) {
        this.isAttented = isAttented;
        notifyPropertyChanged(BR.isAttented);
    }

    public String getPostQty() {
        return postQty;
    }

    public void setPostQty(String postQty) {
        this.postQty = postQty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlateTypeCode() {
        return plateTypeCode;
    }

    public void setPlateTypeCode(String plateTypeCode) {
        this.plateTypeCode = plateTypeCode;
    }

    public PlateTypeBean getPlateType() {
        return plateType;
    }

    public void setPlateType(PlateTypeBean plateType) {
        this.plateType = plateType;
    }

    public String getPlatePic() {
        return platePic;
    }

    public void setPlatePic(String platePic) {
        this.platePic = platePic;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttentionQty() {
        return attentionQty;
    }

    public void setAttentionQty(String attentionQty) {
        this.attentionQty = attentionQty;
    }

    public int getPlateTypeId() {
        return plateTypeId;
    }

    public void setPlateTypeId(int plateTypeId) {
        this.plateTypeId = plateTypeId;
    }

    public String getPlateIntroduce() {
        return plateIntroduce;
    }

    public void setPlateIntroduce(String plateIntroduce) {
        this.plateIntroduce = plateIntroduce;
    }

    @Bindable
    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        notifyPropertyChanged(BR.select);
    }

    @Override
    public String toString() {
        return "{" +
                "createTime='" + createTime + '\'' +
                ", plateNavigationPic='" + plateNavigationPic + '\'' +
                ", sort=" + sort +
                ", isAttented=" + isAttented +
                ", postQty=" + postQty +
                ", id='" + id + '\'' +
                ", plateTypeCode='" + plateTypeCode + '\'' +
                ", plateType=" + plateType +
                ", platePic='" + platePic + '\'' +
                ", plateName='" + plateName + '\'' +
                ", description='" + description + '\'' +
                ", attentionQty=" + attentionQty +
                ", plateTypeId=" + plateTypeId +
                ", plateIntroduce='" + plateIntroduce + '\'' +
                ", select=" + select +
                '}';
    }

    public static class PlateTypeBean {
        private int id;
        private int parentId;
        private int sort;
        private String pathName;
        private String pathId;
        private String description;
        private String iconUrl;
        private String name;
        private int contentQuantity;
        private String code;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getPathName() {
            return pathName;
        }

        public void setPathName(String pathName) {
            this.pathName = pathName;
        }

        public String getPathId() {
            return pathId;
        }

        public void setPathId(String pathId) {
            this.pathId = pathId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getContentQuantity() {
            return contentQuantity;
        }

        public void setContentQuantity(int contentQuantity) {
            this.contentQuantity = contentQuantity;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", parentId=" + parentId +
                    ", sort=" + sort +
                    ", pathName='" + pathName + '\'' +
                    ", pathId='" + pathId + '\'' +
                    ", description='" + description + '\'' +
                    ", iconUrl='" + iconUrl + '\'' +
                    ", name='" + name + '\'' +
                    ", contentQuantity=" + contentQuantity +
                    ", code='" + code + '\'' +
                    '}';
        }
    }
}
