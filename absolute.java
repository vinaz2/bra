import java.util.List;
import java.util.ArrayList;

public class absolute {
    
    public static double[] absoluteRisk2(List<PatientData> data, int rawInd, int avgWhite) {
        int n = data.size();
        double[] absRisk = new double[n];
        
        // Initialize with NaN
        for (int i = 0; i < n; i++) {
            absRisk[i] = Double.NaN;
        }
        
        // Breast cancer incidence rates
        double[] whiteLambda1 = {0.00001000, 0.00007600, 0.00026600, 0.00066100, 0.00126500, 0.00186600, 0.00221100, 
                                0.00272100, 0.00334800, 0.00392300, 0.00417800, 0.00443900, 0.00442100, 0.00410900};
        
        double[] whiteLambda1Avg = {0.00001220, 0.00007410, 0.00022970, 0.00056490, 0.00116450, 0.00195250, 0.00261540, 
                                    0.00302790, 0.00367570, 0.00420290, 0.00473080, 0.00494250, 0.00479760, 0.00401060};
        
        double[] whiteNlambda1 = {0.0000120469, 0.0000746893, 0.0002437767, 0.0005878291, 0.0012069622, 0.0019762053, 0.0026200977, 
                                  0.0033401788, 0.0039743676, 0.0044875763, 0.0048945499, 0.0051610641, 0.0048268456, 0.0040407389};
        
        double[] blackLambda1 = {0.00002696, 0.00011295, 0.00031094, 0.00067639, 0.00119444, 0.00187394, 0.00241504, 
                                 0.00291112, 0.00310127, 0.00366560, 0.00393132, 0.00408951, 0.00396793, 0.00363712};
        
        double[] hspncLambda1 = {0.0000166, 0.0000741, 0.0002740, 0.0006099, 0.0012225, 0.0019027, 0.0023142, 
                                 0.0028357, 0.0031144, 0.0030794, 0.0033344, 0.0035082, 0.0025308, 0.0020414};
        
        double[] otherLambda1 = {0.00001000, 0.00007600, 0.00026600, 0.00066100, 0.00126500, 0.00186600, 0.00221100, 
                                 0.00272100, 0.00334800, 0.00392300, 0.00417800, 0.00443900, 0.00442100, 0.00410900};
        
        double[] fHspncLambda1 = {0.0000102, 0.0000531, 0.0001578, 0.0003602, 0.0007617, 0.0011599, 0.0014111,
                                  0.0017245, 0.0020619, 0.0023603, 0.0025575, 0.0028227, 0.0028295, 0.0025868};
        
        double[] chnesLambda1 = {0.000004059636, 0.000045944465, 0.000188279352, 0.000492930493, 0.000913603501,
                                 0.001471537353, 0.001421275482, 0.001970946494, 0.001674745804, 0.001821581075,
                                 0.001834477198, 0.001919911972, 0.002233371071, 0.002247315779};
        
        double[] japnsLambda1 = {0.000000000001, 0.000099483924, 0.000287041681, 0.000545285759, 0.001152211095,
                                 0.001859245108, 0.002606291272, 0.003221751682, 0.004006961859, 0.003521715275,
                                 0.003593038294, 0.003589303081, 0.003538507159, 0.002051572909};
        
        double[] filipLambda1 = {0.000007500161, 0.000081073945, 0.000227492565, 0.000549786433, 0.001129400541,
                                 0.001813873795, 0.002223665639, 0.002680309266, 0.002891219230, 0.002534421279,
                                 0.002457159409, 0.002286616920, 0.001814802825, 0.001750879130};
        
        double[] hawaiLambda1 = {0.000045080582, 0.000098570724, 0.000339970860, 0.000852591429, 0.001668562761,
                                 0.002552703284, 0.003321774046, 0.005373001776, 0.005237808549, 0.005581732512,
                                 0.005677419355, 0.006513409962, 0.003889457523, 0.002949061662};
        
        double[] otrPILambda1 = {0.000000000001, 0.000071525212, 0.000288799028, 0.000602250698, 0.000755579402,
                                 0.000766406354, 0.001893124938, 0.002365580107, 0.002843933070, 0.002920921732,
                                 0.002330395655, 0.002036291235, 0.001482683983, 0.001012248203};
        
        double[] otrAsLambda1 = {0.000012355409, 0.000059526456, 0.000184320831, 0.000454677273, 0.000791265338,
                                 0.001048462801, 0.001372467817, 0.001495473711, 0.001646746198, 0.001478363563,
                                 0.001216010125, 0.001067663700, 0.001376104012, 0.000661576644};
        
        // Competing hazards
        double[] whiteLambda2 = {0.00049300, 0.00053100, 0.00062500, 0.00082500, 0.00130700, 0.00218100, 0.00365500, 
                                 0.00585200, 0.00943900, 0.01502800, 0.02383900, 0.03883200, 0.06682800, 0.14490800};
        
        double[] whiteLambda2Avg = {0.00044120, 0.00052540, 0.00067460, 0.00090920, 0.00125340, 0.00195700, 0.00329840, 
                                    0.00546220, 0.00910350, 0.01418540, 0.02259350, 0.03611460, 0.06136260, 0.14206630};
        
        double[] whiteNlambda2 = {0.0004000377, 0.0004280396, 0.0005656742, 0.0008474486, 0.0012752947, 0.0018601059, 0.0028780622, 
                                  0.0046903348, 0.0078835252, 0.0127434461, 0.0208586233, 0.0335901145, 0.0575791439, 0.1377327125};
        
        double[] blackLambda2 = {0.00074354, 0.00101698, 0.00145937, 0.00215933, 0.00315077, 0.00448779, 0.00632281, 
                                 0.00963037, 0.01471818, 0.02116304, 0.03266035, 0.04564087, 0.06835185, 0.13271262};
        
        double[] hspncLambda2 = {0.0003561, 0.0004038, 0.0005281, 0.0008875, 0.0013987, 0.0020769, 0.0030912,
                                 0.0046960, 0.0076050, 0.0120555, 0.0193805, 0.0288386, 0.0429634, 0.0740349};
        
        double[] otherLambda2 = {0.00049300, 0.00053100, 0.00062500, 0.00082500, 0.00130700, 0.00218100, 0.00365500, 
                                 0.00585200, 0.00943900, 0.01502800, 0.02383900, 0.03883200, 0.06682800, 0.14490800};
        
        double[] fHspncLambda2 = {0.0003129, 0.0002908, 0.0003515, 0.0004943, 0.0007807, 0.0012840, 0.0020325,
                                  0.0034533, 0.0058674, 0.0096888, 0.0154429, 0.0254675, 0.0448037, 0.1125678};
        
        double[] chnesLambda2 = {0.000210649076, 0.000192644865, 0.000244435215, 0.000317895949, 0.000473261994,
                                 0.000800271380, 0.001217480226, 0.002099836508, 0.003436889186, 0.006097405623,
                                 0.010664526765, 0.020148678452, 0.037990796590, 0.098333900733};
        
        double[] japnsLambda2 = {0.000173593803, 0.000295805882, 0.000228322534, 0.000363242389, 0.000590633044,
                                 0.001086079485, 0.001859999966, 0.003216600974, 0.004719402141, 0.008535331402,
                                 0.012433511681, 0.020230197885, 0.037725498348, 0.106149118663};
        
        double[] filipLambda2 = {0.000229120979, 0.000262988494, 0.000314844090, 0.000394471908, 0.000647622610,
                                 0.001170202327, 0.001809380379, 0.002614170568, 0.004483330681, 0.007393665092,
                                 0.012233059675, 0.021127058106, 0.037936954809, 0.085138518334};
        
        double[] hawaiLambda2 = {0.000563507269, 0.000369640217, 0.001019912579, 0.001234013911, 0.002098344078,
                                 0.002982934175, 0.005402445702, 0.009591474245, 0.016315472607, 0.020152229069,
                                 0.027354838710, 0.050446998723, 0.072262026612, 0.145844504021};
        
        double[] otrPILambda2 = {0.000465500812, 0.000600466920, 0.000851057138, 0.001478265376, 0.001931486788,
                                 0.003866623959, 0.004924932309, 0.008177071806, 0.008638202890, 0.018974658371,
                                 0.029257567105, 0.038408980974, 0.052869579345, 0.074745721133};
        
        double[] otrAsLambda2 = {0.000212632332, 0.000242170741, 0.000301552711, 0.000369053354, 0.000543002943,
                                 0.000893862331, 0.001515172239, 0.002574669551, 0.004324370426, 0.007419621918,
                                 0.013251765130, 0.022291427490, 0.041746550635, 0.087485802065};
        
        // 1 - Attributable Risk
        double[][] wrk1ARAll = {
            {0.5788413, 0.5788413},       // White
            {0.72949880, 0.74397137},     // Black
            {0.749294788397, 0.778215491668}, // Hispanic
            {0.5788413, 0.5788413},       // Other
            {0.428864989813, 0.450352338746}, // FHspnc
            {0.47519806426735, 0.50316401683903}, // Asian (6-11)
            {0.47519806426735, 0.50316401683903},
            {0.47519806426735, 0.50316401683903},
            {0.47519806426735, 0.50316401683903},
            {0.47519806426735, 0.50316401683903},
            {0.47519806426735, 0.50316401683903}
        };
        
        // Create lambda arrays
        double[][][] wrkLambda1All = {
            {whiteLambda1, whiteLambda1, whiteLambda1, whiteLambda1, whiteLambda1},
            {blackLambda1, blackLambda1, blackLambda1, blackLambda1, blackLambda1},
            {hspncLambda1, hspncLambda1, hspncLambda1, hspncLambda1, hspncLambda1},
            {otherLambda1, otherLambda1, otherLambda1, otherLambda1, otherLambda1},
            {fHspncLambda1, fHspncLambda1, fHspncLambda1, fHspncLambda1, fHspncLambda1},
            {chnesLambda1, chnesLambda1, chnesLambda1, chnesLambda1, chnesLambda1},
            {japnsLambda1, japnsLambda1, japnsLambda1, japnsLambda1, japnsLambda1},
            {filipLambda1, filipLambda1, filipLambda1, filipLambda1, filipLambda1},
            {hawaiLambda1, hawaiLambda1, hawaiLambda1, hawaiLambda1, hawaiLambda1},
            {otrPILambda1, otrPILambda1, otrPILambda1, otrPILambda1, otrPILambda1},
            {otrAsLambda1, otrAsLambda1, otrAsLambda1, otrAsLambda1, otrAsLambda1}
        };
        
        double[][][] wrkLambda2All = {
            {whiteLambda2, whiteLambda2, whiteLambda2, whiteLambda2, whiteLambda2},
            {blackLambda2, blackLambda2, blackLambda2, blackLambda2, blackLambda2},
            {hspncLambda2, hspncLambda2, hspncLambda2, hspncLambda2, hspncLambda2},
            {otherLambda2, otherLambda2, otherLambda2, otherLambda2, otherLambda2},
            {fHspncLambda2, fHspncLambda2, fHspncLambda2, fHspncLambda2, fHspncLambda2},
            {chnesLambda2, chnesLambda2, chnesLambda2, chnesLambda2, chnesLambda2},
            {japnsLambda2, japnsLambda2, japnsLambda2, japnsLambda2, japnsLambda2},
            {filipLambda2, filipLambda2, filipLambda2, filipLambda2, filipLambda2},
            {hawaiLambda2, hawaiLambda2, hawaiLambda2, hawaiLambda2, hawaiLambda2},
            {otrPILambda2, otrPILambda2, otrPILambda2, otrPILambda2, otrPILambda2},
            {otrAsLambda2, otrAsLambda2, otrAsLambda2, otrAsLambda2, otrAsLambda2}
        };
        
        // Get recoded data
        RecodeCheck.RecodeResult recodeResult = RecodeCheck.recodeCheck(data, rawInd);
        RelativeRisk.RelativeRiskResult rrResult = RelativeRisk.relativeRisk(data, rawInd);
        
        // Find valid IDs without errors
        List<Integer> validIds = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (recodeResult.errorInd[i] == 0) {
                validIds.add(i);
            }
        }
        
