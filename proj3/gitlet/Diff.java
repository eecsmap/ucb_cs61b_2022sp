package gitlet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

import java.io.IOException;
import java.nio.file.InvalidPathException;

import static java.lang.Math.max;

/** A comparison of two sequences of strings.  After executing setSequences to
 *  initialize the data, methods allow computing longest common sequences
 *  and differences in the form of edits needed to convert one sequence to the
 *  next.
 *  @author P. N. Hilfinger */
public class Diff {

    /** Set the sequences currently being compared to the contents
     *  of SEQ1 and SEQ2 (as delivered by their iterators). */
    public void setSequences(Collection<String> seq1, Collection<String> seq2) {
        _lines1 = new ArrayList<>(seq1);
        _lines2 = new ArrayList<>(seq2);
        _lls = null;
        _todo = null;
    }

    /** Set the sequences currently being compared to the contents
     *  of FILE1 and FILE2.  Null Files set empty lists. */
    public void setSequences(File file1, File file2) {
        try {
            Path p1 = file1.toPath();
            _lines1 = Files.readAllLines(p1);
        } catch (IOException | InvalidPathException | NullPointerException
                 excp) {
            _lines1 = Collections.emptyList();
        }
        try {
            Path p2 = file2.toPath();
            _lines2 = Files.readAllLines(p2);
        } catch (IOException | InvalidPathException | NullPointerException
                 excp) {
            _lines2 = Collections.emptyList();
        }
        _lls = null;
        _todo = null;
    }

    /** Return the first of the current sequences. */
    public List<String> sequence1() {
        return _lines1;
    }

    /** Return the second of the current sequences. */
    public List<String> sequence2() {
        return _lines2;
    }

    /** Returns sequence1().get(K). */
    public String get1(int k) {
        return _lines1.get(k);
    }

    /** Returns sequence2().get(K). */
    public String get2(int k) {
        return _lines2.get(k);
    }

    /** Return the length of the longest subsequence of the first K1 and K2
     *  items, respectively, of the current data sequences. */
    public int lls(int k1, int k2) {
        checkData();
        if (_lls == null) {
            _lls = new int[_lines1.size() + 1][_lines2.size() + 1];
            for (int[] row : _lls) {
                Arrays.fill(row, -1);
            }
        }
        if (k1 == 0 || k2 == 0
            || k1 > _lines1.size() || k2 > _lines2.size()) {
            return 0;
        } else if (_lls[k1][k2] == -1) {
            initStack();
            push(k1, k2, 0);
            while (!empty()) {
                pop();
                if (_llsTop1 == 0 || _llsTop2 == 0) {
                    _lls[_llsTop1][_llsTop2] = 0;
                } else if (_lls[_llsTop1][_llsTop2] != -1) {
                    continue;
                } else {
                    switch (_llsState) {
                    case 0:
                        if (_lines1.get(_llsTop1 - 1)
                            .equals(_lines2.get(_llsTop2 - 1))) {
                            push(_llsTop1, _llsTop2, 4);
                            push(_llsTop1 - 1, _llsTop2 - 1, 0);
                        } else {
                            push(_llsTop1, _llsTop2, 1);
                        }
                        continue;
                    case 1:
                        push(_llsTop1, _llsTop2, 2);
                        push(_llsTop1, _llsTop2 - 1, 0);
                        continue;
                    case 2:
                        push(_llsTop1, _llsTop2, 3);
                        push(_llsTop1 - 1, _llsTop2, 0);
                        continue;
                    case 3:
                        _lls[_llsTop1][_llsTop2]
                            = max(_lls[_llsTop1][_llsTop2 - 1],
                                  _lls[_llsTop1 - 1][_llsTop2]);
                        continue;
                    case 4:
                        _lls[_llsTop1][_llsTop2]
                            = _lls[_llsTop1 - 1][_llsTop2 - 1] + 1;
                        continue;
                    default:
                        assert false;
                    }
                }
            }
        }
        return _lls[k1][k2];
    }

    /** Return the length of the longest common subsequence of the current
     *  data subsequences. */
    public int lls() {
        checkData();
        return lls(_lines1.size(), _lines2.size());
    }

    /** Return true iff the sequences currently being compared have identical
     *  content. */
    public boolean sequencesEqual() {
        checkData();
        if (_lines1.size() != _lines2.size()) {
            return false;
        }
        return lls(_lines1.size(), _lines2.size()) == _lines1.size();
    }

