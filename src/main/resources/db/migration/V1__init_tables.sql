create table if not exists balance (
balance_id int8 primary key,
amount numeric not null default 0
);

create table if not exists balance_transaction (
    transaction_id int8 primary key,
    balance_id int8 not null references balance(balance_id),
    amount numeric(19, 4),
    operation_type int4,
    date_of_operation timestamp not null default now(),
    message varchar(255)
);

create table if not exists spend_balance_transaction (
    transaction_id int8 primary key references balance_transaction(transaction_id),
    type_of_spending_operation varchar(255),
    receiver_id int8 references balance(balance_id)
);

create table if not exists income_balance_transaction (
    transaction_id int8 primary key references balance_transaction(transaction_id)
);