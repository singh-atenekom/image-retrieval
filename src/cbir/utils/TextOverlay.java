package cbir.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import cbir.GUI;

/**
 * Class for painting text over images
 *
 * @author Shiwang Singh
 *
 */
public class TextOverlay extends JPanel
{

  private BufferedImage image;
  private String        distanceValue;
  private int           count;
  private Font          txtFont;
  private GUI           frame;

  public TextOverlay( File imPath, GUI frame, String distanceValue, int count, Font txtFont )
  {
    this.distanceValue = distanceValue;
    this.count = count;
    this.txtFont = txtFont;
    this.frame = frame;

    try
    {
      image = ImageIO.read( imPath );
      image = process( image );
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }

  }

  @Override
  public Dimension getPreferredSize()
  {
    return new Dimension( image.getWidth() / frame.imageScaleFactor, image.getHeight() / frame.imageScaleFactor );
  }

  private BufferedImage process( BufferedImage old )
  {
    int w = old.getWidth();
    int h = old.getHeight();
    BufferedImage img = new BufferedImage(
        w, h, BufferedImage.TYPE_INT_RGB );
    Graphics2D g2d = img.createGraphics();
    g2d.drawImage( old, 0, 0, w / frame.imageScaleFactor, h / frame.imageScaleFactor, null );

    // TODO: change the color retrieval rate in %
    double distVal = 100 - 100 * Double.parseDouble( distanceValue );
    if ( distVal > 40 )
    {
      g2d.setPaint( Color.GREEN );
    }
    else
    {
      g2d.setPaint( Color.RED );
    }
    String s = String.format( "%2.2f", distVal );
    if ( count == 1 )
    {

      g2d.setPaint( Color.BLUE );
      s = " Query";
    }
    g2d.setFont( txtFont );
    // System.out.println( "image -> w: " + w + ", h:" + h );
    // TODO: retrieval rate

    FontMetrics fm = g2d.getFontMetrics();
    int x = fm.stringWidth( s ) / frame.imageScaleFactor;
    int y = fm.getHeight() / frame.imageScaleFactor;
    g2d.drawString( s, 4, y + 5 * 15 / frame.fontLabelOnImage );
    g2d.dispose();
    return img;
  }

  @Override
  protected void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    g.drawImage( image, 0, 0, null );
  }

}