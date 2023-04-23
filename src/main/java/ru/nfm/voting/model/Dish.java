package ru.nfm.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    private Integer price;

    @Column(name = "created", nullable = false, updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created = new Date();

    @ManyToMany(mappedBy = "dishes")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private Set<Menu> menus;

    public Dish(Integer id, String name) {
        super(id, name);
    }

    public Dish(Integer id, String name, Integer price) {
        this(id, name);
        this.price = price;
    }
}