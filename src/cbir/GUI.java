package cbir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import org.opencv.core.Core;

import cbir.utils.FileChooser;
import cbir.utils.HistogramFeature;
import cbir.utils.ImagePanel;
import cbir.utils.Retrieval;
import cbir.utils.StatusBar;

/**
 * 
 * Image Retrieval Application v1.0 @ 2016
 * Class: Main GUI
 *
 * @author Shiwang Singh
 *
 */
public class GUI extends JFrame implements ActionListener
{
  public String           guiName;
  public JMenuBar         menuBar;
  public JMenu            menuFile;
  public JMenu            menuOption;
  public JMenu            menuHelp;
  public JMenuItem        menuFileItemExit;
  public JPanel           leftPanel;
  public JPanel           lp1;
  public JPanel           lp2;
  public JButton          btnDeleteImagesIndex;
  public JButton          btnBilderhinzufuegen;
  public JButton          btnBilderverzeichnisHinzufuegen;
  public JLabel           lp1LabelBildDisplay;
  public JLabel           lp1Label1;
  public JLabel           lp1Logo;
  public Font             fontButton;
  public Font             fontLabelHead;
  public JLabel           lp2LabelBildDBErstellen;
  public JLabel           lp2LabelBildDBErstellenDisplay;
  public JButton          btnErstellen;
  public JLabel           lp2Logo;
  public JPanel           lp3;
  public JButton          btnBildAuswaehlen;
  public JLabel           lp3Logo;
  public JLabel           lp3LabelBildSuchen;
  public JLabel           lp3LabelBildSuchenDisplay;
  public JPanel           lp4;
  public JProgressBar     progressBar;
  public StatusBar        statusBar;
  public JPanel           lp5;
  public JLabel           lp5LabelStatus;
  public JButton          btnGO;
  public JButton          btnAbort;
  public JRadioButton     rbRGB;
  public JRadioButton     rbHSV;
  public File             imDefaultPath = new File( getClass().getResource( "userimages" ).getPath() );
  public File             featTxtPath   = new File( getClass().getResource( "feat.txt" ).getPath() );
  public FileChooser      fc;
  public File             imPathQuery;
  public JLabel           laImageBox;
  public String           laImageBoxFile;
  public ImageIcon        imageIcon;
  public JRadioButton     rbNormalView;
  public JRadioButton     rbFullView;
  public ImagePanel       imPanel;
  public File             selectedFile;
  public JScrollPane      imScrollPane;
  public Dimension        screenSize;
  public Dimension        preferredSize;
  public Dimension        preferredSizeBigLabel;
  public int              iconSize;
  public TabbedPane       resultTabbedPane;
  public HistogramFeature hsvHist;
  public JRadioButton     rbL1;
  public JRadioButton     rbL2;
  public JRadioButton     rbJSD;
  private JLabel          percentageLabel;
  public StringBuilder    queryFeatureX;
  public int              imageScaleFactor;
  public int              marginX;
  public int              nImageForDisplay;
  public int              imOffset;
  public int              fontLabelOnImage;
  public JRadioButton     rbYCrCB;                                                                      ;

  /**
   * Main method
   *
   * @param args
   */
  public static void main( String[] args )
  {

    GUI gui = new GUI( "Image Retrieval Application v1.0 @ 2016 Shiwang Singh" );
    gui.init();

  }

  /**
   * Loading external OpenCV Library for image processing
   */
  private void loadOpenCvLibrary()
  {
    try
    {
      /* Uncomment for Mac */
      
      System.loadLibrary( "lib/x64/opencv_java300" );
    }
    catch ( Exception e )
    {
      System.out.println( e.getMessage() );
    }

  }

  /**
   * constructor
   *
   * @param btnInsertImagesFolder
   */
  public GUI( JButton btnInsertImagesFolder )
  {
    this.btnBilderverzeichnisHinzufuegen = btnInsertImagesFolder;
  }

  public GUI( String guiName )
  {
    loadOpenCvLibrary();
    this.guiName = guiName;
  }

  /**
   * init values for TabbedPane
   */
  private void initValueTabbedPane()
  {

    imageScaleFactor = 2;
    marginX = 10;
    nImageForDisplay = 100;
    imOffset = 7;
    fontLabelOnImage = 14;//default 15

  }

