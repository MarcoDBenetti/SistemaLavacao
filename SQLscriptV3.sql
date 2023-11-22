DROP database if exists prj_final_lavacao;
CREATE database prj_final_lavacao;
USE prj_final_lavacao;

CREATE TABLE cor (
  id int NOT NULL AUTO_INCREMENT,
  nome varchar(100) NOT NULL,
  CONSTRAINT pk_cor primary key (id_cor)
);
INSERT INTO cor (nome) VALUES('Branco'), ('Preto'), ('Cinza'), ('Azul'), ('Vermelho');

CREATE TABLE marca (
  id int NOT NULL AUTO_INCREMENT,
  nome varchar(100) NOT NULL,
  CONSTRAINT pk_marca primary key (id_marca)
);
INSERT INTO marca (nome) VALUES('Fiat'), ('Jeep'), ('Chevrolet'), ('Volkswagen'), ('Ford');

CREATE TABLE modelo (
  id int NOT NULL AUTO_INCREMENT,
  descricao varchar(200) NOT NULL,
  id_marca int not null,
  CONSTRAINT pk_modelo primary key (id_modelo),
  CONSTRAINT fk_modelo_marca foreign key (id_marca) references marca(id)
);
INSERT INTO modelo (descricao, id_marca) VALUES('Gol', 4), ('Ka', 5), ('Renegade', 2), ('Mobi', 1), ('Onix', 3);

CREATE TABLE veiculo ( 
  id int NOT NULL AUTO_INCREMENT,
  placa varchar(100) NOT NULL,
  observacoes varchar(350),
  id_modelo int not null,
  id_cor int not null,
  constraint pk_veiculo primary key (id_veiculo),
  constraint fk_veiculo_modelo foreign key (id_modelo) references modelo(id),
  constraint fk_veiculo_cor foreign key (id_cor) references cor(id)
);
INSERT INTO veiculo (placa, observacoes, id_modelo, id_cor) VALUES('3F4P', 'Bola', 1, 1), ('RT57F', '', 3, 5), ('M29EK', '', 5, 2), ('B47PR', '', 4, 4), ('LQX3Y', '', 2, 3);

CREATE TABLE motor ( 
  id int NOT NULL references modelo(id),
  potencia int not null,
  tipo_combustivel ENUM('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') NOT NULL DEFAULT 'GASOLINA',
  constraint pk_motor primary key (id_motor),
  constraint fk_motor_modelo foreign key (id_motor) references modelo(id) on delete cascade
);