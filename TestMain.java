import java.util.ArrayList;
import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        // 构造患者数据（仅填入原始变量）
        PatientData patient = new PatientData();
        patient.race =2;         // 6 = Chinese
        patient.age = 45;         // 年龄
        patient.ageMen = 10;      // 初潮年龄
        patient.age1st = 20;      // 首次生育年龄
        patient.nRels = 1;        // 一等亲中乳腺癌人数
        patient.nBiop = 1;        // 活检次数
        patient.hypPlas = 0;      // 有无非典型增生
        patient.t1 = 45.2;        // 起始年龄
        patient.t2 = 53.3;        // 截止年龄

        

        List<PatientData> dataList = new ArrayList<>();
        dataList.add(patient);

        // 自动转码：生成分类字段
         // 调用后 patient 中的 nbCat, amCat 等会被自动设置
        // 调用 recodeCheck
        RecodeCheck.RecodeResult recodeResult = RecodeCheck.recodeCheck(dataList, 1);

        // 回写 recode 字段到 patient
        patient.nbCat = recodeResult.nbCat[0];
        patient.amCat = recodeResult.amCat[0];
        patient.afCat = recodeResult.afCat[0];
        patient.nrCat = recodeResult.nrCat[0];
        patient.rHyp = recodeResult.rHyp[0];
        patient.charRace = recodeResult.charRace[0];


        // 相对风险
        RelativeRisk.RelativeRiskResult rr = RelativeRisk.relativeRisk(dataList, 1);
        System.out.println("RR (age < 50): " + rr.rrStar1[0]);
        System.out.println("RR (age >= 50): " + rr.rrStar2[0]);

        // 绝对风险
        double[] absRisk = absolute.absoluteRisk2(dataList, 1, 0);
        System.out.println("Absolute Risk: " + absRisk[0]);

        // 风险总结
        //RiskSummary.RiskTable table = RiskSummary.riskSummary(dataList, 1);
        //System.out.println("Summary t1: " + table.t1[0]);
        //System.out.println("Summary t2: " + table.t2[0]);

        System.out.println("nb=" + patient.nbCat + ", am=" + patient.amCat + ", af=" + patient.afCat + ", nr=" + patient.nrCat);

    }
}