  /**
   * initilazing values for GUI
   */
  private void init()
  {

    initValueTabbedPane();
    hsvHist = new HistogramFeature( this );
    fc = new cbir.utils.FileChooser( this );
    //screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    System.out.println( "imDefaultPath length -> " + imDefaultPath.listFiles().length );

    preferredSize = new Dimension( 210, 16 );
    preferredSizeBigLabel = new Dimension( 210, (int) ( preferredSize.getHeight() ) + 11 );
    //System.out.println( screenSize );
    iconSize = (int) ( preferredSize.getHeight() ) - 5;

    fontButton = this.getContentPane().getFont().deriveFont( Font.PLAIN, 11f );//11f
    fontLabelHead = this.getContentPane().getFont().deriveFont( Font.BOLD, 24f );//26f

    //this.pack();
    this.setVisible( true );
    //this.setMinimumSize( this.getSize() );
    this.setResizable( true );
    initComponents();
    //System.out.println( System.getProperty( "user.home" ) );

  }

  // GUI construction
  private void initComponents()
  {
    /*try
    {
      UIManager.setLookAndFeel( "javax.swing.plaf.nimbus.NimbusLookAndFeel" );
    }
    catch ( Exception e )
    {}*/
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize( 1280, 800 );
    // centering the GUI in middle of screen
    this.setLocation( dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2 );
    this.setTitle( this.guiName );
    //this.setAlwaysOnTop( true );
    // setting grid layout
    this.setLayout( new BorderLayout() );
    this.setDefaultCloseOperation( EXIT_ON_CLOSE );

    // creating menubar
    createMenuBar();

    // Left panel, defining GRID layout
    leftPanel = new JPanel( new GridLayout( 5, 0 ) );
    leftPanel.setPreferredSize( new Dimension( 260, this.getHeight() ) );
    leftPanel.setBackground( Color.LIGHT_GRAY );

    // Left panel's first object
    createLeftPanelFirst();

    // Left panel's second object
    createLeftPanelSecond();

    // Left panels's third object
    createLeftPanelThird();

    // Left panel's fourth object
    createLeftPanelImageBox();

    // Left panel's fifth object
    createLeftPanelStatusBar();

    // User's sepecific settings
    if ( imDefaultPath.listFiles().length > 0 )
    {
      System.out.println( "directory is not empty" );
      btnBilderhinzufuegen.setEnabled( false );
      btnBilderverzeichnisHinzufuegen.setEnabled( false );
      btnErstellen.setEnabled( false );
      lp2LabelBildDBErstellenDisplay.setText( "<html>"
          + "<font color='red'><b> Datenbank ist vorhanden</b></font></html>" );
      lp1LabelBildDisplay.setText( "<html>Es wurden "
          + "<font color='red'><b>" + imDefaultPath.listFiles().length
          + "</b></font> Bilder ausgewählt</html>" );
    }
    // Adding LeftPanel to GUI
    this.getContentPane().add( leftPanel, BorderLayout.WEST );
    System.out.println( "Feature .txt path -> " + featTxtPath.length() );

    // User's specific settings
    if ( featTxtPath.length() > 0 )
    {
      btnBildAuswaehlen.setEnabled( true );
    }

    // Tab pane
    resultTabbedPane = new TabbedPane( this );
    // adding TabbedPane to GUI
    this.getContentPane().add( resultTabbedPane, BorderLayout.CENTER );

    //fitting the windows.
    //this.pack();
  }

