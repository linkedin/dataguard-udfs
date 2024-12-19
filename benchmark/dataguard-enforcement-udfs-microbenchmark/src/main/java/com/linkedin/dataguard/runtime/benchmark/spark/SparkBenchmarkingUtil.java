package com.linkedin.dataguard.runtime.benchmark.spark;

import com.linkedin.dataguard.runtime.benchmark.common.BenchmarkingUtils;
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkTypeDataProvider;
import com.linkedin.dataguard.runtime.udf.spark.RedactFieldIfUDF;
import com.linkedin.dataguard.runtime.udf.spark.RedactSecondarySchemaFieldIfUDF;
import com.linkedin.transport.api.types.StdStructType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.spark.sql.catalyst.expressions.BoundReference;
import org.apache.spark.sql.catalyst.expressions.CaseWhen;
import org.apache.spark.sql.catalyst.expressions.CreateArray;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.catalyst.expressions.LessThan;
import org.apache.spark.sql.catalyst.expressions.Literal;
import org.apache.spark.sql.catalyst.expressions.Rand;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import scala.Some;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.Tuple2;


/**
 * Spark-specific utilities for micro-benchmarking
 */
public final class SparkBenchmarkingUtil {

  private SparkBenchmarkingUtil() {
  }

  public static List<Expression> createExpressions(BenchmarkingUtils.EnforcementParams enforcementParams, double percent) {
    return createExpressions(enforcementParams, percent, false);
  }

  /**
   * Creates UDF Expression objects, which can later be invoked with `eval` functions during benchmarking
   *
   * @param enforcementParams fieldpath and type information for redactions
   * @param percent % of redactions to be invoked
   * @param useCaseWhen use case statements instead of UDFs. Only works if the columns are top-level.
   * @return list of expressions to be invoked during benchmarking
   */
  public static List<Expression> createExpressions(BenchmarkingUtils.EnforcementParams enforcementParams, double percent, boolean useCaseWhen) {
    String typeSignature = enforcementParams.getTableSchemaTypeSignature();
    Map<String, List<String>> pathsPerColumn = enforcementParams.getFieldPathsForColumns();
    List<Expression> expressions = new ArrayList<>();

    StdStructType tableSchemaType = (StdStructType) new NullableSparkTypeDataProvider().getStdFactory().createStdType(typeSignature);

    Map<String, Integer> columnIndexes = IntStream.range(0, tableSchemaType.fieldNames().size())
        .boxed() // Convert int stream to Integer stream
        .collect(Collectors.toMap(i -> tableSchemaType.fieldNames().get(i), i -> i));

    for (Map.Entry<String, List<String>> column : pathsPerColumn.entrySet()) {
      String columnName = column.getKey();
      int columnIndex = columnIndexes.get(columnName);

      Expression columnExpression = new BoundReference(
          columnIndex,
          (DataType) tableSchemaType.fieldTypes().get(columnIndex).underlyingType(),
          true);

      for (int i = 0; i < column.getValue().size(); i++) {
        Expression conditionExpression;
        if (!useCaseWhen) {
          conditionExpression = getConditionExpression(percent);
          String path = column.getValue().get(i);
          Expression pathExpression = Literal.create(path, DataTypes.StringType);

          if (path.startsWith("$")) {
            // Secondary schema field
            columnExpression = getSecondarySchemaRedaction(conditionExpression, columnExpression, pathExpression, columnName);
          } else {
            // Primary schema field
            columnExpression = getPrimarySchemaFieldRedaction(conditionExpression, columnExpression, pathExpression);
          }
        } else {
          columnExpression = getCaseExpressionRedaction(percent, column.getValue().get(i), columnExpression);
        }
      }

      expressions.add(columnExpression);
    }

    return expressions;
  }

  private static Expression getCaseExpressionRedaction(double percent, String path, Expression columnExpression) {
    if (path.contains(".") || (!doubleEquals(percent, 0.0) && !doubleEquals(percent, 1.0))) {
      throw new RuntimeException("Cannot use case when for nested redaction");
    }

    Expression conditionExpression = Literal.create(false, DataTypes.BooleanType);
    if (doubleEquals(percent, 0.0)) {
      conditionExpression = Literal.create(true, DataTypes.BooleanType);
    }

    return new CaseWhen(
        JavaConverters.asScalaBufferConverter(Arrays.asList(
            new Tuple2<>(conditionExpression, columnExpression)
        )).asScala().toSeq(),
        new Some<>(Literal.create(null, columnExpression.dataType())));
  }

  private static Expression getPrimarySchemaFieldRedaction(Expression conditionExpression, Expression columnExpression,
      Expression pathExpression) {
    List<Expression> expressionList = Arrays.asList(conditionExpression, columnExpression, pathExpression,
        Literal.create(null, DataTypes.NullType));
    Seq<Expression> udfParameters = JavaConverters.asScalaBufferConverter(expressionList).asScala().toSeq();
    return new RedactFieldIfUDF(udfParameters);
  }

  private static Expression getSecondarySchemaRedaction(Expression conditionExpression, Expression columnExpression,
      Expression pathExpression, String columnName) {
    List<Expression> expressionList = Arrays.asList(conditionExpression, columnExpression,
        new CreateArray(
            JavaConverters.asScalaBufferConverter(Arrays.asList(pathExpression)).asScala().toSeq()),
        Literal.create(columnName, DataTypes.StringType),
        Literal.create("UNKNOWN", DataTypes.StringType));
    return new RedactSecondarySchemaFieldIfUDF(
        JavaConverters.asScalaBufferConverter(expressionList).asScala().toSeq());
  }

  private static Expression getConditionExpression(double percent) {
    Expression conditionExpression;
    if (doubleEquals(percent, 0.0)) {
      conditionExpression = Literal.create(false, DataTypes.BooleanType);
    } else if (doubleEquals(percent, 1.0)) {
      conditionExpression = Literal.create(true, DataTypes.BooleanType);
    } else {
      Rand randExpression = new Rand(Literal.create(0, DataTypes.IntegerType));
      randExpression.initialize(0);
      conditionExpression = new LessThan(randExpression, Literal.create(percent, DataTypes.DoubleType));
    }
    return conditionExpression;
  }

  private static boolean doubleEquals(double a, double b) {
    double epsilon = 1e-9;
    return Math.abs(a - b) < epsilon;
  }
}
