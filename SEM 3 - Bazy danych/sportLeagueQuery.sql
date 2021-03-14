CREATE DATABASE SportLeague;
use sportleague;
CREATE TABLE UzytkownikAplikacji (
idUzytkownika int auto_increment primary key not null,
login varchar(30) unique not null,
haslo varchar(255) not null,
imie varchar(20),
nazwisko varchar(20),
pesel varchar(11),
poziomUprawnien int not null,
czyZatwierdzony bool default false not null
);

DELIMITER //
CREATE PROCEDURE dodajUzytkownika (login VARCHAR(30), haslo VARCHAR(255), imie VARCHAR(20), nazwisko VARCHAR(20), pesel char(11), poziomUprawnien int)
BEGIN
	INSERT INTO uzytkownikaplikacji (login, haslo, imie, nazwisko, pesel, poziomUprawnien, czyZatwierdzony) VALUES (login, sha1(haslo), imie, nazwisko, pesel, poziomUprawnien, false);
END //

INSERT INTO uzytkownikaplikacji (idUzytkownika, login, haslo, poziomUprawnien, czyZatwierdzony) VALUES (1, "admin", sha1('ZAQ!2wsx'), 1, true);

DELIMITER //
CREATE PROCEDURE zaloguj (nick VARCHAR(30), haslo VARCHAR(255))
BEGIN
DECLARE pass1, pass2 VARCHAR(255);
	SET pass1 = sha1(haslo);
    SET pass2 = (SELECT uzytkownikaplikacji.haslo FROM uzytkownikaplikacji WHERE login = nick AND czyZatwierdzony = true);
    IF pass1 = pass2 THEN SELECT 1;
    ELSE SELECT 0;
    END IF;
END //

DELIMITER //
CREATE PROCEDURE sprawdzHaslo (haslo VARCHAR(255), id int)
BEGIN
DECLARE pass1, pass2 VARCHAR(255);
	SET pass1 = sha1(haslo);
    SET pass2 = (SELECT uzytkownikaplikacji.haslo FROM uzytkownikaplikacji WHERE idUzytkownika = id);
    IF pass1 = pass2 THEN SELECT 1;
    ELSE SELECT 0;
    END IF;
END //

INSERT INTO uzytkownikaplikacji (idUzytkownika, login, haslo, poziomUprawnien, czyZatwierdzony) VALUES (1, "admin", sha1('ZAQ!2wsx'), 1, true);

CREATE TABLE Druzyna (
 idDruzyny int auto_increment primary key not null,
 nazwaDruzyny varchar(20) not null,
 miasto varchar(20) not null,
 rokZalozenia int not null,
 dywizja enum("Open", "Women", "Mixed") not null,
 zwyciestwa int default 0 not null,
 remisy int default 0 not null,
 porazki int default 0 not null,
 zdobytePunkty int default 0 not null,
 idUzytkownikaKapitana int not null,
 foreign key (idUzytkownikaKapitana) references uzytkownikaplikacji(idUzytkownika)
);

INSERT INTO Druzyna(nazwaDruzyny, miasto, rokZalozenia, dywizja, idUzytkownikaKapitana) VALUES ("71 Wratislavia", "Wrocław", 2014, "Mixed", 1);
INSERT INTO Druzyna(nazwaDruzyny, miasto, rokZalozenia, dywizja, idUzytkownikaKapitana) VALUES ("BLOW", "Wrocław", 2010, "Mixed", 1);
INSERT INTO Druzyna(nazwaDruzyny, miasto, rokZalozenia, dywizja, idUzytkownikaKapitana) VALUES ("KWR Knury", "Kamieniec Wrocławski", 2012, "Mixed", 1);

