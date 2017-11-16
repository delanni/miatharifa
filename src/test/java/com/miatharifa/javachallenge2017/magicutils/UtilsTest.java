package com.miatharifa.javachallenge2017.magicutils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class UtilsTest {

    @Test
    public void generateIncreasingElementSizes(){
        for(int i = 3; i < 19; i++){
            // T ~= i^2
            measureBucketCalculation(i, 3);
        }
    }

    @Test
    public void generateIncreasingBucketSizes(){
        for(int i = 1; i < 10; i++){
            measureBucketCalculation(15, i);
        }
    }

    @Test
    public void testBucketGeneration() {
        Collection<Integer[]> allBucketSizes = measureBucketCalculation(3, 3);
        org.junit.Assert.assertEquals(allBucketSizes.size(), 10);
    }

    @Test
    public void testMediumBucketGeneration() {
        Collection<Integer[]> allBucketSizes = measureBucketCalculation(30, 3);
    }

    @Test
    public void testRealisticBucketGeneration() {
        Collection<Integer[]> allBucketSizes = measureBucketCalculation(10 , 10, 1);
    }

    @Test
    public void testBucketWithMinimumCriteria(){
        Collection<Integer[]> allBucketSizes = measureBucketCalculation(12, 5, 5);
        System.out.println(allBucketSizes.stream().map(Arrays::toString).collect(Collectors.toList()));
    }

    private Collection<Integer[]> measureBucketCalculation(int numberOfElements, int numberOfBuckets) {
        return measureBucketCalculation(numberOfElements, numberOfBuckets, 0);
    }

    private Collection<Integer[]> measureBucketCalculation(int numberOfElements, int numberOfBuckets, int minimum) {
        long before = System.currentTimeMillis();
        Collection<Integer[]> allBucketSizes = Utils.getAllBucketSizes(numberOfBuckets, numberOfElements, minimum);
        long after = System.currentTimeMillis();

        System.out.println(numberOfElements + " into " + numberOfBuckets + " took " + (after-before) + "ms -> " + allBucketSizes.size());
        return allBucketSizes;
    }
}