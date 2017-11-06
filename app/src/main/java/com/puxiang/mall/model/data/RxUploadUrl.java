package com.puxiang.mall.model.data;

public class RxUploadUrl {

    /**
     * relativeUrl : attached/images/201611/15207d47669647f39fa471104d13455a29216.jpg
     * absoluteUrl : http://somicshop.oss-cn-shenzhen.aliyuncs
     * .com/attached/images/201611/15207d47669647f39fa471104d13455a29216.jpg
     * url : http://somicshop.oss-cn-shenzhen.aliyuncs
     * .com/attached/images/201611/15207d47669647f39fa471104d13455a29216.jpg
     */

    private String relativeUrl;
    private String absoluteUrl;
    private String url;
    private String filePath;

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    public void setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
