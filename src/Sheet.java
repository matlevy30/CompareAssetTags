
public abstract class Sheet {

	String[]values;
	
	Sheet(String[] values) {
		this.values = values;
	}

	public abstract String assetTag();

	public abstract String serialNumber();

	public abstract String HostName();
	
	public abstract String operationalStatus();
	
	public abstract String cabinateName();

	public abstract String location();
	
	public String getNumber() {
		throw new UnsupportedOperationException("Can't do this with UAPM sheet");
	}

	public void setSerial(String s) {
		throw new UnsupportedOperationException("Can't do this with UAPM sheet");
	}
	
	public void setAssetTag(String s) {
		throw new UnsupportedOperationException("Can't do this with UAPM sheet");
	}
	
	String[] getLine() {
		return values;
	}
	// Printing line values, local methods
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i != values.length; ++i) {
			sb.append(values[i]);
			sb.append(", ");
		}
		return sb.toString();
	}
}
