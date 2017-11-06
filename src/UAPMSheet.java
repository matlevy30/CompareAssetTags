
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

		if (values[5].length() != 0) {

			int pod = values[4].length() - 1;
			sb.append(values[4].substring(pod));

			int pos = values[5].indexOf("-");
			sb.append(values[5].substring(0, pos));
			sb.append(values[5].substring(pos + 1, values[5].length()));

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
