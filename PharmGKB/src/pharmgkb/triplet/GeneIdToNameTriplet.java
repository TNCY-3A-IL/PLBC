package pharmgkb.triplet;

public class GeneIdToNameTriplet extends Triplet {

	public GeneIdToNameTriplet(String subject, String object) {
		super(subject, "rdfs:hasLabel", object);
	}

}
