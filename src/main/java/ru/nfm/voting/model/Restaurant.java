package ru.nfm.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.nfm.voting.util.validation.NoHtml;

import java.util.List;

@Entity
@Table(name = "restaurant",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"name", "address"}, name = "restaurant_unique_name_address_idx")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "phone_number")
    @NotBlank
    @Size(min = 5, max = 13)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 2, max = 150)
    @NoHtml
    private String address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Menu> menus;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private List<Vote> votes;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Integer id, String name, String phoneNumber, String address) {
        this(id, name);
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
