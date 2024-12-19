package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

/**
 * A wrapper on top of FieldPathNode that allows pointer based
 * comparison between fieldPathNodes without putting any constraint
 * on equals/hashcode implementation for different implementations
 * of FieldPathNode. This is useful for associating various information
 * with specific nodes in a syntax tree. (e.g. types for two @s could be
 * different)
 *
 * An alternative to this would be mirroring structures for every node
 * in the syntax tree with additional metadata, but the current approach
 * seems more lightweight, since this intermediate structure anyway needs
 * to be morphed into operators afterwards.
 */
public class NodeAddress {
  private final FieldPathNode fieldPathNode;

  public NodeAddress(FieldPathNode fieldPathNode) {
    this.fieldPathNode = fieldPathNode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NodeAddress that = (NodeAddress) o;
    return fieldPathNode == that.fieldPathNode;
  }

  @Override
  public int hashCode() {
    // TODO: optimize
    return 0;
  }
}