  /**
   * Creating first row in left panel
   */
  private void createLeftPanelFirst()
  {

    // GridBagLayout for Horizontal lines
    lp1 = new JPanel( new GridBagLayout() );
    lp1.setBackground( Color.YELLOW );
    GridBagConstraints labCnst = new GridBagConstraints();

    labCnst.insets = new Insets( 3, 3, 3, 3 );
    lp1Label1 = new JLabel( ""
        + "<html>Bilder auswählen, <br> in dem gesucht werden soll</html>"
        );
    lp1LabelBildDisplay = new JLabel( "Es wurden keine Bilder ausgewählt" );
    ImageIcon ii = new ImageIcon( getClass().getResource( "icons/insertImages.png" ) );
    ii.setImage( ii.getImage().getScaledInstance( iconSize, iconSize, Image.SCALE_SMOOTH ) );
    ImageIcon ii1 = new ImageIcon( getClass().getResource( "icons/insertFolder.png" ) );
    ii1.setImage( ii1.getImage().getScaledInstance( iconSize, iconSize, Image.SCALE_SMOOTH ) );
    ImageIcon ii2 = new ImageIcon( getClass().getResource( "icons/minus.png" ) );
    ii2.setImage( ii2.getImage().getScaledInstance( iconSize, iconSize, Image.SCALE_SMOOTH ) );
    btnBilderhinzufuegen = new JButton( "Bilder hinzufügen...", ii );
    btnBilderverzeichnisHinzufuegen = new JButton( "Bilderverzeichnisse hinzufügen...", ii1 );
    btnDeleteImagesIndex = new JButton( "Bilder aus Index entfernen", ii2 );
    lp1Logo = new JLabel( "<html><font color='red'>1</font><html>" );

    labCnst.gridx = 0;
    labCnst.gridy = 0;
    lp1.add( lp1Logo, labCnst );
    labCnst.gridx = 1;
    labCnst.gridy = 0;
    lp1.add( lp1Label1, labCnst );
    labCnst.gridy = 1;
    lp1.add( lp1LabelBildDisplay, labCnst );
    labCnst.gridy = 2;
    lp1.add( btnBilderhinzufuegen, labCnst );
    labCnst.gridy = 3;

    lp1.add( btnBilderverzeichnisHinzufuegen, labCnst );

    //labCnst.weightx = 1.0;

    labCnst.gridy = 4;
    lp1.add( btnDeleteImagesIndex, labCnst );

    btnBilderhinzufuegen.setPreferredSize( preferredSize );
    btnBilderverzeichnisHinzufuegen.setPreferredSize( preferredSize );
    btnDeleteImagesIndex.setPreferredSize( preferredSize );
    lp1Label1.setPreferredSize( preferredSizeBigLabel );
    lp1LabelBildDisplay.setPreferredSize( preferredSize );

    btnBilderhinzufuegen.setFont( fontButton );
    btnBilderverzeichnisHinzufuegen.setFont( fontButton );
    btnDeleteImagesIndex.setFont( fontButton );
    lp1LabelBildDisplay.setFont( fontButton );
    lp1Logo.setFont( fontLabelHead );

    btnBilderhinzufuegen.addActionListener( this );
    btnBilderverzeichnisHinzufuegen.addActionListener( this );
    btnDeleteImagesIndex.addActionListener( this );

    // wrapping lp1Label1
    //lp1Label1.setWrapStyleWord( true );

    leftPanel.add( lp1 );
  }

  /**
   * Creating second row in left panel
   */
  private void createLeftPanelSecond()
  {
    // GridBagLayout for Horizontal lines
    lp2 = new JPanel( new GridBagLayout() );
    lp2.setBackground( Color.PINK );
    GridBagConstraints labCnst = new GridBagConstraints();
    labCnst.insets = new Insets( 3, 3, 3, 3 );

    lp2LabelBildDBErstellen = new JLabel( ""
        + "<html>Bilderdatenbank erstellen <br></html>"
        );
    lp2LabelBildDBErstellenDisplay = new JLabel( "Keine Datenbank wurde ausgewählt" );

    ImageIcon ii = new ImageIcon( getClass().getResource( "icons/erstellen.png" ) );
    ii.setImage( ii.getImage().getScaledInstance( iconSize, iconSize, Image.SCALE_SMOOTH ) );

    btnErstellen = new JButton( "Erstellen", ii );
    btnErstellen.setEnabled( false );
    btnErstellen.addActionListener( this );
    lp2Logo = new JLabel( "<html><font color='red'>2</font><html>" );

    labCnst.gridx = 0;
    labCnst.gridy = 0;
    lp2.add( lp2Logo, labCnst );

    labCnst.gridx = 1;
    labCnst.gridy = 0;
    lp2.add( lp2LabelBildDBErstellen, labCnst );

    JLabel modeLabel = new JLabel( "<html><b>Farb Merkmal Histogram:</b></html>", JLabel.LEFT );
    modeLabel.setFont( fontButton );
    modeLabel.setPreferredSize( preferredSize );
    labCnst.gridy = 1;
    setupTypeRadio();
    lp2.add( modeLabel, labCnst );

    labCnst.gridy = 3;
    lp2.add( lp2LabelBildDBErstellenDisplay, labCnst );

    labCnst.gridy = 4;
    lp2.add( btnErstellen, labCnst );

    btnErstellen.setPreferredSize( preferredSize );
    lp2LabelBildDBErstellen.setPreferredSize( preferredSizeBigLabel );
    lp2LabelBildDBErstellenDisplay.setPreferredSize( preferredSize );

    btnErstellen.setFont( fontButton );
    lp2LabelBildDBErstellenDisplay.setFont( fontButton );
    lp2Logo.setFont( fontLabelHead );

    leftPanel.add( lp2 );
  }

