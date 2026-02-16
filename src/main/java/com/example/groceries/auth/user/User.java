package com.example.groceries.auth.user;

import com.example.groceries.family.Family;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", sequenceName = "seq_users_id", allocationSize = 1)
    private Long id;
    private String name;
    private String email;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "family_id")
    private Family family;
    @OneToOne (mappedBy = "owner")
    private Family owndFamily;
}
