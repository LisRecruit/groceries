package com.example.groceries.family;

import com.example.groceries.auth.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Families")
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "family_seq", sequenceName = "seq_families_id", allocationSize = 1)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private int code;
    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public void addUser(User user) {
        users.add(user);
        user.setFamily(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setFamily(null);
    }
}
