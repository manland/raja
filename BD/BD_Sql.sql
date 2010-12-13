-- SQL: CREATION DES TABLES pour la base MaladieFacteur --


-- SUPPRESSION DES TABLES
DROP TABLE APRORIGINE;
DROP TABLE FACTEUR;
DROP TABLE MALADIE; 


-- Info
-- M comme MaladieFacteur
-- M comme Maladie
-- Donc prefix -> MM
CREATE TABLE MALADIE(
	MM_NOM_MALADIE VARCHAR(25) PRIMARY KEY	
);

-- Info
-- M comme MaladieFacteur
-- F comme Facteur
-- Donc prefix -> MF
CREATE TABLE FACTEUR(
	MF_ABREV_FACTEUR VARCHAR(20) PRIMARY KEY,
	MF_NOM_FACTEUR VARCHAR(30),
	MF_TYPE_FACTEUR VARCHAR(10)
);

-- Info
-- M comme MaladieFacteur
-- A comme APrOrigine
-- Donc prefix -> MA
CREATE TABLE APRORIGINE(
	MA_NOMM VARCHAR(25) REFERENCES MALADIE(MM_NOM_MALADIE),
	MA_ABREVF VARCHAR(20) REFERENCES FACTEUR(MF_ABREV_FACTEUR),
	MA_SOUCHE VARCHAR(10)
);


