package edu.stanstate.degreeplanner.utils;

import java.time.LocalTime;

public final class TimeUtil {
  private TimeUtil() {}

  public static boolean overlaps(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
    return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
  }

  public static boolean daysOverlap(String daysA, String daysB) {
    for (char c : daysA.toCharArray()) {
      if (daysB.indexOf(c) >= 0) return true;
    }
    return false;
  }
}