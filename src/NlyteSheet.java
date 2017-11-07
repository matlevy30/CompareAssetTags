
public class NlyteSheet extends Sheet {

	NlyteSheet(String[] values) {
		super(values);
	}

	@Override
	public String assetTag() {
		return values[4].trim().toUpperCase();
	}

	@Override
	public String serialNumber() {
		return values[3].trim();
	}

	@Override
	public String HostName() {
		return values[0].trim();
	}

	@Override
	public String operationalStatus() {
		return values[6].trim();
			
	}

	@Override
	public String location() {
		return values[7].trim();
	}

	@Override
	public void setCabinateName(String uapm) {
		values[8] = cabinateName() +" || "+ uapm;
	}


	@Override
	public String cabinateName() {
		if(location().contains("Singapore")) {
			if(values[8].length() !=3) {
				return "NoCab";
			}
		}
		else if(location().contains("Highlands Ranch")) {
			if(values[8].length() == 0 ){
				return "NoCab";
			}
		}
		else if(location().contains("Ashburn")) {
			if (values[8].length() != 6) {
				return "NoCab";
			}
		}

		return values[8].trim();
	}
	

	public String getNumber() {
		return values[1].trim();
	}
	
	@Override
	public void setSerial(String s) {
		values[3] = s.trim().toUpperCase();
	}

	@Override
	public void setAssetTag(String s) {
		values[4] = s.trim().toUpperCase();
	}
	
}
