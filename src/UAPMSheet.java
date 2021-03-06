public class UAPMSheet extends Sheet {

	UAPMSheet(String[] values) {
		super(values);
	}

	@Override
	public String assetTag() {
		return values[1].trim();
	}

	@Override
	public String serialNumber() {
		//Replacing Special Character that Excel puts once downloaded
		char a = 160;
		String especial = Character.toString(a);
		if(values[2].contains(especial)) {
			values[2] = values[2].replace(a,' ');
		}
		return values[2].trim();
	}

	@Override
	public String HostName() {
		return values[0].trim();
	}

	@Override
	public String operationalStatus() {
		return values[3].trim();
	}

	@Override
	public String cabinateName() {
		StringBuilder sb = new StringBuilder();
		//OCS Cabinet Name
		if (values[9].contains("Singapore")) {
			sb.append(values[5]);
		}
		//OCC Cabinet Name
		else if(values[9].contains("Highlands Ranch")) {
			if(values[5].contains("Row") || values[5].contains("Cab")) {
				String up = values[5].toUpperCase();
				String v = up.replace("ROW", "");
				String v2 = v.replace("CAB C", "");
				String v3 = v2.replace(" ", "");
				String v4 = v3.replace("RACK","A-");
				sb.append(v4.trim());
			}
			else if(values[5].contains("PKI")) {
				String up = values[5].toUpperCase();
				String v = up.replace(" PKI CAGE", "");
				sb.append(v.trim());
			}
			else {
				sb.append(values[5]);
			}
		}
		//OCE cabinet Name
		else if (values[9].contains("Ashburn")) {
			//Back Office Cabinets
			if (values[5].contains("Cabinet")) {
				sb.append(values[5]);
			}
			else if (values[5].length() != 0) {
				int pod = values[4].length() - 1;
				sb.append(values[4].substring(pod));

				int pos = values[5].indexOf("-");
				sb.append(values[5].substring(0, pos));
				sb.append(values[5].substring(pos + 1, values[5].length()));

			}
		}
		return sb.toString();
	}

	//Asset Found
	public String Assetclass() {
		return values[10].trim();
	}

	@Override
	public String room() {
		return values[4].trim();
	}

	//Location filter

	public boolean filterLocation() {
		// OCC Locations
		if (values[9].contains("Highlands Ranch")) {
			if (values[4].equals("Data Center") || values[4].contains("Data Hall") || values[4].equals("Telco Room")
					|| values[4].equals("Demarc") || values[4].equals("Tape Storage")) {
				return true;
			}
		}
		// OCE Locations
		else if (values[9].contains("Ashburn")) {
			if ((values[4].contains("Pod") || values[4].equals("AV Room")
					|| values[4].contains("PKI") || values[4].contains("TR")
					|| values[4].contains("Switch Gear") || values[4].contains("UPS") || values[4].contains("ER"))
					&& !(values[5].equals("Tape Library")) && !values[4].equals("UPS 6A")
					&& !values[4].equals("UPS 7B") && !values[4].contains("NOC Room") && !values[4].contains("NOC Data Center")) {
				return true;
			} else if (values[4].equals("Telco Room")) {
				return true;
			}
		}
		//OCS Locations
		else if (values[9].contains("Singapore")) {
			if (values[4].equals("Data Center")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String location() {
		if (values[9].contains("Highlands Ranch")) return "Highlands Ranch";
		else if (values[9].contains("Ashburn")) return "Ashburn";
		else if (values[9].contains("Singapore")) return "Singapore";
		return values[9].trim();
	}
}
