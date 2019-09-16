package util;

import com.aliyun.odps.Column;
import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import mr.Android_BatteryRealTimeDataCollectPartReadOnlyData;
import mr.scooter.Scooter_track_clearupInfo;

import java.util.LinkedHashMap;

public class ToolUtil {
    public  static JobConf runJob(String[] args,Class clazz){
        {
            String[] inputs = null;
            String[] outputs = null;
            if (args.length == 2) {
                inputs = args[0].split(",");
                outputs = args[1].split(",");

            } else {
                System.err.println("MultipleInOut in... out...");
                System.exit(1);
            }
            JobConf job = new JobConf();
            job.setMapperClass(clazz);
            job.setNumReduceTasks(0);

            for (String in : inputs) {
                String[] ss = in.split("\\|");
                if (ss.length == 1) {
                    InputUtils.addTable(TableInfo.builder().tableName(ss[0]).build(), job);
                } else if (ss.length == 2) {
                    LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                    InputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
                } else {
                    System.err.println("Style of input: " + in + " is not right");
                    System.exit(1);
                }
            }
            for (String out : outputs) {
                String[] ss = out.split("\\|");
                if (ss.length == 1) {
                    OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).build(), job);
                } else if (ss.length == 2) {
                    LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                    OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
                } else if (ss.length == 3) {
                    if (ss[1].isEmpty()) {
                        LinkedHashMap<String, String> map = convertPartSpecToMap(ss[2]);
                        OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
                    } else {
                        LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                        OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map)
                                .label(ss[2]).build(), job);
                    }
                } else {
                    System.err.println("Style of output: " + out + " is not right");
                    System.exit(1);
                }
            }
            return job;

        }
    }
    public  static JobConf runJob(String[] args, Class clazz1,Class clazz2, Column[] col1,Column[] col2){
        {
            String[] inputs = null;
            String[] outputs = null;
            if (args.length == 2) {
                inputs = args[0].split(",");
                outputs = args[1].split(",");
            } else {
                System.err.println("MultipleInOut in... out...");
                System.exit(1);
            }
            JobConf job = new JobConf();
            job.setMapperClass(clazz1);
            job.setReducerClass(clazz2);
            job.setMapOutputKeySchema(col1);
            job.setMapOutputValueSchema(col2);

            for (String in : inputs) {
                String[] ss = in.split("\\|");
                if (ss.length == 1) {
                    InputUtils.addTable(TableInfo.builder().tableName(ss[0]).build(), job);
                } else if (ss.length == 2) {
                    LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                    InputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
                } else {
                    System.err.println("Style of input: " + in + " is not right");
                    System.exit(1);
                }
            }
            for (String out : outputs) {
                String[] ss = out.split("\\|");
                if (ss.length == 1) {
                    OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).build(), job);
                } else if (ss.length == 2) {
                    LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                    OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
                } else if (ss.length == 3) {
                    if (ss[1].isEmpty()) {
                        LinkedHashMap<String, String> map = convertPartSpecToMap(ss[2]);
                        OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
                    } else {
                        LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                        OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map)
                                .label(ss[2]).build(), job);
                    }
                } else {
                    System.err.println("Style of output: " + out + " is not right");
                    System.exit(1);
                }
            }
            return job;

        }
    }
    public static LinkedHashMap<String, String> convertPartSpecToMap(
            String partSpec) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if (partSpec != null && !partSpec.trim().isEmpty()) {
            String[] parts = partSpec.split("/");
            for (String part : parts) {
                String[] ss = part.split("=");
                if (ss.length != 2) {
                    throw new RuntimeException("ODPS-0730001: error part spec format: "
                            + partSpec);
                }
                map.put(ss[0], ss[1]);
            }
        }
        return map;
    }
}
