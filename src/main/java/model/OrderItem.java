package model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OrderItem")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Order.class) // TODO rework this associacian to unilateral
    @JoinColumn(name="orderId", nullable = false)
    private Order owningOrder; // the order that this order item belongs to

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice; // price per one item

    OrderItem() {};

    OrderItem(Product product, int quantity, BigDecimal unitPrice, Order owningOrder) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.owningOrder = owningOrder;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public Order getOwningOrder() {
        return owningOrder;
    }

    public void setOwningOrder(Order owningOrder) {
        this.owningOrder = owningOrder;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (quantity != orderItem.quantity) return false;
        if (!product.equals(orderItem.product)) return false;
        if (!unitPrice.equals(orderItem.unitPrice)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = product.hashCode();
        result = 31 * result + quantity;
        result = 31 * result + unitPrice.hashCode();
        return result;
    }
}
