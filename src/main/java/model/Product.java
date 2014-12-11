package model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="Product")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @ManyToOne(targetEntity = ProductCategory.class)
    @JoinColumn(name = "productCategoryId", nullable = false)
    private ProductCategory category;

    @Transient
    private ProductFeatures features;

    @Column
    private int amountOffered;

    @Column
    private boolean isPromoted;

    Product(){}

    public int getAmountOffered() {
        return amountOffered;
    }

    public void setAmountOffered(int amountOffered) {
        this.amountOffered = amountOffered;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (!category.getName().equals(product.category.getName())) return false;
        if (features != null ? !features.equals(product.features) : product.features != null) return false;
        if (!name.equals(product.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + category.getName().hashCode();
        result = 31 * result + (features != null ? features.hashCode() : 0);
        return result;
    }

    public Product(String name, ProductCategory category, BigDecimal price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public boolean getIsPromoted() { //TODO: singature isPromoted does not work: org.hibernate.PropertyNotFoundException: Could not find a getter for isPromoted in class model.Product
        return isPromoted;
    }

    void setIsPromoted(boolean isPromoted) {
        this.isPromoted = isPromoted;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductFeatures getFeatures() {
        return features;
    }

    void setFeatures(ProductFeatures features) {
        this.features = features;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }
}
