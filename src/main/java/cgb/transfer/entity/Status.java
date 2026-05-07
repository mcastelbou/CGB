package cgb.transfer.entity;

public enum Status {
	SUCCESS("success"),
	DELAYED("delayed"),
	FAILURE("failure"),
	CANCELED("canceled"),
	CLOSED("closed");
	
	private final String name;
	
	Status(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
