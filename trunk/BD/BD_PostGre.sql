-- POSTGRE: CREATION DES TABLES pour la base RecensementMaladie --


-- SUPPRESSION DES TABLES
DROP TABLE RECENSEMENT, ZONE_OMS, PAYS, MALADIE;


-- Info
-- R comme Recensement
-- R comme RecencementMaladie
-- Donc prefix -> RR
CREATE TABLE RECENSEMENT(
    RR_ID       CHAR(3) CONSTRAINT PRIMARY KEY,
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
    RP_LIBELLE_ZO	 VARCHAR(40) CONSTRAINT FOREIGN KEY fk_rp_libelle_zo REFERENCES ZONE_OMS(RZ_LIBELLE),
    RP_LIBELLE       VARCHAR(40) CONSTRAINT pk_rp_libelle PRIMARY KEY,
	RP_POPULATION	 CHAR(10)
);

-- Info
-- R comme Recensement
-- M comme Maladie
-- Donc prefix -> RM
CREATE TABLE MALADIE(
    RM_LIBELLE       	VARCHAR(40) CONSTRAINT PRIMARY KEY,
	RM_POPULATION		CHAR(10),
	RM_ID_RECENSEMENT	CHAR(3) CONSTRAINT FOREIGN KEY fk_rm_id_recensement REFERENCES RECENSEMENT(RR_ID),
	RM_LIBELLE_PAYS		VARCHAR(40) CONSTRAINT fk_rm_libelle_pays REFERENCES PAYS(RP_LIBELLE)
);