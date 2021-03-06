/*
 * Copyright 2017 Tim Fisken
 *
 * This file is part of ted-zs-scala.
 *
 * ted-zs-scala is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * ted-zs-scala is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the  GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with ted-zs-scala.  If
 * not, see <http://www.gnu.org/licenses/>
 */

package tedzs

import org.scalatest.FunSpec

/**
  * Tests for general utilities related to manipulating and traversing Nodes
  */
class NodeSpec extends FunSpec {
  describe("A node") {
    describe("postOrder") {
      it("should return just the root for a tree with no children") {
        val tree = SimpleNode("1", Seq())
        assert(tree.postOrder == Vector(tree))
      }

      it("should return children before parents for a two-level tree") {
        val b = SimpleNode("b", Seq())
        val c = SimpleNode("c", Seq())
        val a = SimpleNode("a", Seq(b, c))
        assert(a.postOrder.map(_.label) == Vector("b", "c", "a"))
      }

      it("should return post-order for a multi-level tree") {
        val d = SimpleNode("d", Seq())
        val e = SimpleNode("e", Seq())
        val f = SimpleNode("f", Seq())
        val g = SimpleNode("g", Seq())
        val b = SimpleNode("b", Seq(d, e))
        val c = SimpleNode("c", Seq(f, g))
        val a = SimpleNode("a", Seq(b, c))

        assert(a.postOrder.map(_.label) == Vector("d", "e", "b", "f", "g", "c", "a"))
      }
    }

    describe("postOrderPaths") {
      it("should return a single element path for a single-element tree") {
        val tree = SimpleNode("a", Seq())
        assert(tree.postOrderPaths == Vector(Seq(tree)))
      }

      it("should return a one-element path for child nodes in a two-level tree") {
        val b = SimpleNode("b", Seq())
        val c = SimpleNode("c", Seq())
        val a = SimpleNode("a", Seq(b, c))

        assert(a.postOrderPaths.map(_.tail) == Vector(Seq(a), Seq(a), Seq()))
      }

      it("should return two-element paths for child nodes in a three-level tree") {
        val c = SimpleNode("c", Seq())
        val b = SimpleNode("b", Seq(c))
        val a = SimpleNode("a", Seq(b))

        assert(a.postOrderPaths.map(_.map(_.label)) ==
          Vector(Seq("c", "b", "a"), Seq("b", "a"), Seq("a")))
      }
    }

    describe("leftMostDescendants") {
      it("should return the root for a one-element tree") {
        val tree = SimpleNode("a", Seq())

        assert(tree.leftMostDescendants == Vector(0))
      }

      it("should return the left-most node for a two-level tree") {
        val b = SimpleNode("b", Seq())
        val c = SimpleNode("c", Seq())
        val a = SimpleNode("a", Seq(b, c))

        assert(a.leftMostDescendants == Vector(0, 1, 0))
      }

      it("should return the left-most node for a three-level tree") {
        val c = SimpleNode("c", Seq())
        val b = SimpleNode("b", Seq(c))
        val a = SimpleNode("a", Seq(b))

        assert(a.leftMostDescendants == Vector(0, 0, 0))
      }
    }

    describe("keyroots") {
      it("should return the root for a one-node tree") {
        val tree = SimpleNode("a", Seq())

        assert(tree.keyroots == Vector(0))
      }

      it("should return the root and the right siblings for a two-level tree") {
        val b = SimpleNode("b", Seq())
        val c = SimpleNode("c", Seq())
        val a = SimpleNode("a", Seq(b, c))

        assert(a.keyroots == Seq(1, 2))
      }

      it("should return all the right siblings in a two-level tree") {
        val b = SimpleNode("b", Seq())
        val c = SimpleNode("c", Seq())
        val d = SimpleNode("d", Seq())
        val a = SimpleNode("a", Seq(b, c, d))

        assert(a.keyroots == Seq(1, 2, 3))
      }

      it("should return all the right siblings in a three-level tree") {
        val tree = SimpleNode("a", Seq(
          SimpleNode("b", Seq(
            SimpleNode("e", Seq()),
            SimpleNode("f", Seq())
          )),
          SimpleNode("c", Seq(SimpleNode("g", Seq()))),
          SimpleNode("d", Seq())
        ))

        assert(tree.keyroots == Seq(1, 4, 5, 6))
      }
    }
  }
}
