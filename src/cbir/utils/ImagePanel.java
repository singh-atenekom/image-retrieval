package cbir.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
  private int           height;
  private int           width;

  private BufferedImage image;

  public ImagePanel( File imPath, int height, int width )
  {
    this.height = height;
    this.width = width;
    try
    {

      image = ImageIO.read( imPath );

    }
    catch ( IOException ex )
    {
      System.out.println( ex.getMessage() );
      // handle exception...
    }
  }

  @Override
  protected void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    g.drawImage( image, 0, 0, height, width, null ); // see javadoc for more info on the parameters
  }

}