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
            final String regex = "ethan.*traverses.*a.*tree.*[.]txt";
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
            outputStream = new FileOutputStream("ethantraversesatree.out");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        EthanTraversesATree solver = new EthanTraversesATree();
        int testCount = Integer.parseInt(in.next());
        for (int i = 1; i <= testCount; i++)
            solver.solve(i, in, out);
        out.close();
    }

    static class EthanTraversesATree {
        public void solve(int testNumber, InputReader reader, OutputWriter writer) {
            int n = reader.readInteger();
            int k = reader.readInteger();
            Node[] nodes = new Node[n + 1];

            for (int i = 1; i <= n; i++) nodes[i] = new Node(i);

            for (int i = 1; i <= n; i++) {
                int left = reader.readInteger();
                int right = reader.readInteger();

                if (left > 0) nodes[i].left = nodes[left];
                if (right > 0) nodes[i].right = nodes[right];
            }

            Node node = new Node();
            node.preOrderTraverse(nodes[1]);
            List<Integer> preOrder = node.getOrderList();

            node = new Node();
            node.postOrderTraverse(nodes[1]);
            List<Integer> postOrder = node.getOrderList();

//        System.out.println(preOrder);
//        System.out.println(postOrder);
            int map[] = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                int x = preOrder.get(i - 1);
                int y = postOrder.get(i - 1);
                map[x] = y;
            }

            boolean[] flag = new boolean[n + 1];

            int label = 0;
            int L[] = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                int x = i;
                if (!flag[x] && label + 1 <= k) ++label;
                while (!flag[x]) {
                    L[x] = label;
                    flag[x] = true;
                    x = map[x];
                }
            }

            if (label == k) {
                writer.print("Case #" + testNumber + ":");
                for (int i = 1; i <= n; i++) writer.print(" " + L[i]);
                writer.print("\n");
            } else writer.print("Case #" + testNumber + ": Impossible\n");
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

        public void close() {
            writer.flush();
            writer.close();
        }

    }

    static class Node {
        int key;
        Node left;
        Node right;
        List<Integer> orderList = new ArrayList<>();

        public List<Integer> getOrderList() {
            return orderList;
        }

        public Node() {
        }

        public Node(int item) {
            key = item;
            left = right = null;
        }

        void postOrderTraverse(Node node) {
            if (node == null)
                return;

            postOrderTraverse(node.left);
            postOrderTraverse(node.right);
            orderList.add(node.key);
        }

        void preOrderTraverse(Node node) {
            if (node == null)
                return;
            orderList.add(node.key);

            preOrderTraverse(node.left);
            preOrderTraverse(node.right);
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

