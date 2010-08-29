package models;

public enum CardItemType {
	DEFINITION("Definition"),
	PRONUNCIATION("Pronunciation"),
	EXAMPLES("Examples"),
	ADDITIONAL_INFO("Add. Info");
	
	private final String name;
	
	private CardItemType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
