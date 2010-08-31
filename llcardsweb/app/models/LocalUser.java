package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class LocalUser extends Model {
	
	public String openId;
	public String nickname;
	public String email;
	
	public LocalUser(String openId, String nickname, String email) {
		assert openId != null;
		
		this.openId = openId;
		this.nickname = nickname;
		this.email = email;
	}
	
	@Override
	public boolean equals(Object other) {
		assert openId != null; 
		
		if (other == this)
			return true;
		if (!(other instanceof LocalUser))
			return false;
		LocalUser otherUser = (LocalUser)other;
		return openId.equals(otherUser); 
	}

}
