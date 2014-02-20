package pharmgkb.triplet;

public class DrugIdToNameTriplet extends Triplet {

	public DrugIdToNameTriplet(String subject, String object) {
		super(subject, "rdfs:hasLabel", object);
	}

}
