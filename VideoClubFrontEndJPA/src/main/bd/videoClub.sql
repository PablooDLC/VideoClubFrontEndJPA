drop database if exists videoClub;
create database videoClub;
use videoClub;

create table pelicula (
	id bigint,
    titulo varchar(255),
    descripcion varchar(255),
    añoPublicacion int,
    duracion varchar(255),
    categoria varchar(255),
    formato varchar(255),
    valoracion varchar(255),
    primary key (id)
);

create table equipo (
	id bigint,
    nombre varchar(255),
    apellidos varchar(255),
    pais varchar(255),
    añoNacimiento integer,
    rol varchar(255),
    idPelicula bigint,
    primary key(id),
    foreign key (idPelicula) references pelicula(id)
);

create table alquiler (
	id bigint,
    fechaAlquiler varchar(255),
	idCliente bigint,
    idPelicula bigint,
    fechaRetorno varchar(255),
    primary key(id),
    foreign key (idPelicula) references pelicula(id)
);

create table usuario (
	id bigint,
    nombre varchar(255),
    apellidos varchar(255),
    direccion varchar(255),
    activo boolean,
    fechaRegistro varchar(255),
    idAlquiler bigint,
    foreign key(idAlquiler) references alquiler(id)
);

select *
from pelicula;