-- SCHEMA
create table Parts (
        pid number(38) primary key,
        pname varchar(38),
        year number(6),
        price number(12)
        );

create table Suppliers (
        sid number(38) primary key,
        sname varchar(20),
        state varchar(20),
        zipcode varchar(20)
        );

create table Orders (
        pid number(38),
        sid number(38),
        quantity number(38),
        primary key (pid, sid),
        foreign key (pid) references Parts,
        foreign key (sid) references Suppliers
        );
