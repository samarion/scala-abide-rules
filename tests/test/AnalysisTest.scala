package scala.tools.abide.test.rules

import org.scalatest._

import scala.tools.abide._
import scala.tools.abide.util._
import scala.tools.abide.traversal._
import scala.tools.abide.directives._

import scala.reflect.internal.traversal._

trait AnalysisTest extends FlatSpec with Matchers with TreeProvider {

  val context = new Context(global) with MutabilityChecker

  def apply(rule : Traversal with TraversalRule)(tree : global.Tree) : List[rule.Warning] = {
    rule.traverse(tree.asInstanceOf[rule.universe.Tree])
    rule.state.warnings
  }
}
