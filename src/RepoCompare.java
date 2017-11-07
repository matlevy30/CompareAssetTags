import org.apache.xmlbeans.impl.schema.StscChecker;

class RepoCompare {

    //Comparing String Tags
    boolean compareTag(Sheet nlyte, Sheet uapm) {
        return uapm.assetTag().equals(nlyte.assetTag());
    }

    //After tag compare verify that serials are the same
    boolean serialVerification(Sheet nlyte, Sheet uapm) {
       return !(uapm.serialNumber().contains("?")) && !(nlyte.serialNumber().equalsIgnoreCase(uapm.serialNumber()));
    }

    //Comparing Cabinets
    boolean cabinetsCompare(Sheet nlyte, Sheet uapm) {
        String cab = nlyte.cabinateName();
        String uapmcab = uapm.cabinateName();
        if(nlyte.location().contains("Ashburn")) {
            if (cab.equals("NoCab") || cab.contains("NBD") || cab.contains("VTL")) {
                return false;
            } else if (uapm.cabinateName().equals(nlyte.cabinateName())) {
                return false;
            } else if (uapmcab.length() == 6) {
                int rowNlyte = Integer.parseInt(cab.substring(3, 6));
                int rowUAPM = Integer.parseInt(uapmcab.substring(3, 6));
                if ((rowNlyte + 1) == rowUAPM || (rowNlyte - 1) == rowUAPM) {
                    return !((cab.substring(0,3).equals(uapmcab.substring(0,3))));
                }
            }
        }
        else if(nlyte.location().contains("Highlands Ranch")) {
            if (cab.equals("NoCab") || cab.contains("ITD") || cab.contains("OFD") || cab.contains("TCH") || cab.contains("SU550") || cab.contains("TELCO") || cab.contains("TPF")) {
                return false;
            } else if (nlyte.cabinateName().contains(uapm.cabinateName())) {
                return false;
            }else if(nlyte.cabinateName().length() >= 9) {
                int pos = cab.lastIndexOf("-");
                int pos1 = uapmcab.lastIndexOf("-");
                int rowNlyte = Integer.parseInt(cab.substring(pos+1,cab.length()).trim());
                int rowUAPM = Integer.parseInt(uapmcab.substring(pos1+1,uapmcab.length()).trim());
                //Comparing Row Position
                if ((rowNlyte + 1) == rowUAPM ) {
                    String ncab = cab.substring(0,pos+1) + Integer.toString(rowNlyte+1);
                    return !(ncab.contains(uapmcab));
                }else if((rowNlyte - 1) == rowUAPM) {
                    String ncab = cab.substring(0,pos+1) + Integer.toString(rowNlyte-1);
                    return !(ncab.contains(uapmcab));
                }else if(rowNlyte == rowUAPM) {
                    String ncab = cab.substring(0,pos+1) + Integer.toString(rowNlyte);
                    String ucab = uapmcab.substring(0,pos1+1) + Integer.toString(rowUAPM);
                    return !(ncab.contains(ucab));
                }
            }
        }
        else if(nlyte.location().contains("Singapore")) {
            if (cab.equals("NoCab") || cab.contains("NBD") || cab.contains("VTL")) {
                return false;
            }
            else if (uapm.cabinateName().equals(nlyte.cabinateName())) {
                return false;
            }
        }
        return true;
    }

    //Comparing Status
    boolean statusCompare(Sheet nlyte) {
        return !nlyte.operationalStatus().equals("Operational");
    }


}
