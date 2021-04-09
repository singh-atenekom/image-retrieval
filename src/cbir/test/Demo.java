package cbir.test;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Demo implements ActionListener
{

  private DrawingPanel iconPanel;
  private File         imPathQuery;
  private ImageIcon    imageIcon;

  public Demo()
  {
    JFrame frame = new JFrame();
    frame.setSize( 400, 400 );
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.setLayout( new BorderLayout() );

    // Grab the image.
    imPathQuery = new File( getClass().getResource( "../queryImage/query.jpg" ).getPath() );
    imageIcon = new ImageIcon( imPathQuery.getAbsolutePath() );
    Image img = imageIcon.getImage();
    // Create an instance of DrawingPanel
    final DrawingPanel iconPanel = new DrawingPanel( img );

    frame.add( iconPanel, BorderLayout.CENTER );

    JButton button = new JButton( "Change image.." );
    frame.add( button, BorderLayout.NORTH );

    frame.setVisible( true );
  }

  public static void main( String[] args )
  {
    new Demo();
  }

  @Override
  public void actionPerformed( ActionEvent e )
  {

    iconPanel.setImg( new ImageIcon( "2.png" ).getImage() );
    iconPanel.repaint();

  }
}

class DrawingPanel extends JPanel
{
  Image img;

  DrawingPanel( Image img )
  {
    this.img = img;
  }

  public void setImg( Image img )
  {
    this.img = img;
  }

  @Override
  public void paintComponent( Graphics g )
  {
    super.paintComponent( g );

    // Use the image width & height to find the starting point
    int imgX = getSize().width / 2 - img.getWidth( this );
    int imgY = getSize().height / 2 - img.getHeight( this );

    // Draw image centered in the middle of the panel
    g.drawImage( img, imgX, imgY, this );
  } // paintComponent

}