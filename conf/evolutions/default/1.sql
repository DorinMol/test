# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

 !Ups

create table "inventories" ("id_player" INTEGER NOT NULL,"id_item" INTEGER NOT NULL);
create table "items" ("id_item" SERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"damage" INTEGER DEFAULT 0,"armor" INTEGER DEFAULT 0);
create table "players" ("id_player" SERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"level" INTEGER NOT NULL);
create table "user_type_templates" ("id_template" SERIAL NOT NULL,"id_user_type" INTEGER,"email_template" lo);
create trigger "user_type_templates__""email_template""_lob" before update or delete on "user_type_templates" for each row execute procedure lo_manage("email_template");
alter table "inventories" add constraint "inventory_idItem" foreign key("id_item") references "items"("id_item") on update NO ACTION on delete NO ACTION;
alter table "inventories" add constraint "inventory_idPlayer" foreign key("id_player") references "players"("id_player") on update NO ACTION on delete NO ACTION;

!Downs

delete from "user_type_templates";
drop trigger "user_type_templates__""email_template""_lob" on "user_type_templates";
alter table "inventories" drop constraint "inventory_idItem";
alter table "inventories" drop constraint "inventory_idPlayer";
drop table "user_type_templates";
drop table "players";
drop table "items";
drop table "inventories";

