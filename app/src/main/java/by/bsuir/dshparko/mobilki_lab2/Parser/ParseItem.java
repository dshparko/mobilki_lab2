package by.bsuir.dshparko.mobilki_lab2.Parser;

public class ParseItem {
    private String imgUrl, name, price, detailUrl;



    public ParseItem(String imgUrl, String name, String price, String detailUrl) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.price = price;
        this.detailUrl = detailUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
