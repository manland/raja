-- POSTGRE: CREATION DES TUPLES pour la base RecensementMaladie --

-- Table RECENSEMENT
INSERT INTO RECENSEMENT VALUES (1, 1, 2006);
INSERT INTO RECENSEMENT VALUES (2, 1, 1998);
INSERT INTO RECENSEMENT VALUES (3, 1, 2007);
INSERT INTO RECENSEMENT VALUES (4, 1, 2008);
INSERT INTO RECENSEMENT VALUES (5, 1, 2000);
INSERT INTO RECENSEMENT VALUES (6, 1, 2002);
INSERT INTO RECENSEMENT VALUES (7, 1, 1999);;
INSERT INTO RECENSEMENT VALUES (8, 1, 2004);
INSERT INTO RECENSEMENT VALUES (9,  1, 2001);
INSERT INTO RECENSEMENT VALUES (10,  1, 2005);
 
-- Table ZONE_OMS
INSERT INTO ZONE_OMS VALUES ('Europe', 733000000);
INSERT INTO ZONE_OMS VALUES ('East Europe', 3902404192);

-- Table PAYS
INSERT INTO PAYS VALUES ('Europe', 'France', 68000000);
INSERT INTO PAYS VALUES ('Europe', 'Italie', 61000000);
INSERT INTO PAYS VALUES ('Europe', 'Allemagne', 81000000);
INSERT INTO PAYS VALUES ('Europe', 'Espagne', 47023594);
INSERT INTO PAYS VALUES ('Europe', 'Grece', 12034984);
INSERT INTO PAYS VALUES ('East Europe', 'Albanie', 3175068);
INSERT INTO PAYS VALUES ('East Europe', 'Croatie', 4481643);
INSERT INTO PAYS VALUES ('East Europe', 'Hongrie', 9930456);
INSERT INTO PAYS VALUES ('East Europe', 'Estonie', 1307598);
INSERT INTO PAYS VALUES ('East Europe', 'Roumanie', 22246862);

-- Table MALADIE
INSERT INTO MALADIE VALUES ('Dengue', 3157, 8, 'France');
INSERT INTO MALADIE VALUES ('Dengue', 6, 8, 'Hongrie');
INSERT INTO MALADIE VALUES ('Hepatitis A', 1217, 10, 'Allemagne');
INSERT INTO MALADIE VALUES ('Hepatitis A', 21944, 5, 'Roumanie');
INSERT INTO MALADIE VALUES ('Rubella', 16, 7, 'Croatie');
INSERT INTO MALADIE VALUES ('Rubella', 5, 1, 'Estonie');
INSERT INTO MALADIE VALUES ('Measles ', 297, 4, 'Espagne');
INSERT INTO MALADIE VALUES ('Measles ', 1901, 2, 'Albanie');
INSERT INTO MALADIE VALUES ('Legionellosis', 936, 3, 'Italie');
INSERT INTO MALADIE VALUES ('Legionellosis', 12, -, 'Grece');




