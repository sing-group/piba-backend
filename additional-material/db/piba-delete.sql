SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `exploration`;
DELETE FROM `gallery`;
DELETE FROM `idspace`;
DELETE FROM `image`;
DELETE FROM `modifier`;
DELETE FROM `password_recovery`;
DELETE FROM `patient`;
DELETE FROM `polyp`;
DELETE FROM `polypdataset`;
DELETE FROM `polyphistology`;
DELETE FROM `polypsindataset`;
DELETE FROM `polyplocation`;
DELETE FROM `polyprecording`;
DELETE FROM `reviewedpolyprecording`;
DELETE FROM `user`;
DELETE FROM `video`;
DELETE FROM `videomodification`;

SET FOREIGN_KEY_CHECKS = 1;
