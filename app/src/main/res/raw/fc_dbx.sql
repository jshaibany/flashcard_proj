BEGIN TRANSACTION;
DROP TABLE IF EXISTS "FLASHCARDS";
CREATE TABLE IF NOT EXISTS "FLASHCARDS" (
	"_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"class_name"	TEXT,
	"section_name"	TEXT,
	"deckset_name"	TEXT,
	"term"	TEXT,
	"definition"	TEXT,
	"importance"	INTEGER,
	"hardness"	INTEGER,
	"review_hits"	INTEGER DEFAULT 0,
	"quiz_hits"	INTEGER DEFAULT 0,
	"created_on"	TEXT,
	"updated_on"	TEXT,
	"last_review"	TEXT,
	"last_quiz"	TEXT
);
DROP TABLE IF EXISTS "DECKSETS";
CREATE TABLE IF NOT EXISTS "DECKSETS" (
	"_id"	INTEGER,
	"deckset_name"	TEXT,
	"section_name"	TEXT,
	"class_name"	TEXT,
	"created_on"	TEXT,
	"updated_on"	TEXT,
	PRIMARY KEY("deckset_name","section_name","class_name")
);
DROP TABLE IF EXISTS "SECTIONS";
CREATE TABLE IF NOT EXISTS "SECTIONS" (
	"_id"	INTEGER,
	"section_name"	TEXT,
	"class_name"	TEXT,
	"created_on"	TEXT,
	"updated_on"	TEXT,
	PRIMARY KEY("section_name","class_name")
);
DROP TABLE IF EXISTS "CLASSES";
CREATE TABLE IF NOT EXISTS "CLASSES" (
	"_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT UNIQUE,
	"created_on"	TEXT,
	"updated_on"	TEXT
);
DROP TABLE IF EXISTS "android_metadata";
CREATE TABLE IF NOT EXISTS "android_metadata" (
	"locale"	TEXT DEFAULT 'en_US'
);
COMMIT;
