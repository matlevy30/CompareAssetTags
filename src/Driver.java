import java.io.IOException;
import java.util.ArrayList;

public class Driver {
	// REMEMBER TO CHAGE , TO . for the UAPM file and use txt to do this change
	public static ArrayList<Sheet> missingTags;
	public static ArrayList<Sheet> TagstoFix;
	public static ArrayList<Sheet> update;
	public static ArrayList<Sheet> status;
	public static ArrayList<Sheet> cabinate;
         
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

		// Assets where found Tags and No Serial and Vice versa
		update = new ArrayList<>();

		// Assets In Use in UAPM but Not Operational
		status = new ArrayList<>();

		// Assets Cabinate Match
		cabinate = new ArrayList<>();

		// =======================================================================
		// Comparing Asset Tag Info for both Nlyte -> UAPM
		compareTag(NlyteInfo, UAPMInfo);
		// =======================================================================
		// Creating CSV files all of the lists
		WriteCSV write = new WriteCSV(uapm.getHeader(), missingTags, "Missing.csv");
		write.wirte();

		write = new WriteCSV(nlyte.getHeader(), TagstoFix, "Fix.csv");
		write.wirte();

		write = new WriteCSV(nlyte.getHeader(), update, "Update.csv");
		write.wirte();

		write = new WriteCSV(nlyte.getHeader(), status, "Status.csv");
		write.wirte();

		write = new WriteCSV(nlyte.getHeader(), cabinate, "Cabinate.csv");
		write.wirte();

	}

	// Comparing Tags
	public static void compareTag(ArrayList<Sheet> nlyte, ArrayList<Sheet> uapm) {
		// Putting a copy of all uapm to missingTags
		// missingTags.addAll(uapm);
		missingTags = uapm; // to find duplicates
		boolean found = false; // If tag was found or not
		RepoCompare comparator = new RepoCompare(); // Comparator
		for (int i = 0; i != nlyte.size(); ++i) {
			for (int j = 0; j != uapm.size(); ++j) {
				// If the tag is found then add it to the haveTag list and remove from
				// missingTag
				if (comparator.compareTag(nlyte.get(i), uapm.get(j))) {
					// Has to be Active Operational
					if (comparator.statusCompare(nlyte.get(i))) {
						status.add(nlyte.get(i));
					}
					// Determining if serial numbers match if they don't update serial
					else if (comparator.serialVerification(nlyte.get(i), uapm.get(j))) {
						//System.out.println(uapm.get(j).assetTag() + " " + uapm.get(j).serialNumber());
						// update.add(addSerial(nlyte.get(i), uapm.get(j).serialNumber()));
					}
					// Comparing Cabinate Location
					if (comparator.cabinateCompare(nlyte.get(i), uapm.get(j))) {
						cabinate.add(nlyte.get(i));
						// System.out.println(uapm.get(j).cabinateName() + " " +
						// nlyte.get(i).cabinateName());
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
						//System.out.println(uapm.get(j).assetTag() + " " + uapm.get(j).serialNumber());
						// update.add(addTag(nlyte.get(i), uapm.get(j).assetTag()));
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
