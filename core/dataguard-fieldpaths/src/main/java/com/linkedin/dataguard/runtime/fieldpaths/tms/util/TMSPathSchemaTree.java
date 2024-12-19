package com.linkedin.dataguard.runtime.fieldpaths.tms.util;

import com.linkedin.dataguard.runtime.fieldpaths.tms.ParsedTMSPath;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.UnionTypeSelector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.util.TMSPathUtils.*;


/**
 * This class represents the schema tree for a dataset, constructed from a list of
 * TMS field paths representing the schema of the dataset.
 *
 * The schema tree helps to obtain accurate canonical forms of TMS paths, which can be used
 * for comparison across different schemas (field path lists).
 */
public class TMSPathSchemaTree {
  private final TreeNode _root;

  /**
   * Constructs the tree from a list of field paths.
   *
   * @param schemaFields List of TMS field paths to represent the tree.
   */
  public TMSPathSchemaTree(List<String> schemaFields) {
    _root = buildSchemaTree(schemaFields);
    _root.pruneSimpleUnion();
  }

  /**
   * Returns the canonical form for a given TMS field path. In the canonical form,
   * we ignore the simple union selector for nullable fields and simplify type names
   * for array element, map key, and map value to standard names.
   *
   * @param fieldPath the field path to check
   */
  public String getCanonicalPath(String fieldPath) {
    ParsedTMSPath parsedPath = parseTMSPath(fieldPath);
    TreeNode node = _root.findPath(parsedPath, 0);
    if (node != null) {
      LinkedList<TMSPathSelector> pathComponents = new LinkedList<>();
      while (node != null) {
        if (node._nodeSelector != null) {
          pathComponents.addFirst(node._nodeSelector);
        }
        node = node._parent;
      }
      return toCanonicalTMSPath(pathComponents);
    }
    return null;
  }

  /**
   * Returns the field path in the original schema fields.
   *
   * @param fieldPath field path to check, which is expected, but not necessary,
   *                  in the canonical form.
   */
  public String getOriginalPath(String fieldPath) {
    ParsedTMSPath parsedPath = parseTMSPath(fieldPath);
    TreeNode node = _root.findPath(parsedPath, 0);
    return node != null ? node._rawPath : null;
  }

  private TreeNode buildSchemaTree(List<String> schemaFields) {
    TreeNode rootNode = new TreeNode(null, null);
    for (String rawPath : schemaFields) {
      ParsedTMSPath parsedPath = parseTMSPath(rawPath);
      rootNode.addPath(rawPath, parsedPath, 0);
    }
    return rootNode;
  }

  private static class TreeNode {
    private final TMSPathSelector _nodeSelector;
    private TreeNode _parent;
    private final List<TreeNode> _unionChildren;
    private final List<TreeNode> _normalChildren;
    private String _rawPath;

    TreeNode(TMSPathSelector nodeSelector, TreeNode parent) {
      _nodeSelector = nodeSelector;
      _parent = parent;
      _unionChildren = new ArrayList<>();
      _normalChildren = new ArrayList<>();
      _rawPath = null;
    }

    void addPath(String rawPath, ParsedTMSPath parsedPath, int index) {
      if (index >= parsedPath.getTMSPathSelectors().size()) {
        _rawPath = rawPath;
        return;
      }

      TMSPathSelector nodeSelector = parsedPath.getTMSPathSelectors().get(index);
      if (nodeSelector instanceof UnionTypeSelector) {
        addChildIfNotExist(_unionChildren, nodeSelector).addPath(rawPath, parsedPath, index + 1);
      } else {
        addChildIfNotExist(_normalChildren, nodeSelector).addPath(rawPath, parsedPath, index + 1);
      }
    }

    TreeNode findPath(ParsedTMSPath parsedPath, int index) {
      if (index == parsedPath.getTMSPathSelectors().size()) {
        return this;
      }

      TMSPathSelector nodeSelector = parsedPath.getTMSPathSelectors().get(index);
      TreeNode child = findChildNode(nodeSelector);

      if (child != null) {
        return child.findPath(parsedPath, index + 1);
      }

      if (nodeSelector instanceof UnionTypeSelector) {
        return findPath(parsedPath, index + 1);
      }
      return null;
    }

    void pruneSimpleUnion() {
      if (_unionChildren.size() == 1) {
        // Prune the simple union node by moving all of its normal children to
        // the normal children of the parent and update parent pointers of related
        // node accordingly.
        TreeNode unionNode = _unionChildren.get(0);
        if (unionNode._normalChildren.isEmpty()) {
          _rawPath = unionNode._rawPath;
        } else {
          for (TreeNode childOfUnion : unionNode._normalChildren) {
            childOfUnion._parent = this;
          }
          _normalChildren.addAll(unionNode._normalChildren);
        }
        _unionChildren.clear();
      }

      for (TreeNode childNode : _unionChildren) {
        childNode.pruneSimpleUnion();
      }

      for (TreeNode childNode : _normalChildren) {
        childNode.pruneSimpleUnion();
      }
    }

    private TreeNode addChildIfNotExist(List<TreeNode> children, TMSPathSelector childSelector) {
      TreeNode child = findChildNodeFromList(childSelector, children);
      if (child == null) {
        child = new TreeNode(childSelector, this);
        children.add(child);
      }

      return child;
    }

    private TreeNode findChildNode(TMSPathSelector nodeSelector) {
      TreeNode child = findChildNodeFromList(nodeSelector, _normalChildren);
      if (child == null) {
        child = findChildNodeFromList(nodeSelector, _unionChildren);
      }
      return child;
    }

    private TreeNode findChildNodeFromList(TMSPathSelector nodeSelector, List<TreeNode> children) {
      for (TreeNode node : children) {
        if (isSameSelector(node._nodeSelector, nodeSelector)) {
          return node;
        }
      }
      return null;
    }
  }
}
