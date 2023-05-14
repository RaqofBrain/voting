package ru.nfm.voting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"menu_date", "restaurant_id"}, name = "menu_date_restaurant_id_unq_cs")
})
@NamedEntityGraph(name = "Menu.dishList",
        attributeNodes = @NamedAttributeNode("dishList"))
@NamedEntityGraph(name = "Menu.dishListWithRestaurant",
        attributeNodes = {@NamedAttributeNode("dishList"), @NamedAttributeNode("restaurant")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @ToString.Exclude
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dish_menu",
            joinColumns = @JoinColumn(name = "menu_id", nullable = false, referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id", nullable = false, referencedColumnName = "id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "dish_menu_menu_id_dish_id_unq_cs")})
    @ToString.Exclude
    private List<Dish> dishList;

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<Dish> dishList) {
        this(id, menuDate, restaurant);
        this.dishList = dishList;
    }
}