  /**
   * Creating third row in left panel
   */
  private void createLeftPanelThird()
  {
    lp3 = new JPanel( new GridBagLayout() );
    lp3.setBackground( Color.ORANGE );
    GridBagConstraints labCnst = new GridBagConstraints();
    labCnst.insets = new Insets( 3, 3, 3, 3 );

    lp3LabelBildSuchen = new JLabel( ""
        + "<html>Datenbank mit einem Bild, <br> durchsuchen</html>"
        );
    lp3LabelBildSuchenDisplay = new JLabel( "Kein Bild wurde ausgewählt" );
    ImageIcon ii = new ImageIcon( getClass().getResource( "icons/search.png" ) );
    ii.setImage( ii.getImage().getScaledInstance( iconSize, iconSize, Image.SCALE_SMOOTH ) );
    btnBildAuswaehlen = new JButton( "Bild auswählen", ii );
    btnBildAuswaehlen.setEnabled( false );
    btnBildAuswaehlen.addActionListener( this );
    lp3Logo = new JLabel( "<html><font color='red'>3</font><html>" );

    labCnst.gridx = 0;
    labCnst.gridy = 0;
    lp3.add( lp3Logo, labCnst );
    labCnst.gridx = 1;
    labCnst.gridy = 0;
    lp3.add( lp3LabelBildSuchen, labCnst );
    labCnst.gridy = 1;
    lp3.add( lp3LabelBildSuchenDisplay, labCnst );
    labCnst.gridy = 2;
    lp3.add( btnBildAuswaehlen, labCnst );

    btnBildAuswaehlen.setPreferredSize( preferredSize );
    lp3LabelBildSuchen.setPreferredSize( preferredSizeBigLabel );
    lp3LabelBildSuchenDisplay.setPreferredSize( preferredSize );

    btnBildAuswaehlen.setFont( fontButton );
    lp3LabelBildSuchenDisplay.setFont( fontButton );
    lp3Logo.setFont( fontLabelHead );
    setupTypeRadioForImageView();
    leftPanel.add( lp3, labCnst );

  }

  /**
   * Creating image field in left panel
   */
  private void createLeftPanelImageBox()
  {
    lp4 = new JPanel();
    lp4.setLayout( new GridLayout( 0, 1 ) );
    // Get this image from Bild Auswählen Button
    imPathQuery = new File( getClass().getResource( "queryImageInternal/blank.jpg" ).getPath() );
    //imPanel = new ImagePanel( imPathQuery, 260, 150 );

    imageIcon = new ImageIcon( imPathQuery.getAbsolutePath() );
    laImageBox = new JLabel( imageIcon );

    imScrollPane = new JScrollPane( laImageBox );
    imScrollPane.setPreferredSize( new Dimension( 260, 140 ) );
    lp4.setBackground( Color.GRAY );
    lp4.add( imScrollPane );
    leftPanel.add( lp4 );

  }

  /**
   * 
   * Creating Menu bar
   */
  private void createMenuBar()
  {
    // set Menubar
    menuBar = new JMenuBar();
    this.setJMenuBar( menuBar );

    // set menu
    menuFile = new JMenu( "File" );
    menuOption = new JMenu( "Option" );
    menuHelp = new JMenu( "Help" );
    menuBar.add( menuFile );
    menuBar.add( menuOption );
    menuBar.add( menuHelp );

    // TODO menuFileItems ........
    menuFileItemExit = new JMenuItem( "Exit" );
    menuFileItemExit.addActionListener( this );
    // option ...
    // help ....

    menuFile.add( menuFileItemExit );
  }

