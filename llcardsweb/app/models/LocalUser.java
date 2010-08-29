package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class LocalUser extends Model {
	
	public String openId;
	public String nickname;
	public String email;
	
	// TODO: potrebuji ctor?
	public LocalUser(String openId, String nickname, String email) {
		this.openId = openId;
		this.nickname = nickname;
		this.email = email;
	}

}
