package com.epi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.epi.utils.Pair;

public class LongestContainedRange {
  private static int checkAns(List<Integer> A) {
    Collections.sort(A);
    int res = 1;
    int pre = A.get(0), len = 1;
    for (int i = 1; i < A.size(); ++i) {
      if (A.get(i).equals(pre + 1)) {
        ++len;
      } else if (!A.get(i).equals(pre)) {
        res = Math.max(res, len);
        len = 1;
      }
      pre = A.get(i);
    }
    res = Math.max(res, len);
    System.out.println(res);
    return res;
  }

  private static int findLongestContainedRangeInt(List<Integer> A) {
    if (A.isEmpty()) {
      return 0;
    }

    Set<Integer> t = new HashSet<Integer>(); // records the unique appearance.
    // L[i] stores the upper range for i.
    Map<Integer, Integer> L = new HashMap<Integer, Integer>();
    // U[i] stores the lower range for i.
    Map<Integer, Integer> U = new HashMap<Integer, Integer>();
    for (int i = 0; i < A.size(); ++i) {
      if (t.add(A.get(i))) {
        L.put(A.get(i), A.get(i));
        U.put(A.get(i), A.get(i));
        // Merges with the interval starting on A[i] + 1.
        if (L.containsKey(A.get(i) + 1)) {
          L.put(U.get(A.get(i)), L.get(A.get(i) + 1));
          U.put(L.get(A.get(i) + 1), U.get(A.get(i)));
          L.remove(A.get(i) + 1);
          U.remove(A.get(i));
        }
        // Merges with the interval ending on A[i] - 1.
        if (U.containsKey(A.get(i) - 1)) {
          U.put(L.get(A.get(i)), U.get(A.get(i) - 1));
          L.put(U.get(A.get(i) - 1), L.get(A.get(i)));
          U.remove(A.get(i) - 1);
          L.remove(A.get(i));
        }
      }
    }

    Map.Entry<Integer, Integer> m = Collections.max(L.entrySet(),
        new Comparator<Map.Entry<Integer, Integer>>() {
          @Override
          public int compare(Map.Entry<Integer, Integer> o1,
              Map.Entry<Integer, Integer> o2) {
            return Integer.valueOf(o1.getValue() - o1.getKey()).compareTo(
                o2.getValue() - o2.getKey());
          }
        });
    return m.getValue() - m.getKey() + 1;
  }

  // @include
  public static Pair<Integer, Integer> 
      findLongestContainedRange(List<Integer> A) {
    // S records the existence of each entry in A.
    Set<Integer> S = new HashSet<Integer>();
    for (int a : A) {
      S.add(a);
    }

    int maxLen = 0;
    Pair<Integer, Integer> maxRange = new Pair<Integer, Integer>(0, -1);
    // L stores the longest length ending at each A[i].
    Map<Integer, Integer> L = new HashMap<Integer, Integer>();
    for (int a : A) {
      int len = longestRangeLen(a, S, L);
      if (len > maxLen) {
        maxLen = len;
        maxRange = new Pair<Integer, Integer>(a - len + 1, a);
      }
    }
    return maxRange;
  }

  private static int longestRangeLen(int a, Set<Integer> S,
      Map<Integer, Integer> L) {
    // Base case. If a does not exist.
    if (!S.contains(a)) {
      return 0;
    }

    if (!L.containsKey(a)) {
      L.put(a, longestRangeLen(a - 1, S, L) + 1);
    }
    return L.get(a);
  }

  // @exclude

  public static void main(String[] args) {
    Random r = new Random();
    for (int times = 0; times < 1000; ++times) {
      int n;
      if (args.length == 1) {
        n = Integer.parseInt(args[0]);
      } else {
        n = r.nextInt(10001);
      }
      List<Integer> A = new ArrayList<Integer>();
      for (int i = 0; i < n; ++i) {
        A.add(r.nextInt(n + 1));
      }

      assert (findLongestContainedRangeInt(A) == checkAns(A));
      Pair<Integer, Integer> res = findLongestContainedRange(A);
      System.out.println(res);
      assert (res.getSecond() - res.getFirst() + 1 == findLongestContainedRangeInt(A));
    }
  }
}
