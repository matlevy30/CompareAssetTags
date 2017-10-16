
public class RepoCompare {

	//Comparing String Tags
	public boolean compareTag(Sheet nlyte, Sheet uapm) {
		return uapm.assetTag().equals(nlyte.assetTag());
	}
	
	//After tag compare verify that serials are the same
	public boolean serialVerification(Sheet nlyte, Sheet uapm) {
		return !(uapm.serialNumber().contains("Â")) && !(nlyte.serialNumber().equalsIgnoreCase(uapm.serialNumber()));
	}
	
	//Comparing Cabinates
	public boolean cabinateCompare(Sheet nlyte, Sheet uapm) {
		return !nlyte.cabinateName().equals("No Cab") && !uapm.cabinateName().equals(nlyte.cabinateName());
	}
	
	//Comparing Status
	public boolean statusCompare(Sheet nlyte) {
		return !nlyte.operationalStatus().equals("Operational");
	}
	
	
	
}
