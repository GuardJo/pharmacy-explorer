create table pharmacy
(
    id            bigint       not null
        primary key,
    address       varchar(255) null,
    name          varchar(255) null,
    longtitude    double       not null,
    latitude      double       not null,
    created_date  timestamp    null,
    modified_date timestamp    null
);