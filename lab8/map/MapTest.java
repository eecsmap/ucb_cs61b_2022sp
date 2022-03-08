package map;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class MapTest {

    private final static long SEED = 0x61b;
    private final static int TEST_MAP_SIZE = 15_000;
    private final static int NUM_TRIALS = 10;

    /**
     * Tests the functionality of a SimpleMap for a set of key-values pairs
     * containing 5 elements. This test also tests duplicate keys, to make sure
     * that updates are handled accordingly.
     */
    private void smallTestMap(SimpleMap<Integer, Integer> otherMap) {
        int[] keys = {2,5,3,1,4,1};
        int[] values = {1,5,4,3,4,2};
        java.util.TreeMap<Integer, Integer> solnMap = new java.util.TreeMap<>();
        for (int i = 0; i < keys.length; i += 1) {
            otherMap.put(keys[i], values[i]);
            solnMap.put(keys[i], values[i]);
        }
        for (Integer i : solnMap.keySet()) {
            Integer otherResult = otherMap.get(i);
            Integer solnResult = solnMap.get(i);
            assertEquals("Incorrect value contained in map.", solnResult, otherResult);
        }
    }

    /**
     * Tests the functionality of a SimpleMap for a set of key-value pairs
     * through randomly inserting TEST_MAP_SIZE (by default this is 15,000)
     * keys and checking to make sure that all random key's match their random
     * values.
    */
    private void fuzzTestMap(SimpleMap<Integer, Integer> otherMap) {
        Random r = new Random(SEED);
        java.util.TreeMap<Integer, Integer> solnMap = new java.util.TreeMap<>();
        for (int i = 0; i < TEST_MAP_SIZE; i += 1) {
            int key = r.nextInt();
            int val = r.nextInt();
            otherMap.put(key, val);
            solnMap.put(key, val);
        }
        for (Integer i : solnMap.keySet()) {
            Integer otherResult = otherMap.get(i);
            Integer solnResult = solnMap.get(i);
            assertEquals("Incorrect value contained in map.", solnResult, otherResult);
        }
    }

    /**
     * This test is nearly identical to the fuzzTestMap function above, but
     * this test also compares the performance to that of the
     * java.util.TreeMap. In order to pass this test, otherMap must run within
     * a 10x multiple of the java.util.TreeMap's runtime. There are NUM_TRIALS
     * trials (by default this is 10), and otherMap only needs to have one
     * trial faster than 10 times the time for the java.util.TreeMap.
     */
    private void timedFuzzTestMap(SimpleMap<Integer, Integer> otherMap) {
        java.util.TreeMap<Integer, Integer> solnMap = new java.util.TreeMap<>();
        Random r = new Random(SEED);
        double minRatio = Double.MAX_VALUE;
        for (int trialNum = 0; trialNum < NUM_TRIALS; trialNum += 1) {
            solnMap.clear();
            otherMap.clear();
            for (int i = 0; i < TEST_MAP_SIZE; i += 1) {
                int key = r.nextInt();
                int val = r.nextInt();
                otherMap.put(key, val);
                solnMap.put(key, val);
            }


            for (Integer i : solnMap.keySet()) {
                Integer otherResult = otherMap.get(i);
                Integer solnResult = solnMap.get(i);
                assertEquals("Incorrect value contained in map.", solnResult, otherResult);
            }

            double solnTime = 0;
            double otherTime = 0;
            timing.Timer t = new timing.Timer();
            for (Integer i : solnMap.keySet()) {
                t.start();
                Integer otherResult = otherMap.get(i);
                otherTime += t.elapsed();
                t.start();
                Integer solnResult = solnMap.get(i);
                solnTime += t.elapsed();
                assertEquals("Incorrect value contained in map.", solnResult, otherResult);
            }

            if (otherTime / solnTime < minRatio) {
                minRatio = otherTime /solnTime;
            }
            System.out.println("otherMap ran " + otherTime / solnTime + "x the speed as java.util.TreeMap");
        }
        assertTrue("otherMap must be at most 10 times slower than Java's TreeMap.\n" +
                "Smallest ratio obtained was: " + minRatio, minRatio < 10);
    }

    @Test
    public void smallTestLinkedListMap() {
        smallTestMap(new LinkedListMap<>());
    }

    @Test
    public void smallTestTreeMap() {
        smallTestMap(new TreeMap<>());
    }

    @Test
    public void fuzzTestLinkedListMap() {
        fuzzTestMap(new LinkedListMap<>());
    }

    @Test
    public void fuzzTestTreeMap() {
        fuzzTestMap(new TreeMap<>());
    }

    @Test
    public void timedFuzzTestTreeMap() {
        timedFuzzTestMap(new TreeMap<>());
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MapTest.class));
    }
}
