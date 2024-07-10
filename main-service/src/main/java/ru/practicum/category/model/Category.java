package ru.practicum.category.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "categories", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
}
