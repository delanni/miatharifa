package com.miatharifa.javachallenge2017.magicutils;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static List<Integer[]> getAllBucketSizes(int numberOfBuckets, int numberOfElements) {
        return getAllBucketSizes(numberOfBuckets, numberOfElements, 0);
    }

    public static List<Integer[]> getAllBucketSizes(int numberOfBuckets, int numberOfElements, int minBucketSize) {
        Generator<Integer> generator = Factory.createCompositionGenerator(numberOfElements);
        List<Integer[]> partitions = new ArrayList<>();
        for (ICombinatoricsVector<Integer> composition : generator) {
            if (composition.getSize() <= numberOfBuckets) {
                if (composition.getVector().stream().allMatch(x -> x >= minBucketSize)) {
                    List<Integer[]> compositionsWithZeros = injectZeros(composition.getVector().toArray(new Integer[composition.getSize()]), numberOfBuckets - composition.getSize());
                    partitions.addAll(compositionsWithZeros);
                }
            }
        }
        return slowDistinct(partitions);
    }

    private static List<Integer[]> slowDistinct(List<Integer[]> partitions) {
        Map<String, Integer[]> hash = partitions.stream().collect(Collectors.toMap(Arrays::toString, y->y, (integers, integers2) -> integers));
        return new ArrayList<>(hash.values());
    }


    public static List<Integer[]> injectZeros(Integer[] baseArray, int zeroCount) {
        if (zeroCount == 0) {
            List<Integer[]> l = new ArrayList<>();
            l.add(baseArray);
            return l;
        }
        List<Integer[]> integers = injectZeros(baseArray);
        if (zeroCount == 1) {
            return integers;
        } else {
            return integers.stream().map(x -> injectZeros(x, zeroCount - 1)).reduce(new ArrayList<>(), (integers1, integers2) -> {
                integers1.addAll(integers2);
                return integers1;
            });
        }
    }

    public static List<Integer[]> injectZeros(Integer[] baseArray) {
        int newArrayLengths = baseArray.length + 1;
        List<Integer[]> out = new ArrayList<>();
        for (int i = 0; i < newArrayLengths; i++) {
            if (i < baseArray.length && baseArray[i] == 0) continue;
            Integer[] iArr = new Integer[newArrayLengths];
            for (int j = 0; j < newArrayLengths; j++) {
                iArr[j] = i == j ? 0 : j > i ? baseArray[j - 1] : baseArray[j];
            }
            out.add(iArr);
        }
        return out;
    }
}
