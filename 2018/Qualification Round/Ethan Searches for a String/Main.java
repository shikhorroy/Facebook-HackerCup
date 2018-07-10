import java.io.OutputStream;
import java.io.FilenameFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.InputMismatchException;
import java.io.IOException;
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
            final String regex = "ethan.*searches.*for.*a.*string.*[.]txt";
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
            outputStream = new FileOutputStream("ethansearchesforastring.out");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        EthanSearchesForAString solver = new EthanSearchesForAString();
        int testCount = Integer.parseInt(in.next());
        for (int i = 1; i <= testCount; i++)
            solver.solve(i, in, out);
        out.close();
    }

    static class EthanSearchesForAString {
        public void solve(int testNumber, InputReader reader, OutputWriter writer) {
            String a = reader.readLine();

            int startAt = 0, endAt = 0;
            for (int i = 1, sz = a.length() - 1; i < sz; i++) {
                int start = 0, end = 0;
                for (int j = i; j <= sz; j++) {
                    if (a.charAt(start++) != a.charAt(j)) {
                        end = j;
                        break;
                    }
                }
                if (end != 0 && i != end && end <= sz) {
                    startAt = i;
                    endAt = end;
                    break;
                }
            }
            writer.print("Case #" + testNumber + ": ");
            if (startAt == 0 && endAt == 0) writer.printLine("Impossible");
            else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < endAt; i++) {
                    sb.append(a.charAt(i));
                }
                for (int i = endAt - startAt, sz = a.length(); i < sz; i++) {
                    sb.append(a.charAt(i));
                }
                writer.printLine(sb);
                System.out.println("-> " + check(a, sb.toString()));
            }
        }

        boolean check(String a, String b) {
            int i, j;
            i = j = 1;
            while (true) {
                if (i > a.length()) {
                    return true;
                }
                if (j > b.length()) {
                    return false;
                }
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    i++;
                    j++;
                } else if (i == 1) {
                    j++;
                } else {
                    i = 1;
                }
            }
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
}

