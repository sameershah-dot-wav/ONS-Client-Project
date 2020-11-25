CREATE DATABASE IF NOT EXISTS ONS;
USE ONS;

CREATE TABLE IF NOT EXISTS USER (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  firstName VARCHAR(255) NOT NULL,
  lastName VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT email_unique UNIQUE (email))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS ROLE(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT role_unique UNIQUE (role))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS USER_ROLE(
    userId INT UNSIGNED NOT NULL,
    roleId INT UNSIGNED NOT NULL,
    FOREIGN KEY (userId) REFERENCES USER(id),
    FOREIGN KEY (roleId) REFERENCES ROLE(id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS CHECKLIST_TEMPLATE(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    userId INT UNSIGNED NOT NULL, -- this will contain the id of the author who create the list
    listName VARCHAR(255) NOT NULL,
    description LONGTEXT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (userId) REFERENCES USER(id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS TOPIC(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    checklistTemplateId INT UNSIGNED NOT NULL,
    topicName VARCHAR(255) NOT NULL,
    description LONGTEXT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (checklistTemplateId) REFERENCES CHECKLIST_TEMPLATE(id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS CHECKLIST_TEMPLATE_ITEM(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    topicId INT UNSIGNED NOT NULL,
    description LONGTEXT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (topicId) REFERENCES TOPIC(id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS PERSONAL_CHECKLIST(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    userId INT UNSIGNED NOT NULL,
    checklsitTemplateId INT UNSIGNED NOT NULL,
    dateAssigned DATE NOT NULL,
    dateComplete DATE NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (userId) REFERENCES USERS(id),
    FOREIGN KEY (checklsitTemplateId) REFERENCES CHECKLIST_TEMPLATE(id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS CHECKLIST_ITEM(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    personalChecklistId INT UNSIGNED NOT NULL,
    checklsitTemplateItemId INT UNSIGNED NOT NULL,
    checked BOOLEAN NOT NULL, -- boolean will show as TINYINT(1)
    dateChecked DATE NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (personalChecklistId) REFERENCES PERSONAL_CHECKLIST(id),
    FOREIGN KEY (checklsitTemplateItemId) REFERENCES CHECKLIST_TEMPLATE_ITEM(id))
ENGINE = InnoDB;
