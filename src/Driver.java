import java.io.IOException;
import java.util.ArrayList;

public class Driver {
	// REMEMBER TO CHAGE , TO . for the UAPM file and use txt to do this change
	public static ArrayList<Sheet> missingTags;
	public static ArrayList<Sheet> TagstoFix;
	public static ArrayList<Sheet> update;

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

	}

	// Comparing Tags
	public static void compareTag(ArrayList<Sheet> nlyte, ArrayList<Sheet> uapm) {
		// Putting a copy of all uapm to missingTags
		//missingTags.addAll(uapm);
		missingTags = uapm;   // to find duplicates
		// If tag was found or not
		boolean found = false;
		for (int i = 0; i != nlyte.size(); ++i) {
			for (int j = 0; j != uapm.size(); ++j) {
				// If the tag is found then add it to the haveTag list and remove from
				// missingTag
				if (uapm.get(j).assetTag().equals(nlyte.get(i).assetTag())) {
					
					// Determining if serial numbers match if they don't update serial
					if (!(uapm.get(j).serialNumber().contains("Â")) && !(nlyte.get(i).serialNumber().equalsIgnoreCase(uapm.get(j).serialNumber()))) {
						//System.out.println(uapm.get(j).assetTag() + " "+ uapm.get(j).serialNumber());
						update.add(addSerial(nlyte.get(i), uapm.get(j).serialNumber()));
					}
					//Remove from List
					
					missingTags.remove(uapm.get(j));
					found = true;
					break;
				} else if (uapm.get(j).serialNumber().equals(nlyte.get(i).serialNumber()) && !(uapm.get(j).serialNumber().equals(""))) {
					//Assets to be updated by adding the Asset tag by comparing Serial Numbers
					if (!(nlyte.get(i).HostName().contains("Module")) && !(nlyte.get(i).assetTag().contains("CHILD"))) {
						System.out.println(uapm.get(j).assetTag() + " "+ uapm.get(j).serialNumber());
						//update.add(addTag(nlyte.get(i), uapm.get(j).assetTag()));
					}
					//Remove from List
					missingTags.remove(uapm.get(j));
					found = true;
					break;
				}
			}
			if (!found) {
				//Assets Tag that where not found in the UAPM sheet or have some kind of typo or funny info
				if (!(nlyte.get(i).assetTag().equals("")) && !( nlyte.get(i).assetTag().contains("N/A")) && !( nlyte.get(i).assetTag().contains("CHILD"))) {
					TagstoFix.add(nlyte.get(i));
				}
			}
			found = false;
		}
	}

	// Adding Serial Numbers from Asset Tags
	//@SuppressWarnings("unused")
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
