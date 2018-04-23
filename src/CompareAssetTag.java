import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class CompareAssetTag {
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
        String[] locations = new String[]{"Ashburn", "Highlands Ranch", "Singapore"};
        String[] datacenter = new String[]{"OCE", "OCC", "OCS"};
        for (int i = 0; i != 3; ++i) {
            WriteExcel w = new WriteExcel(datacenter[i]);
            w.writtingSheet(uapm.getHeader(), missingTags, "Missing", locations[i]);
            String[] header = nlyte.getHeader();
            header[11] = "Reason";
            w.writtingSheet(nlyte.getHeader(), TagstoFix, "Fix", locations[i]);
            w.writtingSheet(nlyte.getHeader(), status, "Status", locations[i]);
            header = nlyte.getHeader();
            header[8] = "Nlyte Location || UAPM Location";
            w.writtingSheet(nlyte.getHeader(), cabinet, "Cabinet", locations[i]);

        }
        //Writing CSV to write to Tag Fix for SQL Update
		WriteCSV csv = new WriteCSV(nlyte.getHeader(), updateCSV, "Update.csv");
		csv.wirte();

	}
	// Comparing Tags
	private static void compareTag(ArrayList<Sheet> nlyte, ArrayList<Sheet> uapm) {
		// Putting a copy of all uapm to missingTags
		boolean found = false; // If tag was found or not
        ArrayList<Sheet> nlyteover = new ArrayList<>();
        nlyteover.addAll(nlyte);
        RepoCompare comparator = new RepoCompare(); // Comparator
        //Record encountered
        HashSet<String> set = new HashSet<>();
        for (int j = 0; j != uapm.size(); ++j) {
            for (int i = 0; i != nlyte.size(); ++i) {
                // Comparing if tags are the same and in UAPM is in USE
                if (uapm.get(j).operationalStatus().equals("In Use") && uapm.get(j).filterLocation()) {
                    if (comparator.compareTag(nlyte.get(i), uapm.get(j))) {
                        // Has to be Active Operational
                        if (comparator.statusCompare(nlyte.get(i))) {
                            status.add(nlyte.get(i));
                        }
                        // Determining if serial numbers match if they don't update serial
                        if (comparator.serialVerification(nlyte.get(i), uapm.get(j))) {
                            System.out.println(uapm.get(j).assetTag() + " " + uapm.get(j).serialNumber() + " (Serial Update)");
                            nlyte.get(i).setSerial(uapm.get(j).serialNumber());
                            updateCSV.add(nlyte.get(i));
                        }
                        //Have to be in same location
                        if (!comparator.compareLocation(nlyte.get(i), uapm.get(j))) {
                            nlyte.get(i).setReason("Locations Don't Match: " + uapm.get(j).location());
                            TagstoFix.add(nlyte.get(i));
                        }
                        // Comparing Cabinets Location
                        if (comparator.cabinetsCompare(nlyte.get(i), uapm.get(j))) {
                            nlyte.get(i).setCabinateName(uapm.get(j).cabinateName());
                            cabinet.add(nlyte.get(i));
                            //System.out.println(uapm.get(j).cabinateName() + " " +nlyte.get(i).cabinateName());
                        }
                        nlyteover.remove(nlyte.get(i));
                        found = true;
                        break;

                    } else if (!(uapm.get(j).serialNumber().equals("")) && uapm.get(j).serialNumber().equals(nlyte.get(i).serialNumber())) {
                        // Has to be Active Operational
                        if (comparator.statusCompare(nlyte.get(i))) {
                            status.add(nlyte.get(i));
                        }
                        // Assets to be updated by adding the Asset tag by comparing Serial Numbers
                        if (!(nlyte.get(i).HostName().contains("Module"))) {
                            System.out.println(uapm.get(j).assetTag() + " " + uapm.get(j).serialNumber() + " (Tag Update)");
                            nlyte.get(i).setAssetTag(uapm.get(j).assetTag());
                            updateCSV.add(nlyte.get(i));
                        }
                        //Have to be in same location
                        if (!comparator.compareLocation(nlyte.get(i), uapm.get(j))) {
                            nlyte.get(i).setReason("Locations Don't Match: " + uapm.get(j).location());
                            TagstoFix.add(nlyte.get(i));
                        }
                        // Comparing Cabinets Location
                        if (comparator.cabinetsCompare(nlyte.get(i), uapm.get(j))) {
                            nlyte.get(i).setCabinateName(uapm.get(j).cabinateName());
                            cabinet.add(nlyte.get(i));
                        }
                        nlyteover.remove(nlyte.get(i));
                        found = true;
                        break;
                    }
                }
            }
            if (!found && uapm.get(j).operationalStatus().equals("In Use") && uapm.get(j).filterLocation()) {
                // Remove from List
                missingTags.add(uapm.get(j));
            }
            found = false;
		}
        //Add remaining devices for Nlyte to be Fixed
        for (Sheet exception : nlyteover) {
            //Adding filter for expcetions
            if (!(exception.HostName().contains("Module")) && exceptionTagsOCE((NlyteSheet) exception)) {
                TagstoFix.add(exception);
            }
        }
        for (Sheet exceptions : TagstoFix) {
            if (!exceptions.getReason().equals("")) {
                continue;
            }
            exceptions.setReason("Asset not located in : " + exceptions.location() + " || No record in UAPM");
            for (Sheet am : uapm) {
                //Checking if the status of the tag or serial is not in USE
                if (comparator.compareTag(exceptions, am)) {
                    if (am.operationalStatus().equals("In Use")) {
                        exceptions.setReason("Duplicated in Nlyte OR It is in a different Room: " + am.room());
                    } else {
                        exceptions.setReason(am.operationalStatus() + " :: " + am.Assetclass());
                    }
                    break;
                } else if (!(am.serialNumber().equals("")) && am.serialNumber().equals(exceptions.serialNumber())) {
                    if (am.operationalStatus().equals("In Use")) {
                        exceptions.setReason("Duplicated in Nlyte OR It is in a different Room: " + am.room());
                    } else {
                        exceptions.setReason(am.operationalStatus() + " :: " + am.Assetclass() + " (Found by Serial Number) ");
                    }
                    break;
                }
            }
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


    private static boolean exceptionTagsOCE(NlyteSheet line) {
        String[] tags = {"WUS0102880", "WUS0102877", "WUS0102873", "WUS0102878"};
        for (String tag : tags) {
            if (line.assetTag().equals(tag)) {
                return false;
            }
        }
        return true;
    }

}
