create table PHARMACY
(
    id            bigint                                not null
        primary key,
    address       varchar(255)                          null,
    name          varchar(255)                          null,
    longtitude    double                                null,
    latitude      double                                null,
    created_date  timestamp default current_timestamp() not null,
    modified_date timestamp default current_timestamp() null
);