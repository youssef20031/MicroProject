package com.microproject.microproject.model;
import java.util.*;

public class Man {
    static Scanner sc = new Scanner(System.in);
    static int n,m;
    static String s1, s2;
    static int memo[][];

//    public static int dp(int i, int j){
//        if(i == n || j == m)
//            return 0;
//
//        if(memo[i][j] != -1)
//            return memo[i][j];
//
//        if(s1.charAt(i) == s2.charAt(j)){
//            return memo[i][j] = 1 + Math.max(dp(i + 1, j), dp(i, j + 1));
//        }
//        return memo[i][j] = Math.max(dp(i + 1, j), dp(i, j + 1));
//    }
    static int[] values = {1, 5, 6, 9, 12};
    public static void main(String[] args){
//        s1 = sc.next();
//        s2 = sc.next();
//        n = s1.length();
//        m = s2.length();
//
//        memo = new int[n][m];
//        for(int i = 0;i < n;i++)
//            Arrays.fill(memo[i], -1);
//
//        System.out.println(dp(0, 0));
        n = sc.nextInt();
//        System.out.println(dp(n-1, n));

    }

//    public static int dp(int i, int sum){
//        if(i == -1)
//            return -1;
//        if(sum == n)
//            return 0;
//    }
}
