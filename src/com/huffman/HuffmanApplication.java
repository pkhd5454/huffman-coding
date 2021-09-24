package com.huffman;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class HuffmanApplication {
  public List<Run> runs = new ArrayList<>();
  public PriorityQueue<Run> heap;
  public Run root;
  public HashMap<Run, Run> hashMap = new HashMap<>();

  public static void main(String[] args) {
    HuffmanApplication app = new HuffmanApplication();
    RandomAccessFile fIn;
    try {
      fIn = new RandomAccessFile("test.jpg", "r");
      app.compressFile("test.jpg", fIn);
      fIn.close();
    } catch (IOException e) {
      System.err.println("can't open");
    }
  }

  public void compressFile(String inFileName, RandomAccessFile fIn) throws IOException {
    String outFileName = inFileName + ".z";
    RandomAccessFile fOut = new RandomAccessFile(outFileName, "rw");
    collectRuns(fIn);
    outputFrequencies(fIn, fOut);
    createHuffmanTree();
    assignCodewords(root, 0, 0);
    storeRuns(root);
    fIn.seek(0);
    encode(fIn, fOut);
  }

  public void encode(RandomAccessFile fIn, RandomAccessFile fOut) {

  }

  /** 압축파일에 run들의 정보를 기록한다. */
  public void outputFrequencies(RandomAccessFile fIn, RandomAccessFile fOut) throws IOException {
    fOut.writeInt(runs.size());
    fOut.writeLong(fIn.getFilePointer());
    for (Run r : runs) {
      fOut.write(r.symbol);
      fOut.writeInt(r.runLen);
      fOut.writeInt(r.freq);
    }
  }

  /** 트리의 리프노드를 해시맵에 저장한다. */
  public void storeRuns(Run p) {
    if (p.left == null && p.right == null) {
      hashMap.put(p, p);
    } else {
      storeRuns(p.left);
      storeRuns(p.right);
    }
  }

  /** 트리의 리프 노드에 codeword를 부여한다. */
  public void assignCodewords(Run p, int codeword, int length) {
    if (p.left == null && p.right == null) {
      p.codeword = codeword;
      p.codewordLen = length;
    } else {
      assignCodewords(p.left, codeword << 1, length + 1);
      assignCodewords(p.right, (codeword << 1) + 1, length + 1);
    }
  }

  /** 허프만 트리 만들기 */
  public void createHuffmanTree() {
    heap = new PriorityQueue<>();
    for (Run run : runs) {
      heap.add(run);
    }

    while (heap.size() > 1) {
      Run left = heap.poll();
      Run right = heap.poll();
      Run newRun = new Run();
      newRun.left = left;
      newRun.right = right;
      newRun.freq = left.freq + right.freq;
      heap.add(newRun);
    }
    root = heap.peek();
  }

  /** 허프만 트리 출력 */
  public void printHuffmanTree() {
    preOrderTraverse(root, 0);
  }

  public void preOrderTraverse(Run node, int depth) {
    for (int i = 0; i < depth; i++) System.out.print(" ");
    if (node == null) {
      System.out.println("null");
    } else {
      System.out.println(node.toString());
      preOrderTraverse(node.left, depth + 1);
      preOrderTraverse(node.right, depth + 1);
    }
  }

  /** 데이터 파일 fIn에 등장하는 모든 run들과 각각의 등장횟수를 count하여 ArrayList runs에 저장한다. */
  public void collectRuns(RandomAccessFile fIn) throws IOException {
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
  }
}
