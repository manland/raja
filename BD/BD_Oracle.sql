-- ORACLE: CREATION DES TABLES pour la base VirusTransmission --


-- SUPPRESSION DES TABLES
DROP TABLE VIRUS_TRANSMISSION;
DROP TABLE TRANSMISSION;
DROP TABLE VIRUS; 


-- Info
-- V comme VirusTransmission
-- T comme Transmission
-- Donc prefix -> VT
CREATE TABLE TRANSMISSION(
	VT_MODET VARCHAR(20) primary key	
);

-- Info
-- V comme VirusTransmission
-- V comme Virus
-- Donc prefix -> VV
CREATE TABLE VIRUS(
	VV_ABREV VARCHAR(10) primary key,
	VV_NOMV VARCHAR(30),
	VV_TYPEGENOME VARCHAR(5),
	VV_NOMFA VARCHAR(20)
);

-- Info
-- V comme VirusTransmission
-- VT comme Virus_Transmission
-- Donc prefix -> VVT
CREATE TABLE VIRUS_TRANSMISSION(
	VVT_MODET VARCHAR(20) REFERENCES TRANSMISSION(VT_MODET),
	VVT_ABREVV VARCHAR(10) REFERENCES VIRUS(VV_ABREV)
);



