
public class RepoCompare {

	//Comparing String Tags
	boolean compareTag(Sheet nlyte, Sheet uapm) {
		return uapm.assetTag().equals(nlyte.assetTag());
	}
	
	//After tag compare verify that serials are the same
	boolean serialVerification(Sheet nlyte, Sheet uapm) {
		return !(uapm.serialNumber().contains("?")) && !(nlyte.serialNumber().equalsIgnoreCase(uapm.serialNumber()));
	}
	
	//Comparing Cabinates
	public boolean cabinateCompare(Sheet nlyte, Sheet uapm) {
		return !nlyte.cabinateName().equals("No Cab") && !uapm.cabinateName().equals(nlyte.cabinateName());
	}
	
	//Comparing Status
	boolean statusCompare(Sheet nlyte) {
		return !nlyte.operationalStatus().equals("Operational");
	}
	
	
	
}
