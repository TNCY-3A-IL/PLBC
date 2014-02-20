package pharmgkb.triplet;

public class DrugGeneInteractionTripet extends Triplet {

	public DrugGeneInteractionTripet(String subject, InteractionType interactionType, String object) {
		super(subject, "pgkb:" + interactionType.toString(), object);
	}

}
