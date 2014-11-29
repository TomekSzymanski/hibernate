package model;

import java.math.BigDecimal;

//@Entity
//@Table(name="product")
public class Product {

//    @Id @GeneratedValue
//    @Column(name = "id")
    private int id;

//    @Column(name="name")
    private String name;

//    @Column(name="price")
    private BigDecimal price;

//    @Column()
    private ProductCategory category;

    private ProductFeatures features;

    private int amountOffered;

//    @Column(name = "isPromoted")
    private boolean isPromoted;

    Product(){}

    public int getAmountOffered() {
        return amountOffered;
    }

    void setAmountOffered(int amountOffered) {
        this.amountOffered = amountOffered;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (!category.equals(product.category)) return false;
        if (features != null ? !features.equals(product.features) : product.features != null) return false;
        if (!name.equals(product.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + (features != null ? features.hashCode() : 0);
        return result;
    }

    public Product(String name, ProductCategory category) {
        this.name = name;
        this.category = category;
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
