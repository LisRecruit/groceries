package com.example.groceries.store.store_family;

import com.example.groceries.family.Family;
import com.example.groceries.store.Store;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "store_family_meta")
public class StoreFamilyMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "store_family_meta_seq",
            sequenceName = "seq_store_family_meta_id",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @Column(name = "custom_name")
    private String customName;
    @Column(name = "description")
    private String description;

}


