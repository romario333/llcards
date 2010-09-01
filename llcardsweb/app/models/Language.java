package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Language extends Model {
	
	public String code;
	public String name;

}