CREATE TABLE Turniej (
idTurnieju int auto_increment primary key not null,
nazwaTurnieju varchar(20) not null,
dataTurnieju date not null,
miejsce varchar(20) not null,
dywizjaTurnieju enum("Open", "Women", "Mixed", "Open/Women") not null,
idOrganizatora int not null
);
CREATE TABLE Zawodnik (
idZawodnika int auto_increment primary key not null,
idDruzyny int not null,
imie varchar(20) not null,
nazwisko varchar(20) not null,
plec enum("Mężczyzna", "Kobieta") not null,
rokUrodzenia int not null,
numerZawodnika int not null,
zdobytePunkty int default 0,
foreign key (idDruzyny) references Druzyna(idDruzyny)
);
CREATE TABLE Mecz (
idMeczu int auto_increment primary key not null,
idDruzynyPierwszej int not null,
idDruzynyDrugiej int not null,
punktyDruzynyPierwszej int not null,
punktyDruzynyDrugiej int not null,
idTurnieju int not null,
czyZakonczony bool default false,
foreign key (idTurnieju) references Turniej(idTurnieju),
foreign key (idDruzynyPierwszej) references Druzyna(idDruzyny),
foreign key (idDruzynyDrugiej) references Druzyna(idDruzyny)
);
CREATE TABLE punktacjaMeczu (
id int primary key auto_increment,
idMeczu int not null,
idZawodnika int not null,
punktyDruzynyPierwszejPoPunkcie int not null,
punktyDruzynyDrugiejPoPunkcie int not null,
foreign key (idMeczu) references Mecz(idMeczu),
foreign key (idZawodnika) references Zawodnik(idZawodnika)
);

DELIMITER //
CREATE PROCEDURE dodajPunkt (meczuId int, druzynyId int, zawodnikaNumer int, czyPierwsza VARCHAR(10))
BEGIN
DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
    ROLLBACK;
    SELECT 'SQLEXCEPTION';
END;
START TRANSACTION;
	IF czyPierwsza = 'true' THEN
		UPDATE mecz SET punktyDruzynyPierwszej = punktyDruzynyPierwszej + 1 WHERE idMeczu = meczuId AND idDruzynyPierwszej = druzynyId;
    ELSEIF czyPierwsza = 'false' THEN
		UPDATE mecz SET punktyDruzynyDrugiej = punktyDruzynyDrugiej + 1 WHERE idMeczu = meczuId AND idDruzynyDrugiej = druzynyId;
    END IF;
    UPDATE zawodnik SET zdobytePunkty = zdobytePunkty + 1 WHERE idDruzyny = druzynyId AND numerZawodnika = zawodnikaNumer;
    INSERT INTO punktacjaMeczu (idMeczu, idZawodnika, punktyDruzynyPierwszejPoPunkcie, punktyDruzynyDrugiejPoPunkcie) VALUES (meczuId, 
		(SELECT idZawodnika FROM Zawodnik WHERE idDruzyny = druzynyId AND numerZawodnika = zawodnikaNumer), 
		(SELECT punktyDruzynyPierwszej FROM mecz WHERE idMeczu = meczuId), (SELECT punktyDruzynyDrugiej FROM mecz WHERE idMeczu = meczuId));
COMMIT;
END //

DROP TRIGGER IF EXISTS sprawdzWiek;
DELIMITER //
CREATE TRIGGER sprawdzWiek
BEFORE INSERT ON zawodnik 
FOR EACH ROW
BEGIN
	IF YEAR(CURRENT_DATE) - NEW.rokUrodzenia < 18 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid date!';
	END IF;
END //

DROP TRIGGER IF EXISTS sprawdzDywizje;
DELIMITER //
CREATE TRIGGER sprawdzDywizje
BEFORE INSERT ON zawodnik
FOR EACH ROW
BEGIN
	IF NEW.plec = "Kobieta" AND (SELECT dywizja FROM druzyna WHERE idDruzyny = NEW.idDruzyny) = "Open" THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Baba nie moze grac w open!';
	END IF;
	IF NEW.plec = "Mężczyzna" AND (SELECT dywizja FROM druzyna WHERE idDruzyny = NEW.idDruzyny) = "Women" THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Wrong team for this player!';
	END IF;
END //

DROP TRIGGER IF EXISTS sprawdzNumer;
DELIMITER //
CREATE TRIGGER sprawdzNumer
BEFORE INSERT ON zawodnik
FOR EACH ROW
BEGIN
	IF EXISTS (SELECT numerZawodnika FROM zawodnik WHERE idDruzyny = NEW.idDruzyny AND numerZawodnika = NEW.numerZawodnika) THEN
				SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Player with this number already exists!';
                END IF;
END //

