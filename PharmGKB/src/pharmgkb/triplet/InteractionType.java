package pharmgkb.triplet;

public enum InteractionType {
	
	INCREASES_EFFICACY_OF {
		@Override
		public String toString() {
			return "increasesEfficacyOf";
		};
	},
	
	INCREASES_TOXICITY_OF {
		@Override
		public String toString() {
			return "increasesToxicityOf";
		};
	},
	
	AFFECTS_DOSAGE_OF {
		@Override
		public String toString() {
			return "affectsDosageOf";
		};
	},
	
	UNKONW {
		@Override
		public String toString() {
			return "unknow";
		};
	};
	
	public abstract String toString();
}
