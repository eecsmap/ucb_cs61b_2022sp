package image;

/*************************************************************************
 *  Compilation:  javac Picture.java
 *  Execution:    java Picture imagename
 *
 *  Data type for manipulating individual pixels of an image. The original
 *  image can be read from a file in jpg, gif, or png format, or the
 *  user can create a blank image of a given size. Includes methods for
 *  displaying the image in a window on the screen or saving to a file.
 *
 *  % java Picture mandrill.jpg
 *
 *  Remarks
 *  -------
 *   - pixel (x, y) is column x and row y, where (0, 0) is upper left
 *
 *   - see also GrayPicture.java for a grayscale version
 *
 *************************************************************************/

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


/**
 *  This class provides methods for manipulating individual pixels of
 *  an image. The original image can be read from a <tt>.jpg</tt>,
 *  <tt>.gif</tt>,
 *  or <tt>.png</tt> file or the user can create a blank image of a given size.
 *  This class includes methods for displaying the image in a window on
 *  the screen or saving it to a file.
 *  <p>
 *  Pixel (<em>x</em>, <em>y</em>) is column <em>x</em> and row <em>y</em>.
 *  By default, the origin (0, 0) is upper left, which is a common convention
 *  in image processing.
 *  The method <tt>setOriginLowerLeft()</tt> change the origin to the
 *  lower left.
 *  <p>  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class Picture implements ActionListener {
    /** The rasterized image. */
    private BufferedImage image;
    /** On-screen view. */
    private JFrame frame;
    /** Name of file. */
    private String filename;
    /** Location of origin. */
    private boolean isOriginUpperLeft = true;
    /** Width and height. */
    private final int width, height;

   /** Initializes a blank W by H picture, where each pixel is black. */
    public Picture(int w, int h) {
        if (w < 0) {
            throw new IllegalArgumentException("width must be nonnegative");
        }
        if (h < 0) {
            throw new IllegalArgumentException("height must be nonnegative");
        }
        width = w;
        height = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        filename = w + "-by-" + h;
    }

   /** Initializes a new picture that is a deep copy of PIC.  */
    public Picture(Picture pic) {
        width = pic.width();
        height = pic.height();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        filename = pic.filename;
        for (int x = 0; x < width(); x += 1) {
            for (int y = 0; y < height(); y += 1) {
                image.setRGB(x, y, pic.get(x, y).getRGB());
            }
        }
    }

   /** Initializes a picture by reading in a .png, .gif, or .jpg from
     * the given file or URL named NAME. */
    public Picture(String name) {
        filename = name;
        try {
            File file = new File(name);
            if (file.isFile()) {
                image = ImageIO.read(file);
            } else {
                URL url = getClass().getResource(name);
                if (url == null) {
                    url = new URL(name);
                }
                image = ImageIO.read(url);
            }
            width  = image.getWidth(null);
            height = image.getHeight(null);
        } catch (IOException e) {
            throw new RuntimeException("Could not open file: " + name);
        }
    }

    /** Initializes a picture by reading in a .png, .gif, or .jpg from FILE. */
    public Picture(File file) {
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
        width  = image.getWidth(null);
        height = image.getHeight(null);
        filename = file.getName();
    }

   /** Returns a JLabel containing this picture, for embedding in a JPanel,
     * JFrame or other GUI widget.  Returns null if no image. */
    public JLabel getJLabel() {
        if (image == null) {
            return null;
        }
        ImageIcon icon = new ImageIcon(image);
        return new JLabel(icon);
    }

   /** Sets the origin to be the upper left pixel. */
    public void setOriginUpperLeft() {
        isOriginUpperLeft = true;
    }

   /** Sets the origin to be the lower left pixel. */
    public void setOriginLowerLeft() {
        isOriginUpperLeft = false;
    }

   /** Displays the picture in a window on the screen. */
    public void show() {

        if (frame == null) {
            frame = new JFrame();

            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem menuItem1 = new JMenuItem(" Save...   ");
            menuItem1.addActionListener(this);
            KeyStroke stroke =
                KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                       Toolkit.getDefaultToolkit()
                                       .getMenuShortcutKeyMask());
            menuItem1.setAccelerator(stroke);
            menu.add(menuItem1);
            frame.setJMenuBar(menuBar);

            frame.setContentPane(getJLabel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle(filename);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        }

        frame.repaint();
    }

   /** Returns the height of the picture (in pixels). */
    public int height() {
        return height;
    }

   /** Returns the width of the picture (in pixels). */
    public int width() {
        return width;
    }

   /** Returns the color of pixel (X, Y). */
    public Color get(int x, int y) {
        if (x < 0 || x >= width()) {
            throw new IndexOutOfBoundsException("x must be between 0 and "
                                                + (width() - 1));
        }
        if (y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException("y must be between 0 and "
                                                + (height() - 1));
        }
        if (isOriginUpperLeft) {
            return new Color(image.getRGB(x, y));
        } else {
            return new Color(image.getRGB(x, height - y - 1));
        }
    }

   /** Sets the color of pixel (X, Y) to COLOR. */
    public void set(int x, int y, Color color) {
        if (x < 0 || x >= width()) {
            throw new IndexOutOfBoundsException("x must be between 0 and "
                                                + (width() - 1));
        }
        if (y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException("y must be between 0 and "
                                                + (height() - 1));
        }
        if (color == null) {
            throw new NullPointerException("can't set Color to null");
        }
        if (isOriginUpperLeft) {
            image.setRGB(x, y, color.getRGB());
        } else {
            image.setRGB(x, height - y - 1, color.getRGB());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Picture that = (Picture) obj;
        if (this.width()  != that.width()) {
            return false;
        }
        if (this.height() != that.height()) {
            return false;
        }
        for (int x = 0; x < width(); x += 1) {
            for (int y = 0; y < height(); y += 1) {
                if (!this.get(x, y).equals(that.get(x, y))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }

   /** Saves the picture to a file NAME in a standard image format.
     * The filetype must be .png or .jpg. */
    public void save(String name) {
        save(new File(name));
    }

    /** Saves the picture to FILE in a standard image format, depending on
     *  the name of FILE.. */
    public void save(File file) {
        this.filename = file.getName();
        if (frame != null) {
            frame.setTitle(filename);
        }
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        suffix = suffix.toLowerCase();
        if (suffix.equals("jpg") || suffix.equals("png")) {
            try {
                ImageIO.write(image, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: filename must end in .jpg or .png");
        }
    }

   /** Opens a save dialog box when the user selects "Save As" from the menu.
     */
    public void actionPerformed(ActionEvent unused) {
        FileDialog chooser = new FileDialog(frame,
                             "Use a .png or .jpg extension", FileDialog.SAVE);
        chooser.setVisible(true);
        if (chooser.getFile() != null) {
            save(chooser.getDirectory() + File.separator + chooser.getFile());
        }
    }


   /** Tests this <tt>Picture</tt> data type. Reads a picture specified by
    *  ARGS[0], and shows it in a window on the screen. */
    public static void main(String[] args) {
        Picture pic = new Picture(args[0]);
        System.out.printf("%d-by-%d\n", pic.width(), pic.height());
        pic.show();
    }

}
