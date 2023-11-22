DROP database if exists prj_final_lavacao;
CREATE database prj_final_lavacao;
USE prj_final_lavacao;

CREATE TABLE cor (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome varchar(100) NOT NULL
);
INSERT INTO cor (nome) VALUES('Branco'), ('Preto'), ('Cinza'), ('Azul'), ('Vermelho');

CREATE TABLE marca (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome varchar(100) NOT NULL
);
INSERT INTO marca (nome) VALUES('Fiat'), ('Jeep'), ('Chevrolet'), ('Volkswagen'), ('Ford');

CREATE TABLE modelo (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  descricao varchar(200) NOT NULL,
  id_marca int not null,
  foreign key (id_marca) references marca(id)
);
INSERT INTO modelo (descricao, id_marca) VALUES('Gol', 4), ('Ka', 5), ('Renegade', 2), ('Mobi', 1), ('Onix', 3);

CREATE TABLE veiculo ( 
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  placa varchar(100) NOT NULL,
  observacoes varchar(350),
  id_modelo int not null,
  id_cor int not null,
  foreign key (id_modelo) references modelo(id),
  foreign key (id_cor) references cor(id)
);
INSERT INTO veiculo (placa, observacoes, id_modelo, id_cor) VALUES('3F4P', 'Bola', 1, 1), ('RT57F', '', 3, 5), ('M29EK', '', 5, 2), ('B47PR', '', 4, 4), ('LQX3Y', '', 2, 3);