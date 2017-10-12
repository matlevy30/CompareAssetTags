
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
			System.out.print(values[4] +" ");
			System.out.println(values[5]);
			StringBuilder sb = new StringBuilder();
			int pod = values[4].length() -1;
			sb.append(values[4].substring(pod));
			
			int pos = values[5].indexOf("-");
			sb.append(values[5].substring(0, pos));
			sb.append(values[5].substring(pos+1, values[5].length()));
			
			return sb.toString();
		}

	}

