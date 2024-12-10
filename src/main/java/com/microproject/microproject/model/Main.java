package com.microproject.microproject.model;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



public class Main {

    /*
     * Complete the 'maximizeTotalMemoryPoints' function below.
     *
     * The function is expected to return a LONG_INTEGER.
     * The function accepts INTEGER_ARRAY memory as parameter.
     */

    public static long getDataDependenceSum(long n) {
        HashSet<Long> hs = new HashSet<>();
        long sum = 0;
        for(long i = 1 ; i <= Math.sqrt(n) + 1;i++){
            if(!hs.contains(n/i)){
                hs.add(n / i);
                sum += n / i;
            }

            if(i > 1)
                for(long j = n;j >= 1;j /= i){
                    if(!hs.contains(n/j)){
                        sum += n/j;
                        hs.add(n/j);
                    }
                }
        }



        return sum;

    }

    public static void main(String[] args){

        System.out.println(getDataDependenceSum(5));
    }

}

