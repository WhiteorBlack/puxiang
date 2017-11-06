package com.puxiang.mall.model.data;


public class RxEsport {
    /**
     * id : 9
     * distance :
     * linkPhone : 11
     * isAttented : 0
     * address : 广东省广州市天河区天河路490号
     * name : 硕美科电竞馆2
     * lowestPerHour : 11.0
     * longitude : 113.33499848842621
     * feature : 2
     * latitude : 23.13278295729362
     * pic : {"description":"","pic":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201705/5c7d6c249ba242f3bc0f6d3249eabdf543690.jpg?2400x1800"}
     * code : 2
     */

    private String id;
    private String distance;
    private String linkPhone;
    private int isAttented;
    private String address;
    private String name;
    private double lowestPerHour;
    private String longitude;
    private String feature;
    private String latitude;
    private PicBean pic;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public int getIsAttented() {
        return isAttented;
    }

    public void setIsAttented(int isAttented) {
        this.isAttented = isAttented;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLowestPerHour() {
        return lowestPerHour;
    }

    public void setLowestPerHour(double lowestPerHour) {
        this.lowestPerHour = lowestPerHour;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public PicBean getPic() {
        return pic;
    }

    public void setPic(PicBean pic) {
        this.pic = pic;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class PicBean {
        /**
         * description :
         * pic : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201705/5c7d6c249ba242f3bc0f6d3249eabdf543690.jpg?2400x1800
         */

        private String description;
        private String pic;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
