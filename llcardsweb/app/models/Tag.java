package models;

import javax.persistence.Entity;

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
	public boolean systemGenerated;
	
	public Tag(String name, boolean systemGenerated) {
		assert name != null;
		
		this.name = name;
		this.systemGenerated = systemGenerated;
	}
	
	@Override
	public boolean equals(Object other) {
		assert name != null;
		
		if (other == this)
			return true;
		if (!(other instanceof Tag))
			return false;
		Tag tag = (Tag)other;
		return name.equals(tag.name);
	}
	
	@Override
	public int hashCode() {
		return name == null ? 0 : name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}
	
}
