package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
    printSet("x => true : ", x => true)
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(1)

    printSet("s1: ", s1)
    printSet("s2: ", s2)
    printSet("s3: ", s3)
    printSet("s4: ", s4)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      printSet("s -> union(s1, s2) :", s)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains same elements of each set"){
    new TestSets {
      val s = intersect(s1, s2)
      val x = intersect(s1, s4)

      printSet("s -> intersect(s1, s2) : ", s)
      printSet("x -> intersect(s1, s4) : ", x)

      assert(!contains(s,1), "Intersect 1")
      assert(contains(x,1), "Intersect2 1")
    }
  }

  test("diff contains only one set's elements "){
    new TestSets {
      val unionSet = union(s1, s2)
      val difSet = diff(s1, unionSet)
      val difSet2 = diff(s3, unionSet)

      printSet("x -> (union(s1, s2)) : ", unionSet)
      printSet("difset -> diff(s1, unionSet) : ", difSet)
      printSet("difset2 -> diff(s3, unionSet) : ", difSet2)

      assert(!contains(difSet,1), "Diff 1")
      assert(contains(difSet2,3), "Diff 2")

    }
  }

  test("filter should returns the subset of `s` for which `p` holds. "){
    new TestSets {
      val unionSet = union(s1, s2)
      val filterSet = filter(s1, unionSet)

      printSet("unionSet -> (union(s1, s2)) : ", unionSet)
      printSet("filterSet1 -> filter(s1, unionSet) : ", filterSet)

      assert(contains(filterSet,1),"filter 1")
      assert(!contains(filterSet,2),"filter 2")
      assert(!contains(filterSet,3),"filter 3")

    }
  }

  test("a new test set instead of singleton set"){
    new TestSets {

      val bound1000 = boundSet(1000)

      assert(contains(bound1000, 997))
      assert(contains(bound1000, -997))
      assert(!contains(bound1000, -1997))
    }
  }
  // def forall(s: Set, p: Int => Boolean): Boolean = {
  test("forall should return whether all bounded integers within `s` satisfy `p`."){
    new TestSets {

      printSet("x => x%2 == 0 : ", x => x%2 == 0)
      printSet("x => x%3 == 0 : ", x => x%3 == 0)
      printSet("x => x%6 == 0 : ", x => x%6 == 0)

      assert(!forall(x => x%2 == 0, x => x%3 == 0 ))
      assert(forall(x => x%6 == 0, x => x%3 == 0 ))
    }
  }

  test("exists should return whether there exists a bounded integer within `s` that satisfies `p`."){
    new TestSets {

      printSet("x => x%2 == 0 : ", x => x%2 == 0)
      printSet("x => x%3 == 0 : ", x => x%3 == 0)
      printSet("x => x%6 == 0 : ", x => x%6 == 0)

      assert(!exists(x => x%2 == 0, x => x%3 == 0 ))
      assert(exists(x => x%6 == 0, x => x%3 == 0 ))
    }
  }

  test("map should return a set transformed by applying `f` to each element of `s`."){
    new TestSets {

      val mod2 : Int => Boolean = x => x%2 == 0

      assert(contains(map(mod2,x => x*1), 6))
      assert(!contains(map(mod2,x => x*1), 7))
    }
  }
}
