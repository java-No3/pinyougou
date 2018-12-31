package cn.itcast.core.pojo.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderCount implements Serializable{
    private Long goodsId;
    private Long itemId;
    private String title;
    private BigDecimal price;
    private Integer totalNum;
    private BigDecimal totalPrice;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderCount{" +
                "goodsId=" + goodsId +
                ", itemId=" + itemId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", totalNum=" + totalNum +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
