public class PatientData {
    // 基本字段
    public String id;
    public int race;            // 种族
    public int age;             // 年龄
    public int ageMen;          // 初潮年龄
    public int age1st;          // 首次生育年龄
    public int nRels;           // 一等亲中乳腺癌患者人数
    public int nBiop;           // 乳腺活检次数
    public int hypPlas;         // 非典型增生（1 是，0 否）
    public double t1;           // 起始年龄（或评估开始时间）
    public double t2;           // 截止年龄（或预测区间结束时间）

    // RecodeCheck 使用的中间字段（用于字符串分类）
    public String nbCat;        // 生育年龄类别
    public String amCat;        // 初潮年龄类别
    public String afCat;        // 首次生育年龄类别
    public String nrCat;        // 一等亲乳腺癌人数类别
    public String rHyp;         // 高血压类别（可能是字符串）
    public String charRace;     // 种族描述字符串
}
