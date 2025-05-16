package adduct;

public class Adduct {

    /**
     * Calculate the mass (M) to search depending on the adduct hypothesis
     *
     * @param mz mz
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return the monoisotopic mass of the experimental mass mz with the adduct @param adduct
     */
    public static Double getMonoisotopicMassFromMZ(Double mz, String adduct) {
        Double massToSearch=null;
        // !! TODO METHOD
        // !! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).

        /*
        if Adduct is single charge the formula is M = m/z +- adductMass. Charge is 1 so it does not affect

        if Adduct is double or triple charged the formula is M = ( mz +- adductMass ) * charge

        if adduct is a dimer or multimer the formula is M =  (mz +- adductMass) / numberOfMultimer

        return monoisotopicMass;

         */

        if (AdductList.MAPMZPOSITIVEADDUCTS.containsKey(adduct)) {
            massToSearch = AdductList.MAPMZPOSITIVEADDUCTS.get(adduct);
        } else if(AdductList.MAPMZNEGATIVEADDUCTS.containsKey(adduct)) {
            massToSearch= AdductList.MAPMZNEGATIVEADDUCTS.get(adduct);
        } else {
            System.err.println("Unknown adduct: " + adduct);
            return null;
        }

        int multimer=1;
        int charge =1;


        int mIndex = adduct.indexOf("M");
        if (mIndex > 1){
            String beforeM = adduct.substring(1, mIndex);
            try{
                multimer=Integer.parseInt(beforeM);
            } catch (NumberFormatException e){
            }
        }

        char lastChar = adduct.charAt(adduct.length()-1);
        if (lastChar == '+' || lastChar == '-'){
            char mult_char = adduct.charAt(adduct.length()-2);
            if (Character.isDigit(mult_char)){
                charge = Character.getNumericValue(mult_char);
            }

        }


        double monoMass = ((mz * charge) + massToSearch) / multimer;

        return monoMass;

    }

    /**
     * Calculate the mz of a monoisotopic mass with the corresponding adduct
     *
     * @param monoisotopicMass
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return
     */
    public static Double getMZFromMonoisotopicMass(Double monoisotopicMass, String adduct) {
        Double massToSearch;

        if (AdductList.MAPMZPOSITIVEADDUCTS.containsKey(adduct)) {
            massToSearch = AdductList.MAPMZPOSITIVEADDUCTS.get(adduct);
        } else if(AdductList.MAPMZNEGATIVEADDUCTS.containsKey(adduct)) {
            massToSearch= AdductList.MAPMZNEGATIVEADDUCTS.get(adduct);
        } else {
            return null;
        }

        int multimer=1;
        int charge =1;

        int mIndex = adduct.indexOf("M");
        if (mIndex > 1){
            String beforeM = adduct.substring(1, mIndex);
            try{
                multimer=Integer.parseInt(beforeM);
            } catch (NumberFormatException e){
                System.out.println("multimer = 1");
            }
        }

        char lastChar = adduct.charAt(adduct.length()-1);
        if (lastChar == '+' || lastChar == '-') {
            char mult_char = adduct.charAt(adduct.length() - 2);

            if (Character.isDigit(mult_char)) {
                charge = Character.getNumericValue(mult_char);
            }
        }
        return (monoisotopicMass * multimer - massToSearch) / charge;
    }

    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param experimentalMass    Mass measured by MS
     * @param theoreticalMass Theoretical mass of the compound
     */
    public static int calculatePPMIncrement(Double experimentalMass, Double theoreticalMass) {
        int ppmIncrement;
        ppmIncrement = (int) Math.round(Math.abs((experimentalMass - theoreticalMass) * 1000000
                / theoreticalMass));
        return ppmIncrement;
    }

    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param experimentalMass    Mass measured by MS
     * @param ppm ppm of tolerance
     */
    public static double calculateDeltaPPM(Double experimentalMass, int ppm) {
        double deltaPPM;
        deltaPPM =  Math.round(Math.abs((experimentalMass * ppm) / 1000000));
        return deltaPPM;

    }


}
