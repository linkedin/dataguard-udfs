package com.linkedin.dataguard.runtime.fieldpaths.virtual;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.transport.java.JavaBoundVariables;
import com.linkedin.dataguard.runtime.transport.java.JavaFactory;
import com.linkedin.dataguard.runtime.transport.java.JavaTypeDataProvider;
import com.linkedin.dataguard.runtime.transport.java.types.JavaTypeInfo;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.functions.FunctionRepository;
import com.linkedin.transport.api.types.StdType;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.transport.java.JavaTypeSystem.*;
import static com.linkedin.dataguard.runtime.transport.java.types.JavaTypeInfo.JavaClassType.*;
import static org.junit.jupiter.api.Assertions.*;


public class TestFunctionResolution {

  @Test
  public void testFunctionResolution() {
    JavaFactory factory = new JavaFactory(new JavaBoundVariables());
    JavaTypeDataProvider typeDataProvider = new JavaTypeDataProvider();
    TypedFunctionInfo actual =
        FunctionRepository.getTypedFunctionInfo(
            typeDataProvider,
            "getFDSIndex1D",
            ImmutableList.of(
                factory.createStdType("array(varchar)"),
                factory.createStdType("varchar")));

    assertEquals(actual.getFunctionName(), "getFDSIndex1D");
    assertEquals(actual.getArgumentTypes().stream().map(StdType::underlyingType).collect(Collectors.toList()),
        ImmutableList.of(
            new JavaTypeInfo(JAVA_LIST, ImmutableList.of(new JavaTypeInfo(JAVA_STRING)), ImmutableList.of(JAVA_ELEMENT_NAME)),
            new JavaTypeInfo(JAVA_STRING)));
    assertEquals(actual.getReturnType().underlyingType(),
        new JavaTypeInfo(JAVA_INTEGER));
  }
}
