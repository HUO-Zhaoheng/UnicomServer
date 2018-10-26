import com.sun.deploy.util.ArrayUtil;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.*;
import weka.core.pmml.jaxbbindings.Header;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class GDXX {//工单信息

    public GDXX(String ZY, String YYWLLL, String CC, String DQMC, String SBJF,String WYMC, String GZLB,  int[] errorTypeNo) {
        this.ZY = ZY;
        this.YYWLLL = YYWLLL;
        this.CC = CC;
        this.DQMC = DQMC;
        this.SBJF = new HashSet<>();
        this.SBJF.add(SBJF);
        this.WYMC = new HashSet<>();
        this.WYMC.add(WYMC);
        this.GZLB = GZLB;
        this.errorTypeNo = errorTypeNo;
    }

    String ZY;
    String YYWLLL;
    String CC;
    String DQMC;
    HashSet<String> SBJF;
    HashSet<String> WYMC;
    String GZLB;
    int[] errorTypeNo;
}

public class Compress {

    static final String RootPath = System.getProperty("user.dir");
    static final String inputPath = "\\resources\\casualty_for_compression.csv";
    static final String outputPath = "\\resources\\CombinedResult.csv";

    public static void main(String[] args) throws IOException {

        //合并同一工单下的告警
        combineRecordEntry();

        //删去CSV里面莫名其妙出现的双引号
        deleteDoubleQuote();


    }


    private static void combineRecordEntry() throws IOException {
        HashMap<String, GDXX> GDH_GDXX = new HashMap<String, GDXX>();
        final int errorTypeNum = 83;
        String[] inputHeaders = new String[]{"专业", "一级网络类型", "工单号", "派单状态", "工单状态", "告警指纹FP0",
                "城市", "地区名称", "设备机房", "网元名称", "设备类型", "告警标题", "告警发生时间", "告警对象名称", "告警对象设备类型", "网管告警级别",
                "告警发现时间", "告警清除状态", "告警清除时间", "设备工程状态", "告警工程状态", "故障类别", "故障原因"};

        String[] outputHeaders = new String[]{"工单号", "专业", "一级网络类型", "城市", "地区名称", "设备机房", "网元名称", "故障类别"};
        try (CSVParser csvParser = CSVParser.parse(new File(RootPath + inputPath), StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader(inputHeaders).withSkipHeaderRecord())) {
            int CasualtyNum = 0;
            LinkedHashMap<String, Integer> UniqleNoOfCasualty = new LinkedHashMap<>();
            System.out.print("CSV Successfully read");
            List<CSVRecord> records = csvParser.getRecords();
            for (CSVRecord csvRecord : records) {
                String GDH = csvRecord.get("工单号"),
                        ZY = csvRecord.get("专业"),
                        YYWLLL = csvRecord.get("一级网络类型"),
                        CC = csvRecord.get("城市"),
                        DQMC = csvRecord.get("地区名称"),
                        SBJF = csvRecord.get("设备机房"),
                        SBLX = csvRecord.get("设备类型"),
                        //载频的PA温度严重告警(载频级告警,超过95度)这个告警标题带逗号，手动替换成分号
                        GJBT = csvRecord.get("告警标题").replace(",", ";"),
                        WYMC = csvRecord.get("网元名称"),
                        GZLB = csvRecord.get("故障类别");
                if (!UniqleNoOfCasualty.containsKey(SBLX + "+" + GJBT)) {
                    UniqleNoOfCasualty.put(SBLX + "+" + GJBT, CasualtyNum++);
                }
                int BH = UniqleNoOfCasualty.get(SBLX + "+" + GJBT);
                //不存在工单，添加工单
                if (!GDH_GDXX.containsKey(GDH)) {
                    int[] errorTypeNo = new int[errorTypeNum];
                    errorTypeNo[BH]++;
                    GDH_GDXX.put(GDH, new GDXX(ZY, YYWLLL, CC, DQMC, SBJF, WYMC, GZLB, errorTypeNo));

                } else {//存在工单，将该告警添加到该工单下
                    //if (!GDH_GDXX.get(GDH).WYMC.contains(WYMC)) {//新算法语句
                        GDH_GDXX.get(GDH).WYMC.add(WYMC);
                        GDH_GDXX.get(GDH).errorTypeNo[BH] += 1;
                    //}
                }
                //观察是不是有新的机房，如果有则添加
                if (!GDH_GDXX.get(GDH).SBJF.contains(SBJF))
                    GDH_GDXX.get(GDH).SBJF.add(SBJF);
            }

            FileWriter fileWriter = new FileWriter(RootPath + outputPath);
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);
            //UTF-8
            csvPrinter.print(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            //输出首行
            csvPrinter.printRecord(ArrayUtils.addAll(outputHeaders, UniqleNoOfCasualty.keySet().toArray()));
            //输出Records
            for (Map.Entry<String, GDXX> record : GDH_GDXX.entrySet()) {
                GDXX gdxx = record.getValue();
                csvPrinter.printRecord(record.getKey(), gdxx.ZY, gdxx.YYWLLL, gdxx.CC, gdxx.DQMC, gdxx.SBJF.toString().replace(",", ";"),
                        gdxx.WYMC.toString().replace(",", ";"), gdxx.GZLB, arrayToStrWithComma(gdxx.errorTypeNo));
                csvPrinter.flush();
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    //不知道为什么，导出的CSV带双引号，此处手动将双引号删除
    private static void deleteDoubleQuote() throws IOException {
        File file = new File(RootPath + outputPath);
        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);
        CharArrayWriter tempStream = new CharArrayWriter();

        String line = null;

        while ((line = bufIn.readLine()) != null) {
            line = line.replaceAll("\"", "");
            tempStream.write(line);
            tempStream.append(System.getProperty("line.separator"));
        }

        bufIn.close();

        FileWriter out = new FileWriter(file);
        tempStream.writeTo(out);
        out.close();
    }

    private static String arrayToStrWithComma(int[] needChange) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < needChange.length; i++) {
            sb.append(needChange[i]);
            if ((i + 1) != needChange.length) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}