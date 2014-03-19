package pharmgkb.triplet;

public class DrugGeneInteractionTripet extends Triplet {

	public DrugGeneInteractionTripet(String subject, InteractionType interactionType, String object) {
		super("ge:" + subject, "pgkb:" + interactionType.toString(), "dr:" + object);
	}

}
