package xlite.sql;

import org.apache.commons.io.FilenameUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.util.Arrays;

public class JsonSqlEngine {
    private final File jsonDir;
    private SparkSession spark;

    public JsonSqlEngine(File jsonDir) {
        this.jsonDir = jsonDir;
    }

    public void init() {
        File[] files = jsonDir.listFiles((dir, name) -> FilenameUtils.getExtension(name).equals("xjson"));
        spark = SparkSession.builder()
            .appName("xlite-json")
            .master("local")
            .getOrCreate();

        Arrays.stream(files).forEach(file -> {
            Dataset<Row> df = spark.read().json(file.getAbsolutePath());
            String tableName = FilenameUtils.getBaseName(file.getName()).toLowerCase();
            df.createOrReplaceTempView(tableName);
        });
    }

    public void sql(String sql) {
        spark.sql(sql).show();
    }
}
