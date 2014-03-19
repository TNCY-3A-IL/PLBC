package pharmgkb.triplet;

public class GeneIdToNameTriplet extends Triplet {

	public GeneIdToNameTriplet(String subject, String object) {
		super("ge:" + subject, "rdfs:hasLabel", "\"" + object + "\"");
	}

}
