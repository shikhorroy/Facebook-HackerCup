import java.io.OutputStream;
import java.io.FilenameFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.io.BufferedWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author Roy
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream;
        try {
            final String regex = "tourist.*[.]txt";
            File directory = new File(".");
            File[] candidates = directory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.matches(regex);
                }
            });
            File toRun = null;
            for (File candidate : candidates) {
                if (toRun == null || candidate.lastModified() > toRun.lastModified())
                    toRun = candidate;
            }
            inputStream = new FileInputStream(toRun);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream("tourist.out");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        Tourist solver = new Tourist();
        int testCount = Integer.parseInt(in.next());
        for (int i = 1; i <= testCount; i++)
            solver.solve(i, in, out);
        out.close();
    }

    static class Tourist {
        public void solve(int testNumber, InputReader reader, OutputWriter writer) {
            int n = reader.readInteger();
            int k = reader.readInteger();
            long v = reader.readLong();

            List<String> list = new ArrayList<>();
            PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>();
            for (int i = 0; i < n; i++) {
                list.add(reader.readLine());
                pq.add(Pair.of(0, i));
            }

            int totCount = n;
            if (n % k == 0) totCount = n / k;
            List<List<String>> store = new ArrayList<>();
            for (int i = 0; i < totCount; i++) {
                List<String> strings = new ArrayList<>();
                List<Pair<Integer, Integer>> pairs = new ArrayList<>();
                for (int j = 0; j < k; j++) {
                    int value = pq.peek().key;
                    int indexes = pq.peek().value;
                    strings.add(list.get(indexes));
                    pairs.add(Pair.of(value + 1, indexes));
                    pq.poll();
                }
                store.add(strings);
                for (int j = 0; j < k; j++) pq.add(pairs.get(j));
            }

            int index = (int) (v % totCount) - 1;
            if (index == -1) index = totCount - 1;

            writer.print("Case #" + testNumber + ":");
            for (int j = 0; j < n; j++)
                if (store.get(index).contains(list.get(j)))
                    writer.print(" " + list.get(j));
            writer.printLine();
        }

    }

    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private InputReader.SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);

                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0) {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int readInteger() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String readString() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public String readLine() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = read();
            } while (!isNewlineChar(c));
            return res.toString();
        }

        public long readLong() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            long res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String next() {
            return readString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public boolean isNewlineChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return c == '\n' || c == -1;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }

    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void print(Object... objects) {
            for (int i = 0; i < objects.length; i++) {
                if (i != 0) {
                    writer.print(' ');
                }
                writer.print(objects[i]);
            }
        }

        public void printLine(Object... objects) {
            this.print(objects);
            writer.println();
        }

        public void close() {
            writer.flush();
            writer.close();
        }

    }

    static class Pair<K, V> implements Comparable<Pair> {
        public K key;
        public V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public static <K, V> Pair of(K key, V value) {
            return new Pair(key, value);
        }


        public String toString() {
            return "(" + key + ", " + value + ")";
        }


        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + (key != null ? key.hashCode() : 0);
            hash = 31 * hash + (value != null ? value.hashCode() : 0);
            return hash;
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Pair) {
                Pair pair = (Pair) o;
                if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
                if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
                return true;
            }
            return false;
        }


        public int compareTo(Pair o) {
            return (this.key.hashCode() < o.key.hashCode()) ? -1 :
                    (this.key.hashCode() > o.key.hashCode()) ? 1 :
                            (this.value.hashCode() < o.value.hashCode()) ? -1 :
                                    (this.value.hashCode() > o.value.hashCode()) ? 1 : 0;
        }

    }
}

