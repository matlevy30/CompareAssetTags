
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
		if(values[8].contains("Singapore")) {
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
		else if(values[6].contains("Ashburn")) {
			if (values[5].length() != 0) {
				int pod = values[4].length() - 1;
				sb.append(values[4].substring(pod));

				int pos = values[5].indexOf("-");
				sb.append(values[5].substring(0, pos));
				sb.append(values[5].substring(pos + 1, values[5].length()));

			}
		}
		return sb.toString();
	}

	@Override
	public String location() {
		String s = "";
	    if (values[9].contains("Highlands Ranch")) s =  values[9].trim();
		else if (values[6].contains("Ashburn")) s =  values[6].trim();
        return s;
	}
}
