package ru.reksoft.chaplygin.catalog.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disciplinaryActionSeq")
    @SequenceGenerator(name = "disciplinaryActionSeq", sequenceName = "user_id_seq", allocationSize = 1)
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinTable(name = "user_sector",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "sector_id")})
    private Set<Sector> sectors;

    @Basic
    @Column(name = "agree")
    private Boolean agree;
}
