package main;

import utils.io.InputReader;
import utils.io.OutputWriter;

public class LetItFlow {
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