  /**
   * Creating fifth row in left panel
   */
  private void createLeftPanelStatusBar()
  {
    lp5 = new JPanel( new GridBagLayout() );
    lp5.setBackground( Color.LIGHT_GRAY );
    GridBagConstraints labCnst = new GridBagConstraints();

    // Button GO
    btnGO = new JButton( "GO.." );
    btnGO.setPreferredSize( new Dimension( 100, 50 ) );
    //btnGO.setBackground( Color.GREEN );
    btnGO.addActionListener( this );
    btnGO.setFont( fontLabelHead );
    btnGO.setEnabled( false );

    btnAbort = new JButton( "Abort" );
    //btnGO.setBackground( Color.GREEN );
    btnAbort.addActionListener( this );
    btnAbort.setFont( fontButton );
    btnAbort.setEnabled( false );

    statusBar = new StatusBar();
    percentageLabel = new JLabel( " " );

    progressBar = new JProgressBar();
    progressBar.setOrientation( JProgressBar.HORIZONTAL );
    progressBar.setBorderPainted( true );
    progressBar.setMinimum( 0 );
    progressBar.setForeground( Color.BLUE );
    progressBar.setStringPainted( true );
    progressBar.setVisible( true );
    statusBar.add( progressBar );

    setupTypeRadioForDistances( labCnst );

    labCnst.fill = GridBagConstraints.NONE;
    labCnst.gridy = 1;
    lp5.add( new JLabel( " " ), labCnst );
    labCnst.gridy = 2;
    lp5.add( btnGO, labCnst );
    labCnst.gridy = 3;
    lp5LabelStatus = new JLabel( "fertig." );
    lp5.add( lp5LabelStatus, labCnst );
    labCnst.gridy = 4;
    lp5.add( statusBar, labCnst );
    //labCnst.gridx = 1;
    //labCnst.gridy = 4;
    //lp5.add( percentageLabel, labCnst );
    //labCnst.gridx = 0;
    //labCnst.gridy = 4;

    //lp5.add( btnAbort, labCnst );
    leftPanel.add( lp5 );
    statusBar.setPreferredSize( preferredSize );
    lp5LabelStatus.setFont( fontButton );
    lp5LabelStatus.setPreferredSize( preferredSize );
  }

  /**
   * Radio Buttons in fifth row in left panel
   */
  private void setupTypeRadioForDistances( GridBagConstraints labCnst )
  {

    rbL1 = new JRadioButton( "L1" );
    rbL1.setActionCommand( "L1" );
    rbL1.setToolTipText( "Manhatten" );
    rbL1.setBackground( Color.LIGHT_GRAY );
    rbL1.setEnabled( false );

    rbL2 = new JRadioButton( "L2" );
    rbL2.setActionCommand( "L2" );
    rbL2.setToolTipText( "Euclidean" );
    rbL2.setBackground( Color.LIGHT_GRAY );
    rbL2.setEnabled( false );

    rbJSD = new JRadioButton( "JSD" );
    rbJSD.setActionCommand( "JSD" );
    rbJSD.setToolTipText( "Jeffrey Divergence" );
    rbJSD.setBackground( Color.LIGHT_GRAY );
    rbJSD.setEnabled( false );

    ButtonGroup group = new ButtonGroup();
    group.add( rbL1 );
    group.add( rbL2 );
    group.add( rbJSD );

    rbL1.addActionListener( this );
    rbL2.addActionListener( this );
    rbJSD.addActionListener( this );

    JPanel radioOperationPanel = new JPanel( new GridLayout( 1, 0 ) );
    radioOperationPanel.setBackground( Color.LIGHT_GRAY );
    rbL1.setFont( fontButton );
    rbL2.setFont( fontButton );
    rbJSD.setFont( fontButton );

    JLabel labelDistance = new JLabel( "Distance:" );
    labelDistance.setFont( fontButton );

    radioOperationPanel.add( labelDistance );
    radioOperationPanel.add( rbL1 );
    radioOperationPanel.add( rbL2 );
    radioOperationPanel.add( rbJSD );
    radioOperationPanel.setPreferredSize( preferredSize );
    labCnst.fill = GridBagConstraints.HORIZONTAL;
    labCnst.gridx = 0;
    labCnst.gridy = 0;
    lp5.add( radioOperationPanel, labCnst );

  }

