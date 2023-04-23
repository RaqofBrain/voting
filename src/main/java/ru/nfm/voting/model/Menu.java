package ru.nfm.voting.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "menu")
@Getter
@Setter
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToMany
    @JoinTable(
            name = "menu_dishes",
            joinColumns = @JoinColumn(name = "menu_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "dish_id", nullable = false))
    @ToString.Exclude
    private Set<Dish> dishes;
}
