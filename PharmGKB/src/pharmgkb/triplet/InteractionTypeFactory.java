package pharmgkb.triplet;

public class InteractionTypeFactory {

	public static InteractionType getInstance(String interactionTypeStr) {
		InteractionType interactionType = InteractionType.UNKONW;
		
		// JRE6
		if (interactionTypeStr.equals("Toxicity/ADR")) {
			interactionType = InteractionType.INCREASES_TOXICITY_OF;
		}
		else if (interactionTypeStr.equals("Efficacy")) {
			interactionType = InteractionType.INCREASES_EFFICACY_OF;
		}
		else if (interactionTypeStr.equals("Dosage")) {
			interactionType = InteractionType.AFFECTS_DOSAGE_OF;
		}
		
		return interactionType;
	}
}
