create sequence if not exists balance_sequence start with 1 increment by 1;

create table if not exists balance (
id int8 primary key,
balance numeric
);

create table if not exists spend(
    id int8 generated by default as identity primary key,
    spend numeric,
    balance_id int8,
    foreign key(balance_id) references balance(id)
);

create table if not exists income(
    id int8 generated by default as identity primary key,
    income numeric,
    balance_id int8,
    foreign key(balance_id) references balance(id)
);