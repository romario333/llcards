package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class WordFrequency extends Model {
	
	public String word;
	public int wordRank;
	public int wordCount;

	@ManyToOne
	public Language language;

}
