package org.jmhsieh.structs;

public class Pair<L, R> {
  private final L left;
  private final R right;

  final public R getRight() {
    return right;
  }

  final public L getLeft() {
    return left;
  }

  public Pair(final L left, final R right) {
    this.left = left;
    this.right = right;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final boolean equals(Object o) {
    if (!(o instanceof Pair))
      return false;

    Pair<L, R> other = (Pair<L, R>) o;

    if (getLeft() != null && !getLeft().equals(other.getLeft())) {
      return false;
    } else if (getLeft() == null && other.getLeft() != null) {
      return false;
    } else if (getRight() != null && !getRight().equals(other.getRight())) {
      return false;
    } else if (getRight() == null && other.getRight() != null) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public int hashCode() {
    int hLeft = getLeft() == null ? 0 : getLeft().hashCode();
    int hRight = getRight() == null ? 0 : getRight().hashCode();

    return hLeft + (57 * hRight);
  }

  @Override
  public String toString() {
    return "<" + getLeft() + ", " + getRight() + ">";
  }
}
