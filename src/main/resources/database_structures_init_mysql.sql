drop table if exists OrderItem;

drop table if exists Orders;

drop table if exists Customers;

drop table if exists Product;

drop table if exists ProductCategoryAliases;

drop table if exists ProductCategory;

drop table if exists LoggedUser;

create table ProductCategory (
    id int not null auto_increment,
    name varchar(100) unique,
    primary key(id)
);

create table ProductCategoryAliases (
    id int not null auto_increment,
    productCategoryId int not null,
    value varchar(100) not null,
    primary key(id),
    foreign key (productCategoryId) references ProductCategory(id)
);

create table Product (
    id int not null auto_increment,
    name varchar(100) not null,
    price numeric(10,2),
    productCategoryId int not null,
    amountOffered int,
    isPromoted boolean,
    primary key(id),
    foreign key(productCategoryId) references ProductCategory(id)
);


create table Customers (
    id int not null auto_increment,
    name varchar(30) not null,
    surname varchar(30),
    address varchar(200), -- for the time being free text
    primary key(id)
);

create table Orders (
    id int not null auto_increment,
    dateOrderCreated date not null,
    customerId int not null,
    state varchar(10) not null, -- add check constraint
    primary key(id),
    foreign key(customerId) references Customers(id)
);

create table OrderItem (
    id int not null auto_increment,
    orderId int not null,
    productId int not null,
    quantity int not null,
    unitPrice numeric(6,2) not null,
    primary key(id),-- WAS primary key(orderId, productId),
    foreign key(orderId) references Orders(id),
    foreign key(productId) references Product(id)
);

create table LoggedUser (
    id int not null auto_increment,
    login varchar(20) unique not null,
    password char(128) not null,
    primary key(id)
);

/* add initial, internal user */
INSERT INTO loggeduser (login, password) VALUES ('tomek', 'c77d74e935d0ba9b9479546ea819460cd53f9a3b3be34d73dffa840a20a92691471d84ab338701caa5a7849a4ba340a40de0c3ca67a61137bc001b11a14ea8a4');

COMMIT;
