
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
        if (cab.equals("NoCab") || cab.contains("NBD") || cab.contains("VTL")) {
            return false;
        } else if (uapm.cabinateName().equals(nlyte.cabinateName())) {
            return false;
        } else if (uapmcab.length() == 6) {
            String nl = cab.substring(0,3);
            String ua = uapmcab.substring(0,3);
            int rowNlyte = Integer.parseInt(cab.substring(3, 6));
            int rowUAPM = Integer.parseInt(uapmcab.substring(3, 6));
            if ((rowNlyte + 1) == rowUAPM || (rowNlyte - 1) == rowUAPM) {
                    return !nl.equals(ua);
            }
        }
        return true;
    }

    //Comparing Status
    boolean statusCompare(Sheet nlyte) {
        return !nlyte.operationalStatus().equals("Operational");
    }


}
