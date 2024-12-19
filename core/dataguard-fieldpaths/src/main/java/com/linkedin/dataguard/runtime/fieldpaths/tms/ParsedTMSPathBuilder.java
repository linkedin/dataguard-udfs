package com.linkedin.dataguard.runtime.fieldpaths.tms;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.ArrayElementSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.MapKeySelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.MapValueSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.StructFieldSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.UnionTypeSelector;
import org.antlr.v4.runtime.ParserRuleContext;

import static java.util.Objects.*;


public final class ParsedTMSPathBuilder {

  private ParsedTMSPathBuilder() {
  }

  public static ParsedTMSPath buildTMSPath(ParserRuleContext context) {
    ImmutableList.Builder<TMSPathSelector> pathSelectorBuilder = ImmutableList.builder();
    context.accept(new Visitor(pathSelectorBuilder));
    return new ParsedTMSPath(pathSelectorBuilder.build());
  }

  private static class Visitor extends TMSPathBaseVisitor<Void> {

    private final ImmutableList.Builder<TMSPathSelector> pathSelectors;

    Visitor(ImmutableList.Builder<TMSPathSelector> pathSelectors) {
      this.pathSelectors = requireNonNull(pathSelectors);
    }

    @Override
    public Void visitUnionSelectorRef(TMSPathParser.UnionSelectorRefContext ctx) {
      visit(ctx.fieldPath());
      pathSelectors.add(new UnionTypeSelector(ctx.typeSelector().typeName().getText()));
      return null;
    }

    @Override
    public Void visitFieldSelector(TMSPathParser.FieldSelectorContext ctx) {
      pathSelectors.add(new StructFieldSelector(ctx.fieldName().getText()));
      return null;
    }

    @Override
    public Void visitTypeSelector(TMSPathParser.TypeSelectorContext ctx) {
      pathSelectors.add(new ArrayElementSelector(ctx.typeName().getText()));
      return null;
    }

    @Override
    public Void visitKeySelector(TMSPathParser.KeySelectorContext ctx) {
      pathSelectors.add(new MapKeySelector(ctx.typeName().getText()));
      return null;
    }

    @Override
    public Void visitValueSelector(TMSPathParser.ValueSelectorContext ctx) {
      pathSelectors.add(new MapValueSelector(ctx.typeName().getText()));
      return null;
    }
  }
}