    /** Return an array containing the int values of the items in LIST. */
    private int[] toIntArr(Collection<Integer> list) {
        int[] result = new int[list.size()];
        int i;
        i = 0;
        for (int x : list) {
            result[i] = x;
            i += 1;
        }
        return result;
    }

    /** Return largest common subsequence of the sequences being compared as
     *  a sequence of 3n values s01, s02, L0, s11, s12, L1,..., where
     *  si1 is the starting line position of the subsequence in the
     *  first file (0-based), si2 is the starting position in the second
     *  file, and Li is the length of the subsequence. */
    public int[] commonSubsequence() {
        ArrayDeque<Integer> resultList = new ArrayDeque<>();

        int k1, k2;
        k1 = _lines1.size(); k2 = _lines2.size();
        while (k1 > 0 && k2 > 0) {
            int ls = lls(k1, k2);
            if (lls(k1 - 1, k2) == ls) {
                k1 -= 1;
            } else if (lls(k1, k2 - 1) == ls) {
                k2 -= 1;
            } else {
                int L;
                L = 1;
                while (k1 > L && k2 > L && lls(k1 - L - 1, k2 - L) != ls - L
                       && lls(k1 - L, k2 - L  - 1) != ls - L) {
                    L += 1;
                }
                resultList.offerFirst(L);
                resultList.offerFirst(k2 - L);
                resultList.offerFirst(k1 - L);
                k1 -= L; k2 -= L;
            }
        }
        return toIntArr(resultList);
    }

    /** Return the edit that converts the first of the sequences being compared
     *  to the second. This is a sequence of 4n values d0, dL0, a0, aL0, ...,
     *  where the lines [di .. di + dL0 - 1] are the ranges of line
     *  positions in the first file (0-based) of lines to be removed
     *  from that file, and the lines [ai .. ai + aL0 - 1] are the starting
     *  positions in the second file that are to replace them.  Either
     *  dLi or aLi, but not both, may be 0, indicating respectively simple
     *  removal of lines or addition of lines. */
    public int[] diffs() {
        int[] common = commonSubsequence();
        ArrayList<Integer> result = new ArrayList<>();
        int e1, e2;
        e1 = e2 = 0;
        for (int k = 0; k < common.length; k += 3) {
            if (e1 != common[k] || e2 != common[k + 1]) {
                result.add(e1);
                result.add(common[k] - e1);
                result.add(e2);
                result.add(common[k + 1] - e2);
            }
            e1 = common[k] + common[k + 2];
            e2 = common[k + 1] + common[k + 2];
        }
        if (e1 < _lines1.size() || e2 < _lines2.size()) {
            result.add(e1);
            result.add(_lines1.size() - e1);
            result.add(e2);
            result.add(_lines2.size() - e2);
        }
        return toIntArr(result);
    }

    /** Raise an exception if there are no current data sequences for
     *  comparison. */
    private void checkData() {
        if (_lines1 == null) {
            throw new IllegalStateException("no sequences specified");
        }
    }

    /** Initialize work stack for lls. */
    private void initStack() {
        _todo = new int[6 * (_lines1.size() +  _lines2.size() + 1)];
        _todop = 0;
    }

    /** Push an item on the work stack for computing lls(I1, I2). STATE
     *  indicates which inner recursive calls have happened so far for these
     *  arguments. */
    private void push(int i1, int i2, int state) {
        _todo[_todop + 1] = i1;
        _todo[_todop + 2] = i2;
        _todo[_todop] = state;
        _todop += 3;
    }

    /** Pop an item from the work stack. */
    private void pop() {
        _todop -= 3;
        _llsTop1 = _todo[_todop + 1]; _llsTop2 = _todo[_todop + 2];
        _llsState = _todo[_todop];
    }

    /** Return true iff the work stack is empty. */
    private boolean empty() {
        return _todop <= 0;
    }

    /** The sequences being compared. */
    private List<String> _lines1, _lines2;

    /** The memo table for longest common subsequence.  _lls[a][b] contains the
     *  length of the longest common sequence of the prefixes of the current
     *  data sequences of lengths a and b, or -1 if not calculated. */
    private int[][] _lls;

    /** A stack structure used by lls. */
    private int[] _todo;
    /** Structure for the work queue used by lls. */
    private int _todop, _llsState, _llsTop1, _llsTop2;

}
