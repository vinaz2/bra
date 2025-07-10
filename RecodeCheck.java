
import java.util.List;

public class RecodeCheck {
    
    public static class RecodeResult {
        public int[] errorInd;
        public double[] setT1Missing;
        public double[] setT2Missing;
        public String[] nbCat;
        public String[] amCat;
        public String[] afCat;
        public String[] nrCat;
        public String[] rHyp;
        public String[] setHyperPMissing;
        public String[] setRHypMissing;
        public String[] setRaceMissing;
        public String[] charRace;
    }
    
    public static RecodeResult recodeCheck(List<PatientData> data, int rawInd) {
        int n = data.size();
        RecodeResult result = new RecodeResult();
        
        // Initialize arrays
        result.errorInd = new int[n];
        result.setT1Missing = new double[n];
        result.setT2Missing = new double[n];
        result.nbCat = new String[n];
        result.amCat = new String[n];
        result.afCat = new String[n];
        result.nrCat = new String[n];
        result.rHyp = new String[n];
        result.setHyperPMissing = new String[n];
        result.setRHypMissing = new String[n];
        result.setRaceMissing = new String[n];
        result.charRace = new String[n];
        
        // Initialize error indicator to 0
        for (int i = 0; i < n; i++) {
            result.errorInd[i] = 0;
        }
        
        // Test for consistency of T1 and T2
        for (int i = 0; i < n; i++) {
            PatientData patient = data.get(i);
            result.setT1Missing[i] = patient.t1;
            result.setT2Missing[i] = patient.t2;
            
            if (patient.t1 < 20 || patient.t1 >= 90 || patient.t1 >= patient.t2) {
                result.setT1Missing[i] = Double.NaN;
                result.errorInd[i] = 1;
            }
            
            if (patient.t2 > 90 || patient.t1 >= patient.t2) {
                result.setT2Missing[i] = Double.NaN;
                result.errorInd[i] = 1;
            }
        }
        
        // Raw covariates (Raw_Ind = 1)
        if (rawInd == 1) {
            // Initialize NB_Cat to -1
            for (int i = 0; i < n; i++) {
                result.nbCat[i] = "-1";
            }
            
            // Process NB_Cat
            for (int i = 0; i < n; i++) {
                PatientData patient = data.get(i);
                
                // Requirement (A)
                if ((patient.nBiop == 0 || patient.nBiop == 99) && patient.hypPlas != 99) {
                    result.nbCat[i] = "A";
                    result.errorInd[i] = 1;
                }
                
                // Requirement (B)
                if ((patient.nBiop > 0 && patient.nBiop < 99) && 
                    (patient.hypPlas != 0 && patient.hypPlas != 1 && patient.hypPlas != 99)) {
                    result.nbCat[i] = "B";
                    result.errorInd[i] = 1;
                }
                
                // Editing and recoding for N_Biop
                if ("-1".equals(result.nbCat[i])) {
                    if (patient.nBiop == 0 || patient.nBiop == 99) {
                        result.nbCat[i] = "0";
                    } else if (patient.nBiop == 1) {
                        result.nbCat[i] = "1";
                    } else if (patient.nBiop >= 2 || patient.nBiop != 99) {
                        result.nbCat[i] = "2";
                    } else {
                        result.nbCat[i] = null;
                    }
                }
            }
            
            // Editing and recoding for AgeMen
            for (int i = 0; i < n; i++) {
                PatientData patient = data.get(i);
                if ((patient.ageMen >= 14 && patient.ageMen <= patient.t1) || patient.ageMen == 99) {
                    result.amCat[i] = "0";
                } else if (patient.ageMen >= 12 && patient.ageMen < 14) {
                    result.amCat[i] = "1";
                } else if (patient.ageMen > 0 && patient.ageMen < 12) {
                    result.amCat[i] = "2";
                } else if (patient.ageMen > patient.t1 && patient.ageMen != 99) {
                    result.amCat[i] = null;
                } else {
                    result.amCat[i] = null;
                }
                
                // Group for African-Americans
                if (patient.race == 2 && "2".equals(result.amCat[i])) {
                    result.amCat[i] = "1";
                }
            }
            
            // Editing and recoding for Age1st
            for (int i = 0; i < n; i++) {
                PatientData patient = data.get(i);
                if (patient.age1st < 20 || patient.age1st == 99) {
                    result.afCat[i] = "0";
                } else if (patient.age1st >= 20 && patient.age1st < 25) {
                    result.afCat[i] = "1";
                } else if ((patient.age1st >= 25 && patient.age1st < 30) || patient.age1st == 98) {
                    result.afCat[i] = "2";
                } else if (patient.age1st >= 30 && patient.age1st < 98) {
                    result.afCat[i] = "3";
                } else if (patient.age1st < patient.ageMen && patient.ageMen != 99) {
                    result.afCat[i] = null;
                } else if (patient.age1st > patient.t1 && patient.age1st < 98) {
                    result.afCat[i] = null;
                } else {
                    result.afCat[i] = null;
                }
                
                // For African-Americans, set to 0
                if (patient.race == 2) {
                    result.afCat[i] = "0";
                }
            }
            
            // Editing and recoding for N_Rels
            for (int i = 0; i < n; i++) {
                PatientData patient = data.get(i);
                if (patient.nRels == 0 || patient.nRels == 99) {
                    result.nrCat[i] = "0";
                } else if (patient.nRels == 1) {
                    result.nrCat[i] = "1";
                } else if (patient.nRels >= 2 && patient.nRels < 99) {
                    result.nrCat[i] = "2";
                } else {
                    result.nrCat[i] = null;
                }
                
                // For Asian-Americans, group 2 with 1
                if (patient.race >= 6 && patient.race <= 11 && "2".equals(result.nrCat[i])) {
                    result.nrCat[i] = "1";
                }
            }
        }
        
        // Raw_Ind = 0 (covariates already recoded)
        if (rawInd == 0) {
            for (int i = 0; i < n; i++) {
                PatientData patient = data.get(i);
                result.nbCat[i] = String.valueOf(patient.nBiop);
                result.amCat[i] = String.valueOf(patient.ageMen);
                result.afCat[i] = String.valueOf(patient.age1st);
                result.nrCat[i] = String.valueOf(patient.nRels);
            }
        }
        
        // Set R_Hyp (multiplicative factor for hyperplasia)
        for (int i = 0; i < n; i++) {
            if ("0".equals(result.nbCat[i])) {
                result.rHyp[i] = "1.00";
            } else if (!"A".equals(result.nbCat[i]) && !"B".equals(result.nbCat[i]) && 
                       !"0".equals(result.nbCat[i]) && data.get(i).hypPlas == 0) {
                result.rHyp[i] = "0.93";
            } else if (!"A".equals(result.nbCat[i]) && !"B".equals(result.nbCat[i]) && 
                       !"0".equals(result.nbCat[i]) && data.get(i).hypPlas == 1) {
                result.rHyp[i] = "1.82";
            } else if (!"A".equals(result.nbCat[i]) && !"B".equals(result.nbCat[i]) && 
                       !"0".equals(result.nbCat[i]) && data.get(i).hypPlas == 99) {
                result.rHyp[i] = "1.00";
            } else {
                result.rHyp[i] = null;
            }
        }
        
        // Set HyperP and R_Hyp missing
        for (int i = 0; i < n; i++) {
            if ("A".equals(result.nbCat[i])) {
                result.setHyperPMissing[i] = "A";
                result.setRHypMissing[i] = "A";
            } else if ("B".equals(result.nbCat[i])) {
                result.setHyperPMissing[i] = "B";
                result.setRHypMissing[i] = "B";
            } else {
                result.setHyperPMissing[i] = String.valueOf(data.get(i).hypPlas);
                result.setRHypMissing[i] = result.rHyp[i];
            }
        }
        
        // Set Race missing
        for (int i = 0; i < n; i++) {
            int race = data.get(i).race;
            if (race >= 1 && race <= 11) {
                result.setRaceMissing[i] = String.valueOf(race);
            } else {
                result.setRaceMissing[i] = "U";
                result.errorInd[i] = 1;
            }
        }
        
        // Set error indicator
        for (int i = 0; i < n; i++) {
            if (result.nbCat[i] == null || result.amCat[i] == null || 
                result.afCat[i] == null || result.nrCat[i] == null || 
                "U".equals(result.setRaceMissing[i])) {
                result.errorInd[i] = 1;
            }
        }
        
        // African-American adjustments
        for (int i = 0; i < n; i++) {
            PatientData patient = data.get(i);
            if (patient.race == 2) {
                result.afCat[i] = "0";
                if ("2".equals(result.amCat[i])) {
                    result.amCat[i] = "1";
                }
            }
        }
        
        // Hispanic adjustments
        for (int i = 0; i < n; i++) {
            PatientData patient = data.get(i);
            if (patient.race == 3 || patient.race == 5) {
                // N_Biop grouping
                if (patient.nBiop == 0 || patient.nBiop == 99) {
                    result.nbCat[i] = "0";
                } else if ("2".equals(result.nbCat[i])) {
                    result.nbCat[i] = "1";
                }
                
                // AgeMen elimination for US-born Hispanics
                if (patient.race == 3) {
                    result.amCat[i] = "0";
                }
                
                // Age1st grouping
                if ("2".equals(result.afCat[i])) {
                    result.afCat[i] = "1";
                } else if ("3".equals(result.afCat[i])) {
                    result.afCat[i] = "2";
                }
                
                // N_Rels grouping
                if ("2".equals(result.nrCat[i])) {
                    result.nrCat[i] = "1";
                }
            }
        }
        
        // Asian-American adjustments
        for (int i = 0; i < n; i++) {
            PatientData patient = data.get(i);
            if (patient.race >= 6 && patient.race <= 11 && "2".equals(result.nrCat[i])) {
                result.nrCat[i] = "1";
            }
        }
        
        // Set CharRace
        for (int i = 0; i < n; i++) {
            int race = data.get(i).race;
            if (race == 1) {
                result.charRace[i] = "Wh";
            } else if (race == 2) {
                result.charRace[i] = "AA";
            } else if (race == 3) {
                result.charRace[i] = "HU";
            } else if (race == 4) {
                result.charRace[i] = "NA";
            } else if (race == 5) {
                result.charRace[i] = "HF";
            } else if (race == 6) {
                result.charRace[i] = "Ch";
            } else if (race == 7) {
                result.charRace[i] = "Ja";
            } else if (race == 8) {
                result.charRace[i] = "Fi";
            } else if (race == 9) {
                result.charRace[i] = "Hw";
            } else if (race == 10) {
                result.charRace[i] = "oP";
            } else if (race == 11) {
                result.charRace[i] = "oA";
            } else {
                result.charRace[i] = "??";
            }
        }
        
        return result;
    }
}