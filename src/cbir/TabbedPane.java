package cbir;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import cbir.utils.TextOverlay;

/**
 * Tapped Pane class on GUI
 *
 * @author Shiwang Singh
 *
 */
public class TabbedPane extends JPanel
{

  private TextOverlay       bufferedImagePanel;
  private JPanel            jp;
  private JTabbedPane       tabbedPane;
  private JScrollPane       scrollPanel;
  private ArrayList<String> sortedNImagePath;
  private JScrollPane       scrollPane;
  private Font              txtFont;
  private GUI               frame;

  /**
   * Settings for Display images
   */
  private void initPane()
  {}

  public JScrollPane getScrollPanel()
  {
    return scrollPanel;
  }

  /**
   * Creation of Tabbed Pane
   *
   * @param imDefaultPath
   */
  public TabbedPane( GUI frame )
  {

    super( new GridLayout( 1, 1 ) );
    this.frame = frame;

    // Frame's config values
    initPane();
    initComponents();

  }

  private void initComponents()
  {
    tabbedPane = new JTabbedPane();
    ImageIcon icon = null;//createImageIcon( "images/middle.gif" );

    // (1) makeJScrollPanePanel();
    scrollPane = makeJScrollPanePanel();
    tabbedPane.addTab( "Ergebnisbilder", icon, scrollPane,
        "Does nothing" );
    tabbedPane.setMnemonicAt( 0, KeyEvent.VK_1 );

    //panelText = makeResultPanel( "Panel #2" );
    // (2) Results tapped Pane
    //tabbedPane.addTab( "Results", icon, panelText,
    //    "Does nothing" );
    //tabbedPane.setMnemonicAt( 1, KeyEvent.VK_2 );
    //Add the tabbed pane to this panel.

    add( tabbedPane );
    //The following line enables to use scrolling tabs.
    tabbedPane.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );

  }

  /**
   * Create image in the Tab pane
   *
   * @param sortedNImagePath
   * @param sortedNImageDistanceValues
   */
  public void createImagesInTab( ArrayList<String> sortedNImagePath, ArrayList<String> sortedNImageDistanceValues )
  {
    JPanel jp = new JPanel( new GridBagLayout() );
    if ( frame.imDefaultPath.listFiles().length == 0 )
    {
      scrollPanel.setViewportView( new JPanel() );
      return;
    }
    GridBagConstraints labCnst = new GridBagConstraints();
    int count = 0;
    int i = 0, j = 1;//sortedNImagePath
    //for( final File fileEntry : imDefaultPath.listFiles() ) // Show default folder
    for( final String fileEntry : sortedNImagePath )
    {
      if ( count >= frame.nImageForDisplay )
        break;

      String distanceValue = sortedNImageDistanceValues.get( count );
      JPanel box = makeImageBox( new File( fileEntry ), distanceValue, count + 1 );

      if ( count % frame.imOffset == 0 )
      {
        i++;
        if ( j > 2 )
        {
          j = 1;
        }
      }
      else j++;
      labCnst.gridx = j - 1;
      labCnst.gridy = i - 1;

      /*
      System.out.println();
      System.out.println( fileEntry.getName() );
      System.out.printf( "(%d, %d)", labCnst.gridy, labCnst.gridx );
       */
      jp.add( box, labCnst );
      count++;

    }
    scrollPanel.setViewportView( jp );
  }

  private JScrollPane makeJScrollPanePanel()
  {

    jp = new JPanel( new GridBagLayout() );
    scrollPanel = new JScrollPane( jp );
    return scrollPanel;
  }

  /**
   * create box for image
   *
   * @param imagePath
   * @param distanceValue
   * @param count
   * @return
   */
  private JPanel makeImageBox( File imagePath, String distanceValue, int count )
  {

    GridBagConstraints labCnst = new GridBagConstraints();
    ImageIcon ii = new ImageIcon( imagePath.getAbsolutePath() );

    if ( count == 1 )
    {
      if ( ii.getIconWidth() > ii.getIconHeight() ) txtFont = new Font( "Serif", Font.BOLD,
          (int) ( ii.getIconWidth() / frame.fontLabelOnImage ) );
      else txtFont = new Font( "Serif", Font.BOLD, (int) ( ii.getIconHeight() / frame.fontLabelOnImage ) );

    }
    bufferedImagePanel = new TextOverlay( imagePath, frame, distanceValue, count, txtFont );
    JPanel pBoxes = new JPanel( new GridBagLayout() );
    pBoxes.setPreferredSize( new Dimension( ii.getIconWidth() / frame.imageScaleFactor + frame.marginX, ii
        .getIconHeight()
        / frame.imageScaleFactor
        + 50 ) );

    //pBoxes.setLayout( null );
    JPanel inJPanel = new JPanel();
    inJPanel
        .setPreferredSize( new Dimension( ii.getIconWidth() / frame.imageScaleFactor, ii.getIconHeight()
            / frame.imageScaleFactor ) );
    //inJPanel.setBorder( BorderFactory.createLineBorder( Color.BLACK, 1 ) );
    inJPanel.add( bufferedImagePanel );

    labCnst.gridx = 0;
    labCnst.gridy = 1;
    pBoxes.add( inJPanel, labCnst );
    labCnst.gridy = 0;
    JLabel imName = new JLabel( String.format( "%d) %s", count, imagePath.getName() ) );
    pBoxes.add( imName, labCnst );
    return pBoxes;
  }

  /** Returns an ImageIcon, or null if the path was invalid. */
  protected static ImageIcon createImageIcon( String path )
  {
    URL imgURL = TabbedPane.class.getResource( path );
    if ( imgURL != null )
    {
      return new ImageIcon( imgURL );
    }
    else
    {
      System.err.println( "Couldn't find file: " + path );
      return null;
    }
  }

  public void setImageFiles( ArrayList<String> arrayList )
  {
    this.sortedNImagePath = arrayList;
  }

  public void cleanTabbedPane()
  {
    scrollPanel.removeAll();
  }

}