        // Calculate absolute risk for each valid patient
        for (int i : validIds) {
            PatientData patient = data.get(i);
            double rrStar1 = rrResult.rrStar1[i];
            double rrStar2 = rrResult.rrStar2[i];
            
            double[] oneArRr = new double[70];
            int startInterval = (int) Math.floor(patient.t1) - 20 + 1;
            int endInterval = (int) Math.ceil(patient.t2) - 20;
            int numIntervals = (int) Math.ceil(patient.t2) - (int) Math.floor(patient.t1);
            double riskWork = 0;
            double cumLambda = 0;
            
            double[] lambda1 = new double[70];
            double[] lambda2 = new double[70];
            
            int raceIndex = patient.race - 1;
            if (raceIndex < 0 || raceIndex >= wrkLambda1All.length) continue;
            
            // Flatten lambda arrays
            int idx = 0;
            for (int j = 0; j < 14; j++) {
                for (int k = 0; k < 5; k++) {
                    lambda1[idx] = wrkLambda1All[raceIndex][k][j];
                    System.out.println("lambda1:" + lambda1[idx] + "idx:" + idx);
                    lambda2[idx] = wrkLambda2All[raceIndex][k][j];
                    idx++;
                }
            }
            
            if (avgWhite == 0) {
                double oneAr1 = wrk1ARAll[raceIndex][0];
                double oneAr2 = wrk1ARAll[raceIndex][1];
                
                for (int j = 0; j < 30; j++) {
                    oneArRr[j] = oneAr1 * rrStar1;
                }
                for (int j = 30; j < 70; j++) {
                    oneArRr[j] = oneAr2 * rrStar2;
                }
            } else if (avgWhite == 1) {
                for (int j = 0; j < 70; j++) {
                    oneArRr[j] = 1.0;
                }
                
                // Use average rates for white and other races
                if (patient.race == 1 || patient.race == 4) {
                    idx = 0;
                    for (int j = 0; j < 14; j++) {
                        for (int k = 0; k < 5; k++) {
                            lambda1[idx] = whiteLambda1Avg[j];
                            lambda2[idx] = whiteLambda2Avg[j];
                            idx++;
                        }
                    }
                }
            }
            
            // Calculate risk
            for (int j = 0; j < numIntervals; j++) {
                int jInterval = startInterval + j - 1;
                double integralLength;
                
                if (numIntervals > 1) {
                    if (j == 0) {
                        integralLength = 1 - (patient.t1 - Math.floor(patient.t1));
                    } else if (j == (numIntervals - 1)) {
                        double z1 = (patient.t2 > Math.floor(patient.t2)) ? 1 : 0;
                        double z2 = (patient.t2 == Math.floor(patient.t2)) ? 1 : 0;
                        integralLength = (patient.t2 - Math.floor(patient.t2)) * z1 + z2;
                    } else {
                        integralLength = 1;
                    }
                } else {
                    integralLength = patient.t2 - patient.t1;
                }
                
                double lambdaJ = lambda1[jInterval] * oneArRr[jInterval] + lambda2[jInterval];
                System.out.println("j:" +j + " lambdaJ:" + lambdaJ);
                double piJ = ((oneArRr[jInterval] * lambda1[jInterval] / lambdaJ) * 
                             Math.exp(-cumLambda)) * (1 - Math.exp(-lambdaJ * integralLength));
                System.out.println("j:" +j + " piJ:" + piJ);
                System.out.println(":");
                riskWork += piJ;
                cumLambda += lambdaJ * integralLength;
            }
            
            absRisk[i] = 100 * riskWork;
        }
        
        return absRisk;
    }
}