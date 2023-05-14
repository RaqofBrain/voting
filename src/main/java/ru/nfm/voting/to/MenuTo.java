package ru.nfm.voting.to;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    @NotNull
    private LocalDate menuDate;

    @Size(min = 1)
    @NotNull
    private Set<Integer> dishIds;

    public MenuTo(Integer id, Set<Integer> dishIds) {
        super(id);
        this.dishIds = dishIds;
    }

    public MenuTo(Integer id, Set<Integer> dishIds, LocalDate menuDate) {
        this(id, dishIds);
        this.menuDate = menuDate;
    }
}
