package com.huffman;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class HuffmanApplication {
  private List<Run> runs = new ArrayList<>();

  public static void main(String[] args) {
    HuffmanApplication app = new HuffmanApplication();
    RandomAccessFile fIn;
    try {
      fIn = new RandomAccessFile("test.jpg", "r");
      app.collectRuns(fIn);
      fIn.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 데이터 파일 fIn에 등장하는 모든 run들과 각각의 등장횟수를 count하여 ArrayList runs에 저장한다. */
  private void collectRuns(RandomAccessFile fIn) throws IOException {
    Byte prev = null;
    Byte curr;
    int length = 1;
    while (fIn.getFilePointer() != fIn.length()) {
      curr = fIn.readByte();
      if (prev != null) {
        if (prev == curr) {
          length++;
        } else {
          Run run = new Run(prev, length);
          if (runs.contains(run)) {
            runs.get(runs.indexOf(run)).freq++;
          } else {
            runs.add(run);
          }
          length = 1;
        }
      }
      prev = curr;
    }
    fIn.seek(0);
    System.out.println(runs);
  }
}
