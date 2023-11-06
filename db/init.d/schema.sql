create table pharmacy
(
    id            bigint       not null auto_increment
        primary key,
    address       varchar(255) null,
    name          varchar(255) null,
    longtitude    double       not null,
    latitude      double       not null,
    created_date  timestamp    null,
    modified_date timestamp    null
);

create table search_info
(
    id            bigint       not null auto_increment
        primary key,
    base_address  varchar(255) not null,
    base_lng      double       not null,
    base_lat      double       not null,
    created_date  timestamp    null,
    modified_date timestamp    null
);

create table search_infos_pharmacies
(
    id             bigint not null auto_increment
        primary key,
    search_info_id bigint null,
    pharmacy_id    bigint null,
    constraint search_infos_pharmacies_pharmacy_id_fk
        foreign key (pharmacy_id) references pharmacy (id),
    constraint search_infos_pharmacies_search_info_id_fk
        foreign key (search_info_id) references search_info (id)
);

create table shorten_url
(
    id           bigint auto_increment
        primary key,
    original_url varchar(500) not null,
    constraint shorten_url_pk
        unique (original_url)
);
