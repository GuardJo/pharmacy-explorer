package io.github.guardjo.pharmacyexplorer.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "PHARMACY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Pharmacy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String name;
    private double longtitude;
    private double latitude;
}
