import java.io.OutputStream;
import java.io.FilenameFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;
import java.io.IOException;
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
            final String regex = "let.*it.*flow.*[.]txt";
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
            outputStream = new FileOutputStream("letitflow.out");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        LetItFlow solver = new LetItFlow();
        int testCount = Integer.parseInt(in.next());
        for (int i = 1; i <= testCount; i++)
            solver.solve(i, in, out);
        out.close();
    }

    static class LetItFlow {
        final int ROW_MAX = 3;
        final int MOD = 1000000007;

        public void solve(int testNumber, InputReader reader, OutputWriter writer) {
            int n = reader.readInteger();
            String[] house = new String[ROW_MAX];

            for (int i = 0; i < ROW_MAX; i++) {
                house[i] = reader.readString();
            }
            if ((n & 1) == 1 || //~ if n is not an even number then ans is 0
                    (house[0].charAt(0) == '#' || house[1].charAt(0) == '#' ||
                            house[1].charAt(n - 1) == '#' || house[2].charAt(n - 1) == '#')) {
                writer.printLine("Case #" + testNumber + ": " + 0);
            } else {
                int ans = 1;
                for (int j = 1; j < n - 1; j += 2) {
                    if (house[1].charAt(j) == '#' || house[1].charAt(j + 1) == '#' || //~ block found in middle row then ans  is 0
                            ((house[0].charAt(j) == '#' || house[0].charAt(j + 1) == '#') &&
                                    (house[2].charAt(j) == '#' || house[2].charAt(j + 1) == '#'))) {
                        writer.printLine("Case #" + testNumber + ": " + 0);
                        return;
                    }
                    if (house[0].charAt(j) == '.' && house[0].charAt(j + 1) == '.' &&
                            house[2].charAt(j) == '.' && house[2].charAt(j + 1) == '.') {
                        ans = ((ans % MOD) << 1) % MOD;
                    }
                }
                writer.printLine("Case #" + testNumber + ": " + ans);
            }
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

        public String next() {
            return readString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }
}

