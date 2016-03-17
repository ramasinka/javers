package org.javers.core.model;

import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.Id;

/**
 * @author akrystian
 */
@ShallowReference
public class ShallowPhone {
    @Id
    private Long id;
    private String number;
    private Category category;

    public ShallowPhone(Long id, String number, Category category) {
        this.id = id;
        this.number = number;
        this.category = category;
    }

    public ShallowPhone(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
