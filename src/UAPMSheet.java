
public class UAPMSheet extends Sheet{

		
		public UAPMSheet(String[] values) {
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
			// TODO Auto-generated method stub
			return null;
		}

	}

