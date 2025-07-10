import java.util.List;
import java.util.ArrayList;

public class RelativeRisk {
    
    public static class RelativeRiskResult {
        public double[] rrStar1;
        public double[] rrStar2;
        public int[] patternNumber;
    }
    
    public static RelativeRiskResult relativeRisk(List<PatientData> data, int rawInd) {
        int n = data.size();
        RelativeRiskResult result = new RelativeRiskResult();
        result.rrStar1 = new double[n];
        result.rrStar2 = new double[n];
        result.patternNumber = new int[n];
        
        // Beta coefficients for different races
        double[][] wrkBetaAll = {
            // White
            {0.5292641686, 0.0940103059, 0.2186262218, 0.9583027845, -0.2880424830, -0.1908113865},
            // Black
            {0.1822121131, 0.2672530336, 0.0, 0.4757242578, -0.1119411682, 0.0},
            // Hispanic
            {0.0970783641, 0.0000000000, 0.2318368334, 0.166685441, 0.0000000000, 0.0000000000},
            // Other
            {0.5292641686, 0.0940103059, 0.2186262218, 0.9583027845, -0.2880424830, -0.1908113865},
            // FHspnc
            {0.4798624017, 0.2593922322, 0.4669246218, 0.9076679727, 0.0000000000, 0.0000000000},
            // Asian (6-11)
            {0.55263612260619, 0.07499257592975, 0.27638268294593, 0.79185633720481, 0.0, 0.0},
            {0.55263612260619, 0.07499257592975, 0.27638268294593, 0.79185633720481, 0.0, 0.0},
            {0.55263612260619, 0.07499257592975, 0.27638268294593, 0.79185633720481, 0.0, 0.0},
            {0.55263612260619, 0.07499257592975, 0.27638268294593, 0.79185633720481, 0.0, 0.0},
            {0.55263612260619, 0.07499257592975, 0.27638268294593, 0.79185633720481, 0.0, 0.0},
            {0.55263612260619, 0.07499257592975, 0.27638268294593, 0.79185633720481, 0.0, 0.0}
        };
        
        // Get recoded data
        RecodeCheck.RecodeResult recodeResult = RecodeCheck.recodeCheck(data, rawInd);
        
        // Initialize pattern number
        for (int i = 0; i < n; i++) {
            result.patternNumber[i] = -1; // -1 for NA
        }
        
        // Find valid IDs (PNID)
        List<Integer> validIds = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String nbCat = recodeResult.nbCat[i];
            String amCat = recodeResult.amCat[i];
            String afCat = recodeResult.afCat[i];
            String nrCat = recodeResult.nrCat[i];
            
            if (!"A".equals(nbCat) && !"B".equals(nbCat) && 
                amCat != null && afCat != null && nrCat != null) {
                try {
                    double nb = Double.parseDouble(nbCat);
                    double am = Double.parseDouble(amCat);
                    double af = Double.parseDouble(afCat);
                    double nr = Double.parseDouble(nrCat);
                    
                    if (!Double.isNaN(nb) && !Double.isNaN(am) && 
                        !Double.isNaN(af) && !Double.isNaN(nr)) {
                        validIds.add(i);
                        result.patternNumber[i] = (int)(nb * 36 + am * 12 + af * 3 + nr) + 1;
                    }
                } catch (NumberFormatException e) {
                    // Invalid number format, skip
                }
            }
        }
        
        // Calculate LP1 and LP2 for valid IDs
        for (int i : validIds) {
            if ("??".equals(recodeResult.charRace[i])) continue;
            
            int raceIndex = data.get(i).race - 1;
            if (raceIndex < 0 || raceIndex >= wrkBetaAll.length) continue;
            
            double[] beta = wrkBetaAll[raceIndex];
            double nb = Double.parseDouble(recodeResult.nbCat[i]);
            double am = Double.parseDouble(recodeResult.amCat[i]);
            double af = Double.parseDouble(recodeResult.afCat[i]);
            double nr = Double.parseDouble(recodeResult.nrCat[i]);
            double rHyp = Double.parseDouble(recodeResult.rHyp[i]);
            
            // Calculate LP1 (age < 50)
            double lp1 = nb * beta[0] + am * beta[1] + af * beta[2] + 
                         nr * beta[3] + (af * nr) * beta[5] + Math.log(rHyp);
            
            // Calculate LP2 (age >= 50)
            double lp2 = lp1 + nb * beta[4];
            
            result.rrStar1[i] = Math.exp(lp1);
            result.rrStar2[i] = Math.exp(lp2);
        }
        
        return result;
    }
}