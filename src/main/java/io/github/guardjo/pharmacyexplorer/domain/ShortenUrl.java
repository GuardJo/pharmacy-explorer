package io.github.guardjo.pharmacyexplorer.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "SHORTEN_URL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ShortenUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false, unique = true)
    private String originalUrl;
}
