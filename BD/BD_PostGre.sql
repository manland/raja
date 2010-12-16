-- POSTGRE: CREATION DES TABLES pour la base RecensementMaladie --


-- SUPPRESSION DES TABLES
DROP TABLE RECENSEMENT, ZONE_OMS, PAYS, MALADIE;


-- Info
-- R comme Recensement
-- R comme RecencementMaladie
-- Donc prefix -> RR
CREATE SEQUENCE RECENSEMENT_ID_SEQ START 1;
CREATE TABLE RECENSEMENT(
    RR_ID       INTEGER PRIMARY KEY DEFAULT NEXTVAL('RECENSEMENT_ID_SEQ'),
    RR_NBRE		CHAR(3),
    RR_ANNEE	CHAR(4)
);

-- Info
-- R comme Recensement
-- Z comme Zone_Oms
-- Donc prefix -> RZ
CREATE TABLE ZONE_OMS(
    RZ_LIBELLE		VARCHAR(40) CONSTRAINT PRIMARY KEY,
    RZ_POPULATION   CHAR(10)
);

-- Info
-- R comme Recensement
-- P comme Pays
-- Donc prefix -> RP
CREATE TABLE PAYS(
    RP_LIBELLE_ZO	 VARCHAR(40) CONSTRAINT FOREIGN KEY FK_RP_LIBELLE_ZO REFERENCES ZONE_OMS(RZ_LIBELLE),
    RP_LIBELLE       VARCHAR(40) CONSTRAINT PK_RP_LIBELLE PRIMARY KEY,
	RP_POPULATION	 CHAR(10)
);

-- Info
-- R comme Recensement
-- M comme Maladie
-- Donc prefix -> RM
CREATE SEQUENCE MALADIE_ID_SEQ START 1;
CREATE TABLE MALADIE(
	RM_ID       		INTEGER PRIMARY KEY DEFAULT NEXTVAL('MALADIE_ID_SEQ'),
    RM_LIBELLE       	VARCHAR(40),
	RM_POPULATION		CHAR(10),
	RM_ID_RECENSEMENT	CHAR(3) CONSTRAINT FOREIGN KEY FK_RM_ID_RECENSEMENT REFERENCES RECENSEMENT(RR_ID),
	RM_LIBELLE_PAYS		VARCHAR(40) CONSTRAINT FK_RM_LIBELLE_PAYS REFERENCES PAYS(RP_LIBELLE)
);