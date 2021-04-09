package cbir.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import cbir.GUI;

/*
 * Class FileChooser.java
 * @author : Shiwang Singh
 */
public class FileChooser

{

  JFileChooser          chooser;
  String                choosertitle;
  int                   s                     = 0;
  private int           numberOfFiles         = 0;
  private File          userSelectedDirectory = new File( "../corelDB" );
  private BufferedImage img;
  private JFileChooser  chooserFile;
  private boolean       QUERY                 = false;
  private int           noOfImageForSave      = 30;
  private int           status;
  private JFileChooser  chooserFileManual;
  private String        currFolder            = "..";
  private String        currFolderQuery       = "..";
  private boolean       noFileSelection;
  private GUI           frame;

  public FileChooser( GUI frame )
  {
    this.frame = frame;
  }

  public FileChooser()
  {
    //this.currFolderQuery = getClass().getResource( "../queryImagesCorelDB" ).getPath();
  }

  /**
   * This function is for button Bilderverzeichnisse Hinzufügen
   *
   * @param frame
   * @param progressBar
   * @param lp5LabelStatus
   * @param lp1LabelBildDisplay
   * @param btnErstellen
   */
  public void performFileChooser()
  {

    QUERY = false;
    chooser = new JFileChooser();
    chooser.setCurrentDirectory( userSelectedDirectory );
    chooser.setDialogTitle( choosertitle );
    chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    chooser.setAcceptAllFileFilterUsed( false );
    status = chooser.showOpenDialog( frame );

    if ( status == JFileChooser.APPROVE_OPTION )
    {
      numberOfFiles = chooser.getSelectedFile().listFiles().length;
      userSelectedDirectory = chooser.getSelectedFile();
      currFolderQuery = userSelectedDirectory.getAbsolutePath();
      // TODO ProgressBar operation
      frame.progressBar.setMaximum( numberOfFiles );

      Runnable runner = new Runnable()
      {
        @Override
        public void run()
        {
          frame.lp5LabelStatus
          .setText( "<html><font color='green'><b> Bildverzeichnisse Hinzufügen ...</b></font></html>" );
          int count = 1;
          for( final File fileEntry : userSelectedDirectory.listFiles() )
          {
            if ( true ) //( count < noOfImageForSave )
            {
              try
              {
                copyProcess( fileEntry, frame.imDefaultPath, false );
              }
              catch ( Exception e )
              {
                System.out.println( e.getMessage() );
              }
            }
            frame.progressBar.setValue( count );
            count++;
          }
          frame.rbHSV.setEnabled( true );
          frame.rbRGB.setEnabled( true );
          frame.rbRGB.setSelected( true );
          frame.rbYCrCB.setEnabled( true );
          frame.btnErstellen.setEnabled( true );
          frame.lp5LabelStatus.setText( "<html><font color='green'><b> fertig! </b></font></html>" );
          frame.lp1LabelBildDisplay.setText( "<html>Es wurden "
              + "<font color='red'><b>" + frame.imDefaultPath.listFiles().length
              + "</b></font> Bilder ausgewählt</html>" );

        }
      };
      Thread t = new Thread( runner, "thread Bildverzeichnisse hinzufugen" );
      t.start();

      /*System.out.println( "imSystemPath: "
          + imDefaultPath );
      System.out.println( "userSelectedDirectory : "
          + chooser.getSelectedFile() );*/
    }
    else
    {
      noFileSelection = true;
      System.out.println( "No Selection " );
    }
  }

  /**
   * This function is for button Bilder hinzufügen
   *
   * @param frame
   * @return
   */
  public File insertImagesanually()
  {
    chooserFileManual = new JFileChooser();
    chooserFileManual.setCurrentDirectory( new File( currFolder ) );// corelDB folder
    chooserFileManual.setDialogTitle( choosertitle );
    chooserFileManual.setFileFilter( new FileNameExtensionFilter( "Image JPEG (*.jpg)", "jpg" ) );
    chooserFileManual.addChoosableFileFilter( new FileNameExtensionFilter( "Image PNG (*.png)", "png" ) );
    chooserFileManual.setFileSelectionMode( JFileChooser.FILES_ONLY );
    chooserFileManual.setAcceptAllFileFilterUsed( false );
    chooserFileManual.setMultiSelectionEnabled( true );

    //
    if ( chooserFileManual.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION )
    {
      File folder = chooserFileManual.getSelectedFile();
      currFolder = folder.getPath().replaceAll( folder.getName(), "" );
      currFolder = currFolder.substring( 0, currFolder.length() - 1 );
      currFolderQuery = currFolder;
      System.out.println( "userSelectedFiles -> " + chooserFileManual.getSelectedFiles().length );

      Runnable runner = new Runnable()
      {
        @Override
        public void run()
        {
          try
          {

            frame.progressBar.setMaximum( chooserFileManual.getSelectedFiles().length );
            int count = 1;
            if ( chooserFileManual.getSelectedFiles().length > 0 ) for( File file : chooserFileManual
                .getSelectedFiles() )
            {
              System.out.println( "selected image by user -> " + file );
              frame.progressBar.setValue( count );
              copyProcess( file,
                  new File( getClass().getResource( "../userimages" ).getPath() ), false );
              count++;
            }
            else
            {
              copyProcess( chooserFileManual.getSelectedFile(),
                  new File( getClass().getResource( "../userimages" ).getPath() ), false );
            }

          }
          catch ( Exception e )
          {
            System.out.println( e.getMessage() );
          }
          frame.rbHSV.setEnabled( true );
          frame.rbRGB.setEnabled( true );
          frame.rbRGB.setSelected( true );
          frame.rbYCrCB.setEnabled( true );
          frame.btnErstellen.setEnabled( true );
          frame.lp1LabelBildDisplay.setText( "<html>Es wurden "
              + "<font color='red'><b>" + chooserFileManual.getSelectedFiles().length
              + "</b></font> Bilder ausgewählt</html>" );

        }
      };
      Thread t = new Thread( runner, "thread Bilder hinzufugen" );
      t.start();
      frame.lp5LabelStatus.setText( "<html><font color='green'><b> fertig! </b></font></html>" );

      System.out.println( "destination folder -> "
          + new File( getClass().getResource( "../userimages" ).getPath() ) );
      System.out.println( "userSelected folder -> "
          + currFolder );
    }
    else
    {
      noFileSelection = true;
      System.out.println( "No Selection " );
    }
    return chooserFileManual.getSelectedFile();
  }

