package model;

import javax.persistence.*;

@Entity
@Table(name = "ProductCategoryAliases")
public class ProductCategoryAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = ProductCategory.class)
    @JoinColumn(name="productCategoryId", nullable = false)
    private ProductCategory productCategory;

    @Column
    private String value;

    public ProductCategoryAlias() {}

    public ProductCategoryAlias(ProductCategory productCategory, String alias) {
        this.productCategory = productCategory;
        this.value = alias;
    }

    ProductCategory getProductCategory() {
        return productCategory;
    }

    String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductCategoryAlias that = (ProductCategoryAlias) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