DELIMITER //
CREATE PROCEDURE zakonczMecz (meczuId int)
BEGIN
DECLARE idPierwszej INT DEFAULT (SELECT idDruzynyPierwszej FROM mecz WHERE idMeczu = meczuId);
DECLARE idDrugiej INT DEFAULT (SELECT idDruzynyDrugiej FROM mecz WHERE idMeczu = meczuId);
DECLARE punktyPierwszej INT DEFAULT (SELECT punktyDruzynyPierwszej FROM mecz WHERE idDruzynyPierwszej = idPierwszej);
DECLARE punktyDrugiej INT DEFAULT (SELECT punktyDruzynyDrugiej FROM mecz WHERE idDruzynyDrugiej = idDrugiej);
	UPDATE mecz SET czyZakonczony = true WHERE idMeczu = meczuId;
    UPDATE druzyna SET zdobytePunkty = zdobytePunkty + punktyPierwszej WHERE idDruzyny = idPierwszej;
    UPDATE druzyna SET zdobytePunkty = zdobytePunkty + punktyDrugiej WHERE idDruzyny = idDrugiej;
    IF punktyPierwszej > punktyDrugiej THEN
		UPDATE druzyna SET zwyciestwa = zwyciestwa + 1 WHERE idDruzyny = idPierwszej;
        UPDATE druzyna SET porazki = porazki + 1 WHERE idDruzyny = idDrugiej;
    ELSEIF punktyPierwszej < punktyDrugiej THEN 
		UPDATE druzyna SET zwyciestwa = zwyciestwa + 1 WHERE idDruzyny = idDrugiej;
        UPDATE druzyna SET porazki = porazki + 1 WHERE idDruzyny = idPierwszej;
	ELSE
		UPDATE druzyna SET remisy = remisy + 1 WHERE idDruzyny = idDrugiej;
        UPDATE druzyna SET remisy = remisy + 1 WHERE idDruzyny = idPierwszej;
    END IF;
END //

DROP TRIGGER IF EXISTS usunDaneMeczu;
DELIMITER //
CREATE TRIGGER usunDaneMeczu
BEFORE DELETE ON mecz
FOR EACH ROW
BEGIN
	UPDATE druzyna SET zdobytePunkty = zdobytePunkty - OLD.punktyDruzynyPierwszej WHERE idDruzyny = OLD.idDruzynyPierwszej;
    UPDATE druzyna SET zdobytePunkty = zdobytePunkty - OLD.punktyDruzynyDrugiej WHERE idDruzyny = OLD.idDruzynyDrugiej;
    IF OLD.punktyDruzynyPierwszej > OLD.punktyDruzynyDrugiej THEN
		UPDATE druzyna SET zwyciestwa = zwyciestwa - 1 WHERE idDruzyny = OLD.idDruzynyPierwszej;
        UPDATE druzyna SET porazki = porazki - 1 WHERE idDruzyny = OLD.idDruzynyDrugiej;
    ELSEIF OLD.punktyDruzynyPierwszej < OLD.punktyDruzynyDrugiej THEN 
		UPDATE druzyna SET zwyciestwa = zwyciestwa - 1 WHERE idDruzyny = OLD.idDruzynyDrugiej;
        UPDATE druzyna SET porazki = porazki - 1 WHERE idDruzyny = OLD.idDruzynyPierwszej;
	ELSE
		UPDATE druzyna SET remisy = remisy - 1 WHERE idDruzyny = OLD.idDruzynyDrugiej;
        UPDATE druzyna SET remisy = remisy - 1 WHERE idDruzyny = OLD.idDruzynyPierwszej;
    END IF;
	DELETE FROM punktacjameczu WHERE idMeczu = OLD.idMeczu;
END //

DROP TRIGGER IF EXISTS poUsunieciuPunkta;
DELIMITER //
CREATE TRIGGER poUsunieciuPunkta
BEFORE DELETE ON punktacjaMeczu
FOR EACH ROW
BEGIN
DECLARE idPierwszej INT DEFAULT (SELECT idDruzynyPierwszej FROM mecz WHERE idMeczu = OLD.idMeczu);
DECLARE idDrugiej INT DEFAULT (SELECT idDruzynyDrugiej FROM mecz WHERE idMeczu = OLD.idMeczu);
DECLARE druzynyId INT DEFAULT (SELECT idDruzyny FROM zawodnik WHERE idZawodnika = OLD.idZawodnika);
	UPDATE zawodnik SET zdobytePunkty = zdobytePunkty - 1 WHERE idZawodnika = OLD.idZawodnika;
    IF idPierwszej = druzynyId THEN
		UPDATE mecz SET punktyDruzynyPierwszej = punktyDruzynyPierwszej - 1 WHERE idMeczu = OLD.idMeczu;
    ELSEIF idDrugiej = druzynyId THEN
		UPDATE mecz SET punktyDruzynyDrugiej = punktyDruzynyDrugiej - 1 WHERE idMeczu = OLD.idMeczu;
    END IF;
