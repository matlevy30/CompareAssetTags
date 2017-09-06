
public abstract class Sheet {

	String[]values;
	
	public Sheet(String[] values) {
		this.values = values;
	}

	public abstract String assetTag();

	public abstract String serialNumber();

	public abstract String HostName();
	
	public String getNumber() {
		throw new UnsupportedOperationException("Can't do this with UAPM sheet");
	}

	public String[] getLine() {
		return values;
	}

	public void setSerial(String s) {
		throw new UnsupportedOperationException("Can't do this with UAPM sheet");
	}
	
	public void setAssetTag(String s) {
		throw new UnsupportedOperationException("Can't do this with UAPM sheet");
	}

	// Printing line values, local methods
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i != values.length; ++i) {
			sb.append(values[i] + ", ");
		}
		return sb.toString();
	}
}
