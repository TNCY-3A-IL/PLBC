package pharmgkb.triplet;

public class DrugIdToNameTriplet extends Triplet {

	public DrugIdToNameTriplet(String subject, String object) {
		super("dr:" + subject, "rdfs:hasLabel", "\"" + object + "\"");
	}

}