END //

INSERT INTO Druzyna(idDruzyny, nazwaDruzyny, miasto, rokZalozenia, dywizja, idUzytkownikaKapitana) VALUES (0, "Usunięta druzyna", "miasto", 0, "Mixed", 1);
INSERT INTO ZAWODNIK (idZawodnika, idDruzyny, imie, nazwisko, plec, rokUrodzenia, numerZawodnika) values (0, 1, "Usunięty zawodnik", "nazwisko", "Kobieta", 0, 0);

DROP TRIGGER IF EXISTS usuwanieDruzyny;
DELIMITER //
CREATE TRIGGER usuwanieDruzyny
BEFORE delete ON druzyna
FOR EACH ROW
BEGIN
	DELETE from zawodnik WHERE idDruzyny = OLD.idDruzyny;
    UPDATE mecz SET idDruzynyPierwszej = 1 WHERE idDruzynyPierwszej = OLD.idDruzyny;
	UPDATE mecz SET idDruzynyDrugiej = 1 WHERE idDruzynyDrugiej = OLD.idDruzyny;   
END //

DROP TRIGGER IF EXISTS usuwanieZawodnika;
DELIMITER //
CREATE TRIGGER usuwanieZawodnika
BEFORE delete ON zawodnik
FOR EACH ROW
BEGIN
	UPDATE punktacjaMeczu SET idZawodnika = 1 WHERE idZawodnika = OLD.idZawodnika;
END //

INSERT INTO Mecz (idDruzynyPierwszej, idDruzynyDrugiej, punktyDruzynyPierwszej, punktyDruzynyDrugiej, idTurnieju, czyZakonczony) VALUES (1, 2, 0, 0, 1, false);
INSERT INTO Turniej (nazwaTurnieju, dataTurnieju, miejsce, dywizjaTurnieju, idOrganizatora) VALUES ("Poligon", "2020-10-10", "Zabrze", "Mixed", 1);
INSERT INTO Turniej (nazwaTurnieju, dataTurnieju, miejsce, dywizjaTurnieju, idOrganizatora) VALUES ("Windmill", "2018-07-19", "Amsterdam", "Mixed", 1);
INSERT INTO Turniej (nazwaTurnieju, dataTurnieju, miejsce, dywizjaTurnieju, idOrganizatora) VALUES ("Mistrzostwa Polski", "2010-01-10", "Warszawa", "Women", 1);
INSERT INTO Turniej (nazwaTurnieju, dataTurnieju, miejsce, dywizjaTurnieju, idOrganizatora) VALUES ("WLU", "2020-05-09", "Warszawa", "Open", 1);
INSERT INTO ZAWODNIK (idDruzyny, imie, nazwisko, plec, rokUrodzenia, numerZawodnika) values (2, "Wiktoria", "Pyra", "Kobieta", 2001, 51);
INSERT INTO ZAWODNIK (idDruzyny, imie, nazwisko, plec, rokUrodzenia, numerZawodnika) values (2, "Klaudia", "Wrońska", "Kobieta", 1998, 7);
INSERT INTO ZAWODNIK (idDruzyny, imie, nazwisko, plec, rokUrodzenia, numerZawodnika) values (3, "Maciej", "Adamski", "Mężczyzna", 1998, 3);
INSERT INTO ZAWODNIK (idDruzyny, imie, nazwisko, plec, rokUrodzenia, numerZawodnika) values (3, "Natalia", "Nędzi", "Kobieta", 2001, 13);
INSERT INTO ZAWODNIK (idDruzyny, imie, nazwisko, plec, rokUrodzenia, numerZawodnika) values (2, "Paulina", "Dul", "Kobieta", 1995, 1);