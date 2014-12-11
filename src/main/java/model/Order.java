package model;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 Represents one customer oder
 */
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;

    /** customer owning this order */
    @ManyToOne(targetEntity = Customer.class, cascade = CascadeType.ALL )
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderState state = OrderState.NEW;

    @Column
    private Date dateOrderCreated; // TODO replace with LocalDate, integrate class implementig Enhanced user type

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="orderId")
    private Set<OrderItem> orderedGoods = new LinkedHashSet<>();

    Order() {
        dateOrderCreated = new Date();
    }

    public Order(ShoppingCart cart, Customer customer) {
        // TODO rework this associacian to unilateral
        Set<OrderItem> orderItems = cart.getProducts();
        orderItems.forEach(item->item.setOwningOrder(this));

        orderedGoods.addAll(orderItems);
        dateOrderCreated = new Date();
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDateOrderCreated() {
        return dateOrderCreated;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    void setDateOrderCreated(Date dateOrderCreated) {
        this.dateOrderCreated = dateOrderCreated;
    }

    public OrderState getState() {
        return state;
    }

    void setState(OrderState state) {
        this.state = state;
    }

    public Set<OrderItem> getOrderedGoods() {
        return orderedGoods;
    }

    void setOrderedGoods(Set<OrderItem> orderedGoods) {
        this.orderedGoods = orderedGoods;
    }

    public boolean containsProduct(Product product) {
        return orderedGoods.stream().anyMatch(item -> item.getProduct().equals(product));
    }

    public int getProductQuantity(Product product) {
        return orderedGoods.stream()
                .filter(item -> item.getProduct().equals(product))
                .mapToInt(OrderItem::getQuantity)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(("Cannot get quantity for product " + product + ", order does not contain it")));
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", state=" + state +
                ", dateOrderCreated=" + dateOrderCreated +
                ", orderedGoods=" + orderedGoods +
                '}';
    }
}
