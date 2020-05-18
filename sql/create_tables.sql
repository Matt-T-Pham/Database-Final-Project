CREATE TABLE UU (
  login char(30) NOT NULL UNIQUE,
  password char(12) NOT NULL,
  name varchar(40),
  address varchar(50) NOT NULL,
  phone char(10),
  PRIMARY KEY (login)
);

CREATE TABLE UD (
  login char(30) NOT NULL UNIQUE,
  PRIMARY KEY (login),
  FOREIGN KEY (login) REFERENCES UU(login)
);

CREATE TABLE Ctypes (
  tid int NOT NULL AUTO_INCREMENT,
  make char(50),
  model char(50),
  PRIMARY KEY (tid),
  UNIQUE KEY typekey (make, model),
  INDEX type (make, model)
);

CREATE TABLE UC (
  vin int NOT NULL UNIQUE,
  category varchar(50),
  tid int NOT NULL,
  login char(30) NOT NULL,
  PRIMARY KEY (vin),
  FOREIGN KEY (login) REFERENCES UD(login),
  FOREIGN KEY (tid) REFERENCES Ctypes(tid)
);

CREATE TABLE Period (
  pid int NOT NULL AUTO_INCREMENT,
  fromHour int NOT NULL,
  toHour int NOT NULL,
  PRIMARY KEY (pid)
);

CREATE TABLE Available (
  login char(30) NOT NULL,
  pid int NOT NULL,
  PRIMARY KEY (login, pid),
  FOREIGN KEY (login) REFERENCES UD(login),
  FOREIGN KEY (pid) REFERENCES Period(pid)
);

CREATE TABLE Reserve (
  login char(30) NOT NULL,
  vin int NOT NULL,
  pid int NOT NULL,
  PRIMARY KEY (login, vin, pid),
  FOREIGN KEY (login) REFERENCES UU(login),
  FOREIGN KEY (vin) REFERENCES UC(vin),
  FOREIGN KEY (pid) REFERENCES Period(pid)
);

CREATE TABLE Ride (
  rid int NOT NULL AUTO_INCREMENT,
  cost int NOT NULL,
  rdate DATETIME NOT NULL,
  login char(30) NOT NULL,
  vin int NOT NULL,
  PRIMARY KEY (rid),
  FOREIGN KEY (login) REFERENCES UU(login),
  FOREIGN KEY (vin) REFERENCES UC(vin)
);

CREATE TABLE Trust (
  truster char(30) NOT NULL,
  trustee char(30) NOT NULL,
  isTrusted boolean NOT NULL,
  PRIMARY KEY (truster, trustee),
  FOREIGN KEY (truster) REFERENCES UU(login),
  FOREIGN KEY (trustee) REFERENCES UU(login)
);

CREATE TABLE Feedback (
  fid int NOT NULL AUTO_INCREMENT,
  fbtext char(100),
  fbdate DATETIME,
  rating int,
  vin int NOT NULL,
  login char(30) NOT NULL,
  PRIMARY KEY (fid),
  FOREIGN KEY (vin) REFERENCES UC(vin),
  FOREIGN KEY (login) REFERENCES UU(login),
  UNIQUE KEY user_car (login, vin)
);

CREATE TABLE Rates (
  login char(30) NOT NULL,
  fid int NOT NULL,
  rating int,
  PRIMARY KEY (login, fid),
  FOREIGN KEY (login) REFERENCES UU(login),
  FOREIGN KEY (fid) REFERENCES Feedback(fid)
);

CREATE TABLE Favorites (
  vin int NOT NULL,
  login char(30) NOT NULL,
  PRIMARY KEY (vin, login),
  FOREIGN KEY (vin) REFERENCES UC(vin),
  FOREIGN KEY (login) REFERENCES UU(login)
);



DELIMITER $$

    CREATE TRIGGER DriverIsAvailable 
    BEFORE INSERT ON Ride
    FOR EACH ROW 
    BEGIN
      DECLARE done INT DEFAULT FALSE;
      DECLARE fromHour int;
      DECLARE toHour int;
      DECLARE cur CURSOR FOR (SELECT p.fromHour, p.toHour FROM Available a, Period p, UC uc WHERE a.pid = p.pid AND uc.vin = NEW.vin AND a.login = uc.login);
      DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

      OPEN cur;
      
      read_loop: LOOP
        FETCH cur INTO fromHour, toHour;
        IF done THEN 
          LEAVE read_loop;
        END IF;
        IF (HOUR(NEW.rdate) < fromHour OR HOUR(NEW.rdate) > toHour) THEN
            SIGNAL SQLSTATE '45000';
        END IF;
      END LOOP;
      CLOSE cur;

      IF (fromHour IS NULL) THEN
        SIGNAL SQLSTATE '45000';
      END IF;

    END$$

DELIMITER ;

DELIMITER $$

    CREATE TRIGGER NoSelfFeedbackRate
    BEFORE INSERT ON Rates
    FOR EACH ROW 
    BEGIN
      SET @login = (SELECT login FROM Feedback WHERE fid = NEW.fid);

      IF (@login = NEW.login) THEN
        SIGNAL SQLSTATE '45000';
      END IF;
    END$$

DELIMITER ;