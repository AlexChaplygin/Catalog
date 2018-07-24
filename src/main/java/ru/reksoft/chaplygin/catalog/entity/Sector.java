package ru.reksoft.chaplygin.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sectors")
@Getter
@Setter
public class Sector {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actionSeq")
    @SequenceGenerator(name = "actionSeq", sequenceName = "sector_id_seq", allocationSize = 1)
    private Integer id;

    // Is a table column
    // It identifies the parent of current row
    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonIgnore
    private Sector parent;

    //Is not a table column
    // It is a collection of those rows that have this row as a parent.
    @OneToMany(mappedBy = "parent")
    private Set<Sector> items;

    @Basic
    @Column(name = "name")
    private String title;

}
