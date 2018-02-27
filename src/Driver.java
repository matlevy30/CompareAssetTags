import java.io.IOException;
import java.util.ArrayList;

public class Driver {
	// REMEMBER TO CHANGE , TO . for the UAPM file and use txt to do this change
	private static ArrayList<Sheet> missingTags;
	private static ArrayList<Sheet> TagstoFix;
	private static ArrayList<Sheet> status;
	private static ArrayList<Sheet> cabinet;
	private static ArrayList<Sheet> updateCSV;

	public static void main(String[] args) throws IOException {
		// Reading Nlyte XLSX file
		Reading nlyte = new ReadingNlyte();
		nlyte.readLines();
		ArrayList<Sheet> NlyteInfo = nlyte.getList();

		// Reading Asset Management CVS
		Reading uapm = new ReadingUAPM();
		uapm.readLines();
		ArrayList<Sheet> UAPMInfo = uapm.getList();

		// Missing Asset Tags
		missingTags = new ArrayList<>();

		// Assets in Nlyte but Not in UAPM
		TagstoFix = new ArrayList<>();

		// Assets In Use in UAPM but Not Operational
		status = new ArrayList<>();

		// Assets Cabinate Match
		cabinet = new ArrayList<>();

		//Update List
		updateCSV = new ArrayList<>();

		// =======================================================================
		// Comparing Asset Tag Info for both Nlyte -> UAPM
		compareTag(NlyteInfo, UAPMInfo);
		// =======================================================================
		// Creating xlsx files all of the lists
		WriteExcel w = new WriteExcel();
		w.writtingSheet(uapm.getHeader(), missingTags, "Missing");
		w.writtingSheet(nlyte.getHeader(), TagstoFix, "Fix");
		w.writtingSheet(nlyte.getHeader(), status, "Status");
		w.writtingSheet(nlyte.getHeader(), cabinet, "Cabinet");


		//Writing CSV to write to Tag Fix for SQL Update
		WriteCSV csv = new WriteCSV(nlyte.getHeader(), updateCSV, "Update.csv");
		csv.wirte();

	}

	// Comparing Tags
	private static void compareTag(ArrayList<Sheet> nlyte, ArrayList<Sheet> uapm) {
		// Putting a copy of all uapm to missingTags
		// missingTags.addAll(uapm);
		missingTags = uapm; // to find duplicates
		boolean found = false; // If tag was found or not
		RepoCompare comparator = new RepoCompare(); // Comparator
		for (int i = 0; i != nlyte.size(); ++i) {
			for (int j = 0; j != uapm.size(); ++j) {
				// Comparing if tags are the same
				if (comparator.compareTag(nlyte.get(i), uapm.get(j))) {
					// Has to be Active Operational
					if (comparator.statusCompare(nlyte.get(i))) {
						status.add(nlyte.get(i));
					}
					// Determining if serial numbers match if they don't update serial
					else if (comparator.serialVerification(nlyte.get(i), uapm.get(j))) {
						System.out.println(uapm.get(j).assetTag() + " " + uapm.get(j).serialNumber()+ " (Serial Update)");
						nlyte.get(i).setSerial(uapm.get(j).serialNumber());
						updateCSV.add(nlyte.get(i));
					}
					// Comparing Cabinets Location
					if (comparator.cabinetsCompare(nlyte.get(i), uapm.get(j))) {
						nlyte.get(i).setCabinateName(uapm.get(j).cabinateName());
						cabinet.add(nlyte.get(i));
						//System.out.println(uapm.get(j).cabinateName() + " " +nlyte.get(i).cabinateName());
					}
					found = true;

				} else if (uapm.get(j).serialNumber().equals(nlyte.get(i).serialNumber())
						&& !(uapm.get(j).serialNumber().equals(""))) {
					// Has to be Active Operational
					if (comparator.statusCompare(nlyte.get(i))) {
						status.add(nlyte.get(i));
					}
					// Assets to be updated by adding the Asset tag by comparing Serial Numbers
					else if (!(nlyte.get(i).HostName().contains("Module"))
							&& !(nlyte.get(i).assetTag().contains("CHILD"))) {
						System.out.println(uapm.get(j).assetTag() + " " + uapm.get(j).serialNumber() + " (Tag Update)");
						nlyte.get(i).setAssetTag(uapm.get(j).assetTag());
						updateCSV.add(nlyte.get(i));
					}
					found = true;
				}
				if (found) {
					// Remove from List
					missingTags.remove(uapm.get(j));
					break;
				}
			}
			if (!found) {
				// Assets Tag that where not found in the UAPM sheet or have some kind of typo
				// or funny info
				if (!(nlyte.get(i).assetTag().equals("")) && !(nlyte.get(i).assetTag().contains("N/A"))
						&& !(nlyte.get(i).assetTag().contains("CHILD"))) {
					TagstoFix.add(nlyte.get(i));
				}
			}
			found = false;
		}

	}

	// Adding Serial Numbers from Asset Tags
	@SuppressWarnings("unused")
	private static Sheet addSerial(Sheet nlyte, String serial) {
		nlyte.setSerial(serial);
		return nlyte;
	}

	// Adding Asset Tags from Serial Numbers
	@SuppressWarnings("unused")
	private static Sheet addTag(Sheet nlyte, String tag) {

		nlyte.setAssetTag(tag);
		return nlyte;
	}

}
