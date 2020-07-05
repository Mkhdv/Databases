-- SCHEMA
create table Students (
        sid number(38) primary key,
        sname varchar(38)
        );

create table Courses (
        cid number(38) primary key,
        cname varchar(20),
        credits number(20)
        );

create table Enrolled (
        sid number(38),
        cid number(38),
        primary key (sid, cid),
        foreign key (sid) references Students,
        foreign key (cid) references Courses
        );