package models;

import javax.persistence.Column;
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
	
	// TODO: mozna i vytvorit neco jako user-defined group?
	public static final String TAG_GROUP_EMPTY = "";
	
	@Column(name="TagGroup")
	public String group;
	public String name;
	// TODO: asi by slo nahradit odkazem na systemoveho uzivatele?
	public boolean systemGenerated;
	
	@ManyToOne
	public LocalUser user;
	
	public static Tag getInstance(LocalUser user, String group, String name, boolean systemGenerated) {
		Tag tag = Tag.find("user = ? and group = ? and name = ?", user, group, name).first();
		if (tag == null) {
			tag = new Tag(user, group, name, systemGenerated);
			tag.save();
		}
		return tag;
	}
	
	Tag(LocalUser user, String group, String name, boolean systemGenerated) {
		assert user != null;
		assert name != null;
		
		this.user = user;
		this.group = group == null ? TAG_GROUP_EMPTY : group;
		this.name = name;
		this.systemGenerated = systemGenerated;
	}
	
	@Override
	public boolean equals(Object other) {
		assert user != null;
		assert group != null;
		assert name != null;
		
		if (other == this)
			return true;
		if (!(other instanceof Tag))
			return false;
		Tag otherTag = (Tag)other;
		return user.equals(otherTag.user) && group.equals(otherTag.group) && name.equals(otherTag.name); 
	}
	
	@Override
	public int hashCode() {
		return user.hashCode() + group.hashCode() + name.hashCode();
	}

	@Override
	public String toString() {
		if (group.equals(TAG_GROUP_EMPTY))
			return name;
		else
			return group + "/" + name;
	}
	
}
