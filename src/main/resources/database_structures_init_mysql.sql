drop table if exists Product;

drop table if exists ProductCategoryAliases;

drop table if exists ProductCategory;

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
    isPromoted boolean,
    primary key(id),
    foreign key(productCategoryId) references ProductCategory(id)
);

drop table if exists Customers;

create table Customers (
    id int not null auto_increment,
    name varchar(30) not null,
    surname varchar(30),
    address varchar(200), -- for the time being free text
    primary key(id)
);

drop table if exists Orders;

create table Orders (
    id int not null auto_increment,
    dateOrderCreated date not null,
    customerId int not null,
    state varchar(10) not null,
    primary key(id),
    foreign key(customerId) references Customers(id)
);

drop table if exists OrderedProducts;

create table OrderedProducts (
    orderId int not null,
    productId int not null,
    quantity int not null,
    primary key(orderId, productId),
    foreign key(orderId) references Orders(id),
    foreign key(productId) references Product(id)
);
