package test.rice.concisegen;

import main.rice.concisegen.ConciseSetGenerator;
import main.rice.obj.*;
import main.rice.test.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the ConciseSetGenerator class.
 */
class ConciseSetGeneratorTest {

    /**
     * Tests setCover() in the case that all tests are needed to get coverage.
     */
    @Test
    void testNeedAll() {
        // Generate ten integer test cases
        List<TestCase> allCases = generateIntegerCases(10);

        // Construct a situation where all cases in allCases are needed and check results
        testNeedAllHelper(allCases);
    }

    /**
     * Tests setCover() in the case that only one test case is needed (i.e., all wrong
     * files failed on the given test) when not all files are wrong.
     */
    @Test
    void testNeedOneSomeWrong() {
        // Odd-numbered files are wrong
        Set<Integer> wrongSet = Set.of(1, 3, 5, 7, 9);

        // Create ten integer test cases
        List<TestCase> allCases = generateIntegerCases(10);

        // Tests 0-8 each catch one file
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0) {
                // Only odd files are wrong, so shift even tests' failed files by one
                caseToFiles.add(Set.of(i + 1));
            } else {
                caseToFiles.add(Set.of(i));
            }
        }

        // The last test case catches all wrong files (and thus is the only that should be
        // chosen by setCover())
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 != 0) {
                set.add(i);
            }
        }
        caseToFiles.add(set);

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        List<APyObj> elem = Collections.singletonList(new PyIntObj(9));
        Set<TestCase> expected = Set.of(new TestCase(elem));
        assertEquals(expected, actual);
    }

    /**
     * Tests setCover() in the case that each file fails one different test, but not all
     * tests are needed (number of tests exceeds number of files).
     */
    @Test
    void testEachFileFailsOneTest() {
        // Every file is wrong
        Set<Integer> wrongSet = generateAllWrong(5);

        // Create ten integer test cases
        List<TestCase> allCases = generateIntegerCases(10);

        // Tests 0-8 each catch one file
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            if (i % 2 == 0) {
                // Even test catch the file at i/2
                caseToFiles.add(Set.of(i / 2));
            } else {
                // Odd tests don't catch anything
                caseToFiles.add(Set.of());
            }
        }

        // Expect even-numbered tests to be selected
        Set<TestCase> expected = new HashSet<>();
        for (int i = 0; i <= 9; i += 2) {
            List<APyObj> elem = Collections.singletonList(new PyIntObj(i));
            expected.add(new TestCase(elem));
        }

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        assertEquals(expected, actual);
    }

    /**
     * Tests that setCover() does the right thing when there is overlap between tests,
     * i.e. multiple tests cover the same files.
     */
    @Test
    void testOverlap() {
        List<TestCase> allCases = generateIntegerCases(8);

        // Generate expected results, tailored to what will happen in testOverlapHelper
        Set<TestCase> expected = new HashSet<>();
        for (int i = 0; i < 8; i += 3) {
            expected.add(new TestCase(Collections.singletonList(new PyIntObj(i))));
        }

        testOverlapHelper(allCases, expected);
    }

    /**
     * Helper function which generates an allCases list containing integers from 0 to
     * numTests - 1, inclusive.
     *
     * @param numTests the number of tests to generate
     * @return a list of test cases, each consisting of a single integer
     */
    private static List<TestCase> generateIntegerCases(int numTests) {
        List<TestCase> allCases = new ArrayList<>();
        for (int i = 0; i < numTests; i++) {
            List<APyObj> elem = Collections.singletonList(new PyIntObj(i));
            allCases.add(new TestCase(elem));
        }
        return allCases;
    }

    /**
     * Helper function which generates a wrongSet containing all numbers from 0 to
     * numFiles.
     *
     * @param numFiles the number of files that were tested
     * @return the set of each integer from 0 to numFiles - 1, inclusive
     */
    private static Set<Integer> generateAllWrong(int numFiles) {
        Set<Integer> wrongSet = new HashSet<>();
        for (int i = 0; i < numFiles; i++) {
            wrongSet.add(i);
        }
        return wrongSet;
    }

    /**
     * Helper function which generates a simple caseToFile list mapping the i-th test case
     * to the set containing the i-th file.
     *
     * @param numTests number of test cases
     * @return simple caseToFile list that was generated
     */
    private static List<Set<Integer>> generateSimpleCaseToFile(int numTests) {
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < numTests; i++) {
            Set<Integer> set = new HashSet<>();
            set.add(i);
            caseToFiles.add(set);
        }
        return caseToFiles;
    }

    /**
     * Helper function for testing the case where all tests in allCases are needed for
     * coverage.
     *
     * @param allCases the test cases
     */
    private static void testNeedAllHelper(List<TestCase> allCases) {
        // Every file is wrong
        Set<Integer> wrongSet = generateAllWrong(10);

        // Generate expected results: need every test case
        Set<TestCase> expected = new HashSet<>(allCases);

        // Ten files: each file fails a different test
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 9; i > -1; i--) {
            caseToFiles.add(Set.of(i));
        }

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        assertEquals(expected, actual);
    }

    /**
     * Helper function for testing the case where there is overlap between the test cases
     * (one case catches a subset of the files caught by another case).
     *
     * @param allCases the test cases
     */
    private static void testOverlapHelper(List<TestCase> allCases, Set<TestCase> expected) {
        // Generate caseToFiles list that looks somewhat random, but is carefully
        // constructed to include sets of varying size and moderate overlap between sets.
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Set<Integer> set = new HashSet<>();
            set.add(i);

            if (i % 2 == 0) {
                set.add(i + 1);
                set.add(i * 2);
            }
            if (i % 3 == 0) {
                set.add(i + 1);
                set.add(i + 2);
                set.add(i * 3);
            }
            caseToFiles.add(set);
        }
        caseToFiles.get(2).add(1);
        caseToFiles.get(3).add(13);
        caseToFiles.get(6).add(4);

        // Make sure the wrongSet includes everything that appeared in caseToFiles above
        Set<Integer> wrongSet = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            wrongSet.add(i);
            if (i % 2 == 0) {
                wrongSet.add(i * 2);
            }
            if (i % 3 == 0) {
                wrongSet.add(i * 3);
            }
        }
        wrongSet.add(13);

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        assertEquals(expected, actual);
    }
}