  /**
   * Radio Buttons in second row in left panel
   */
  private void setupTypeRadio()
  {

    rbRGB = new JRadioButton( "RGB" );
    rbRGB.setActionCommand( "RGB" );
    rbRGB.setBackground( Color.PINK );
    rbRGB.setEnabled( false );

    rbHSV = new JRadioButton( "HSV" );
    rbHSV.setActionCommand( "HSV" );
    rbHSV.setBackground( Color.PINK );
    rbHSV.setEnabled( false );

    rbYCrCB = new JRadioButton( "YCrCB" );
    rbYCrCB.setActionCommand( "YCrCB" );
    rbYCrCB.setBackground( Color.PINK );
    rbYCrCB.setEnabled( false );

    ButtonGroup group = new ButtonGroup();
    group.add( rbRGB );
    group.add( rbHSV );
    group.add( rbYCrCB );

    rbRGB.addActionListener( this );
    rbHSV.addActionListener( this );
    rbYCrCB.addActionListener( this );

    JPanel radioOperationPanel = new JPanel( new GridLayout( 1, 0 ) );
    radioOperationPanel.setBackground( Color.PINK );
    rbRGB.setFont( fontButton );
    rbHSV.setFont( fontButton );
    rbYCrCB.setFont( fontButton );

    radioOperationPanel.add( rbRGB );
    radioOperationPanel.add( rbHSV );
    radioOperationPanel.add( rbYCrCB );

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 2;
    lp2.add( radioOperationPanel, c );

  }

  /**
   * Radio Buttons in third row in left panel
   */
  private void setupTypeRadioForImageView()
  {

    rbNormalView = new JRadioButton( "normal" );
    rbNormalView.setMnemonic( KeyEvent.VK_S );
    rbNormalView.setActionCommand( "normal" );
    rbNormalView.setBackground( Color.ORANGE );
    rbNormalView.setEnabled( false );

    rbFullView = new JRadioButton( "full" );
    rbFullView.setMnemonic( KeyEvent.VK_L );
    rbFullView.setActionCommand( "full" );
    rbFullView.setBackground( Color.ORANGE );
    rbFullView.setEnabled( false );

    ButtonGroup group = new ButtonGroup();
    group.add( rbNormalView );
    group.add( rbFullView );

    rbNormalView.addActionListener( this );
    rbFullView.addActionListener( this );

    JPanel radioOperationPanel = new JPanel( new GridLayout( 1, 0 ) );
    radioOperationPanel.setBackground( Color.ORANGE );
    rbNormalView.setFont( fontButton );
    rbFullView.setFont( fontButton );

    radioOperationPanel.add( rbNormalView );
    radioOperationPanel.add( rbFullView );

    GridBagConstraints labCnst = new GridBagConstraints();
    labCnst.fill = GridBagConstraints.HORIZONTAL;
    labCnst.gridx = 1;
    labCnst.gridy = 4;
    lp3.add( radioOperationPanel, labCnst );
    JLabel modeLabel = new JLabel( "<html><b>Image View:</b></html>", JLabel.LEFT );
    modeLabel.setFont( fontButton );
    modeLabel.setPreferredSize( preferredSize );
    labCnst.gridy = 3;
    lp3.add( modeLabel, labCnst );
  }

