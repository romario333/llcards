package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * Tag, which can be associated with card. Tag is entered either by
 * user or system - {@link #systemGenerated}.
 * 
 * When comparing two tags, it is not important, who created the tag.
 * 
 * @author romario
 *
 */
@Entity
public class Tag extends Model {
	public String name;
	// TODO: asi by slo nahradit odkazem na systemoveho uzivatele?
	public boolean systemGenerated;
	
	@ManyToOne
	public LocalUser user;
	
	/**
	 * Returns tag for the specified user and name. If tag does not exists,
	 * new tag is inserted into the database.  
	 * 
	 * @param user
	 * @param name
	 * @param systemGenerated True if tag is being created by system, false if by user. 
	 * @return
	 */
	public static Tag getInstance(LocalUser user, String name, boolean systemGenerated) {
		Tag tag = Tag.find("user = ? and name = ?", user, name).first();
		if (tag == null) {
			tag = new Tag(user, name, systemGenerated);
			tag.save();
		}
		return tag;
	}
	
	Tag(LocalUser user, String name, boolean systemGenerated) {
		assert user != null;
		assert name != null;
		
		this.user = user;
		this.name = name;
		this.systemGenerated = systemGenerated;
	}
	
	@Override
	public boolean equals(Object other) {
		assert user != null;
		assert name != null;
		
		if (other == this)
			return true;
		if (!(other instanceof Tag))
			return false;
		Tag otherTag = (Tag)other;
		return user.equals(otherTag.user) && name.equals(otherTag.name); 
	}
	
	@Override
	public int hashCode() {
		return user.hashCode() + name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}
	
}
