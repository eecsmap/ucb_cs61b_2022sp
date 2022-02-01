package image;

/** General utilities for hw2.image.Rescaler.
 *  @author Josh Hug
 */
public class Utils {
    /** Returns the image with the given FILENAME as an energy matrix. */
    public static double[][] toEnergyMatrix(String filename) {
        Picture inputImg = new Picture(filename);
        Rescaler ir = new Rescaler(inputImg);
        return ir.energyMatrix();
    }

    /** Dumps matrix M into FILENAME. */
    public static void dumpMatrix(double[][] m, String filename) {
        Out out = new Out(filename);
        out.print(MatrixUtils.matrixToString(m));
        out.close();
    }

    /** Dumps the vertical accumulation of matrix M into FILENAME.
     */

    public static void dumpVerticalAccumulation(double[][] m, String filename) {
        double[][] am = MatrixUtils.accumulateVertical(m);
        Out out = new Out(filename);
        out.print(MatrixUtils.matrixToString(am));
        out.close();
    }

    /** Dumps the horizontal accumulation of matrix M into FILENAME.
     */

    public static void dumpHorizontalAccumulation(double[][] m,
                                                  String filename) {
        double[][] am =
            MatrixUtils.accumulate(m, MatrixUtils.Orientation.HORIZONTAL);
        Out out = new Out(filename);
        out.print(MatrixUtils.matrixToString(am));
        out.close();

    }

    /** Dumps an annotated version of the vertical accumulation of matrix M
     *  into FILENAME. Matrix is annotated by the vertical seam.
     */

    public static void dumpVerticalSeam(double[][] m, String filename) {
        double[][] am = MatrixUtils.accumulateVertical(m);
        int[] vertSeam = MatrixUtils.findVerticalSeam(am);
        Out out = new Out(filename);
        out.print(MatrixUtils.seamToString(am, vertSeam,
                                           MatrixUtils.Orientation.VERTICAL));
        out.close();
    }

    /** Dumps an annotated version of the horizontal accumulation of matrix M
     *  into FILENAME. Matrix is annotated by the horizontal seam.
     */


    public static void dumpHorizontalSeam(double[][] m, String filename) {
        double[][] am =
            MatrixUtils.accumulate(m, MatrixUtils.Orientation.HORIZONTAL);
        int[] horizSeam =
            MatrixUtils.findSeam(m, MatrixUtils.Orientation.HORIZONTAL);
        Out out = new Out(filename);
        out.print(MatrixUtils.seamToString(am, horizSeam,
                                           MatrixUtils.Orientation.HORIZONTAL));
        out.close();
    }

    /** Returns the contents of the file FILENAME as a String. May be
     *  useful for testing your output vs. the sample outputs.
     */
    public static String readFile(String filename) {
        In in = new In(filename);
        return in.readAll();
    }

    /** Converts the image in file ARGS[0] to four files corresponding to its
     *  vertical accumulation, horizontal accumulation, vertical seam
     *  and horizontal seam.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage:\njava Utils [image filename]");
            return;
        }

        String filename = args[0];
        double[][] m = toEnergyMatrix(filename);
        dumpMatrix(m, filename + ".energyMatrix");
        dumpVerticalAccumulation(m, filename + ".verticalAccumulation");
        dumpHorizontalAccumulation(m, filename + ".horizontalAccumulation");
        dumpVerticalSeam(m, filename + ".verticalSeam");
        dumpHorizontalSeam(m, filename + ".horizontalSeam");
    }
}