  /**
   * This function is for button Bild auswählen
   *
   * @param frame
   * @return chooserFile.getSelectedFile()
   */
  public File selectQueryFromFolder()
  {
    QUERY = true;
    chooserFile = new JFileChooser();
    chooserFile.setCurrentDirectory( new File( currFolderQuery ) );
    chooserFile.setDialogTitle( choosertitle );
    chooserFile.setFileFilter( new FileNameExtensionFilter( "Image JPEG (*.jpg)", "jpg" ) );
    chooserFile.addChoosableFileFilter( new FileNameExtensionFilter( "Image PNG (*.png)", "png" ) );
    chooserFile.setFileSelectionMode( JFileChooser.FILES_ONLY );
    chooserFile.setAcceptAllFileFilterUsed( false );
    //
    if ( chooserFile.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION )
    {
      File folder = chooserFile.getSelectedFile();
      currFolderQuery = folder.getPath().replaceAll( folder.getName(), "" );
      currFolderQuery = currFolderQuery.substring( 0, currFolderQuery.length() - 1 );
      try
      {

        copyProcess( chooserFile.getSelectedFile(),
            new File( getClass().getResource( "../queryImageInternal" ).getPath() ), true );
      }
      catch ( Exception e )
      {
        // TODO: handle exception
        System.out.println( e.getMessage() );
      }

      System.out.println( "queryPath -> "
          + new File( getClass().getResource( "../queryImageInternal" ).getPath() ) );
      System.out.println( "userSelectedFile -> "
          + chooserFile.getSelectedFile() );
    }
    else
    {
      noFileSelection = true;
      System.out.println( "No Selection " );
    }
    return chooserFile.getSelectedFile();
  }

  public int getStatus()
  {
    return status;
  }

  private void copyProcess( File sourceFile, File destFile, boolean query )
  {
    try
    {
      img = ImageIO.read( sourceFile );
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }

    BufferedImage bimg = new BufferedImage( img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB );

    Graphics2D g2 = bimg.createGraphics();
    g2.drawImage( img, 0, 0, null );
    g2.dispose();
    //System.out.println( new File( destFile.getAbsolutePath() + "/" + sourceFile.getName() ) );
    try
    {
      if ( query ) ImageIO.write( bimg, "jpg", new File( destFile.getAbsolutePath() + "/query.jpg" ) );
      else ImageIO.write( bimg, "jpg", new File( destFile.getAbsolutePath() + "/" + sourceFile.getName() ) );

    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }

  }

  /**
   * Delete all file contains in Default folder i.e "userimages"
   */
  public void deleteFilesFromDefaultFolder()
  {
    for( final File fileEntry : frame.imDefaultPath.listFiles() )

      try
      {
        Files.delete( fileEntry.toPath() );
      }
      catch ( NoSuchFileException x )
      {
        System.err.format( "%s: no such" + " file or directory%n", fileEntry.toPath() );
      }
      catch ( DirectoryNotEmptyException x )
      {
        System.err.format( "%s not empty%n", fileEntry.toPath() );
      }
      catch ( IOException x )
      {
        // File permission problems are caught here.
        System.err.println( x );
      }
  }

  public int getNumberOfFiles()
  {
    return numberOfFiles;
  }

  public File getUserSelectedDirectory()
  {
    return userSelectedDirectory;
  }

  public JFileChooser getChooser()
  {
    return chooser;
  }

  public void setChooser( JFileChooser chooser )
  {
    this.chooser = chooser;
  }

  public String getChoosertitle()
  {
    return choosertitle;
  }

  public void setChoosertitle( String choosertitle )
  {
    this.choosertitle = choosertitle;
  }

  public int getNoOfImageForSave()
  {
    return noOfImageForSave;
  }

  public void setNoOfImageForSave( int noOfImageForSave )
  {
    this.noOfImageForSave = noOfImageForSave;
  }

  public void setNumberOfFiles( int numberOfFiles )
  {
    this.numberOfFiles = numberOfFiles;
  }

}