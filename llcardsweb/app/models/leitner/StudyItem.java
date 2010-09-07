package models.leitner;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.Card;

import play.db.jpa.Model;

@Entity
public class StudyItem extends Model {
	
	@ManyToOne
	public StudyPlan plan;
	
	@ManyToOne
	public Card card;
	
	public StudyItemState state;

}
