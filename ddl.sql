CREATE TABLE GRUPOS (
                        grupoid INT NOT NULL AUTO_INCREMENT,
                        grupodesc VARCHAR(100) NOT NULL,
                        mejorposCTFid INT,
                        PRIMARY KEY (grupoid)
);

CREATE TABLE CTFS (
                      CTFid INT NOT NULL,
                      grupoid INT NOT NULL,
                      puntuacion INT NOT NULL,
                      PRIMARY KEY (CTFid,grupoid)
);

ALTER TABLE GRUPOS
    ADD FOREIGN KEY (mejorposCTFid, grupoid)
        REFERENCES CTFS(CTFid,grupoid);

insert into grupos(grupoid, grupodesc) values(1, '1DAM-G1');
insert into grupos(grupoid, grupodesc) values(2, '1DAM-G2');
insert into grupos(grupoid, grupodesc) values(3, '1DAM-G3');
insert into grupos(grupoid, grupodesc) values(4, '1DAW-G1');
insert into grupos(grupoid, grupodesc) values(5, '1DAW-G2');
insert into grupos(grupoid, grupodesc) values(6, '1DAW-G3');