  /**
   * ActionListners for all Buttons
   */
  @Override
  public void actionPerformed( ActionEvent e )
  {
    Object o = e.getSource();

    /** Menu Items */
    if ( o == menuFileItemExit )
    {
      this.dispose();
      System.out.println( "Clicked -> " + menuFileItemExit.getText() );
    }

    /** Button: Bilder hinzufügen ... */
    if ( o == btnBilderhinzufuegen )
    {

      fc.insertImagesanually();
      lp1LabelBildDisplay.setText( "<html>Es wurden "
          + "<font color='red'><b>" + imDefaultPath.listFiles().length
          + "</b></font> Bilder ausgewählt</html>" );
    }

    /** Button: Bilderverzeichnis hinzufügen ... */
    if ( o == btnBilderverzeichnisHinzufuegen )
    {

      System.out.println( "\nClicked -> " + btnBilderverzeichnisHinzufuegen.getText() );

      fc.setChoosertitle( btnBilderverzeichnisHinzufuegen.getText() );
      // Copy Images from selected folder
      fc.performFileChooser();
      if ( fc.getStatus() == JFileChooser.CANCEL_OPTION )
        return;

      System.out.println( "Number of images in folder -> " +
          fc.getNumberOfFiles() );
    }

    /** TODO Create HSV features from a image folder */
    if ( o == btnErstellen )
    {
      hsvHist.calcHistInThread( this );

    }

    /** Select a query Image, button: Bild auswählen */
    if ( o == btnBildAuswaehlen )
    {
      System.out.println( "Clicked -> Bild Auswählen" );
      selectedFile = fc.selectQueryFromFolder();
      if ( selectedFile != null )
      {
        laImageBoxFile = selectedFile.getAbsolutePath();
        imageIcon.setImage( new ImageIcon( laImageBoxFile ).getImage() );
        imScrollPane.setViewportView( new JLabel( imageIcon ) );

        rbFullView.setEnabled( true );
        rbFullView.setSelected( true );
        rbNormalView.setEnabled( true );
        rbFullView.setSelected( false );
        progressBar.setValue( 0 );
        rbL1.setEnabled( true );
        rbL1.setSelected( true );
        rbL2.setEnabled( true );
        rbJSD.setEnabled( true );
        btnGO.setEnabled( true );
        btnGO.requestFocusInWindow();
      }
    }

    /** TODO Select Distance Manhatten */
    if ( o == rbL2 || o == rbJSD )
    {
      btnGO.setEnabled( true );
      btnGO.requestFocusInWindow();
    }

    /** Search Query image in database */
    if ( o == btnGO )
    {
      System.err.println( "Clicked -> GO" );
      // Quantization of query image
      queryFeatureX = hsvHist.uniformQuantization( selectedFile );

      System.out.println( "rbL1 -> selected" );
      // Retrieval with given query image in a database with Manhatten distance

      new Retrieval( this );

    }

    /** For display the query image in GUI */
    if ( o == rbFullView )
    {
      imScrollPane.setViewportView( new JLabel( imageIcon ) );
      System.out.println( "radio" );
    }
    if ( o == rbNormalView )
    {
      imPanel = new ImagePanel( selectedFile, 240, 140 );
      imScrollPane.setViewportView( imPanel );
    }
    if ( o == rbFullView )
    {
      imScrollPane.setViewportView( new JLabel( imageIcon ) );
    }

    /** Delete files from Directory, button: Bilder aus index entferen */
    if ( o == btnDeleteImagesIndex )
    {
      System.out.println( "Clicked -> Bilder aus index entferen" );
      fc.deleteFilesFromDefaultFolder();
      PrintWriter writer;
      try
      {
        writer = new PrintWriter( featTxtPath );
        writer.print( "" );
        writer.close();
      }
      catch ( FileNotFoundException e1 )
      {
        //Auto-generated catch block
        e1.printStackTrace();
      }

      System.out.println( "INFO -> Kein Bilder in Default Image Directory" );
      /* Users specific settings */
      lp1LabelBildDisplay.setText( "Es wurden keine Bilder ausgewählt" );
      lp2LabelBildDBErstellenDisplay.setText( "Keine Datenbank wurde ausgewählt" );
      btnBildAuswaehlen.setEnabled( false );
      rbFullView.setEnabled( false );
      rbFullView.setSelected( false );
      rbNormalView.setEnabled( false );
      rbNormalView.setSelected( false );
      btnGO.setEnabled( false );
      btnBilderhinzufuegen.setEnabled( true );
      btnErstellen.setEnabled( false );
      btnBilderverzeichnisHinzufuegen.setEnabled( true );
      progressBar.setValue( 0 );
      imScrollPane.setViewportView( new JLabel( new ImageIcon( imPathQuery.getAbsolutePath() ) ) );
      if ( imDefaultPath.listFiles().length == 0 )
        resultTabbedPane.getScrollPanel().setViewportView( new JPanel() );

    }

  }
}
