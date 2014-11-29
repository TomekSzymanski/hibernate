package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ProductCategory")
public class ProductCategory {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

//    /** the features that you can specify as search conditions in searching for products belonging to given category */
//    private ProductFeatures filterableFeatures;

    /** all other names that this category may be referred or known as, or searched for */
    @ElementCollection
    @CollectionTable(name = "ProductCategoryAliases", joinColumns = @JoinColumn(name = "productCategoryId"))
    @Column(name = "value")
    private Set<String> aliases = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductCategory category = (ProductCategory) o;

        if (id != category.id) return false;
        if (aliases != null ? !aliases.equals(category.aliases) : category.aliases != null) return false;
        if (!name.equals(category.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (aliases != null ? aliases.hashCode() : 0);
        return result;
    }

    ProductCategory() {}

    public ProductCategory(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }
}
