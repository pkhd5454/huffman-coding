package com.huffman;

import java.util.Objects;

public class Run implements Comparable<Run> {
  public byte symbol;
  public int runLen;
  public int freq;
  public Run left;
  public Run right;
  public int codeword;
  public int codewordLen;

  public Run() {}

  public Run(byte symbol, int runLen) {
    this.symbol = symbol;
    this.runLen = runLen;
    freq = 1;
  }

  @Override
  public int compareTo(Run other) {
    return freq - other.freq;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Run run = (Run) o;
    return symbol == run.symbol && runLen == run.runLen;
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, runLen);
  }

  @Override
  public String toString() {
    return "[symbol=" + symbol + ", runLen=" + runLen + ", freq=" + freq + "]";
  }
}
