package edu.pet.cloudstorage.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name="Roles")
public class Role implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    /*@ManyToMany(mappedBy="roles")
    private List<User> users;*/

    public Role(String name){
        this.name = name;
    }
}
