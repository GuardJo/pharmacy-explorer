package io.github.guardjo.pharmacyexplorer.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SEARCH_INFO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SearchInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String baseAddress;
    private double baseLng;
    private double baseLat;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "SEARCH_INFOS_PHARMACIES",
            joinColumns = @JoinColumn(name = "SEARCH_INFO_ID"),
            inverseJoinColumns = @JoinColumn(name = "PHARMACY_ID")
    )
    @Builder.Default
    private List<Pharmacy> pharmacies = new ArrayList<>();
}
