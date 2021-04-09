package cbir.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cbir.GUI;

/**
 * Class HistogramFeature
 *
 * @author Shiwang
 *
 */
public class HistogramFeature
{

  ImageViewer imageViewer = new ImageViewer();
  private GUI frame;

  public static void main( String[] args )
  {
    //new HistogramFeature();
  }

  public HistogramFeature()
  {}

  public HistogramFeature( GUI frame )
  {
    this.frame = frame;
    //initComponents();
    //updateView();

    //uniformQuantization();
    //new App();
    //filePath = "../image/0.jpg";
    //calcHistInThread();
    //imshow();
  }

  /**
   * This function calculates HSV histogram for all images from default folder
   * "userimages" in Thread
   *
   * @param imDefaultPath
   * @param progressBar
   * @param lp5LabelStatus
   */
  public void calcHistInThread( final GUI frame
      )
  {

    Runnable runner = new Runnable()
    {
      @Override
      public void run()
      {
        int count = 0;
        frame.progressBar.setMaximum( frame.imDefaultPath.listFiles().length );
        frame.progressBar.setValue( count );
        frame.lp5LabelStatus.setText( "<html><font color='orange'><b> Datenbank erstellen ..</b></font></html>" );

        // File writer
        BufferedWriter bw = null;
        try
        {

          File file = new File( getClass().getResource( "../feat.txt" ).getPath() );
          FileWriter fw = new FileWriter( file.getAbsoluteFile() );
          bw = new BufferedWriter( fw );

          System.out.println( "Write File to -> " + file );
          // if file doesnt exists, then create it
          if ( !file.exists() )
          {
            file.createNewFile();
          }

          for( final File fileEntry : frame.imDefaultPath.listFiles() )
          {
            StringBuilder sbFeatureX = uniformQuantization( fileEntry );
            //System.out.println( sbFeatureX.toString() );
            bw.write( fileEntry + ";" + sbFeatureX.toString() );
            bw.newLine();
            count++;
            frame.progressBar.setValue( count );
          }
          frame.lp5LabelStatus.setText( "<html><font color='green'><b> Datenbank erstellt !</b></font></html>" );
          if ( frame.rbRGB.isSelected() )
            frame.lp2LabelBildDBErstellenDisplay.setText( "<html>"
                + "<font color='red'><b> RGB Datenbank ist vorhanden</b></font></html>" );
          if ( frame.rbHSV.isSelected() )
            frame.lp2LabelBildDBErstellenDisplay.setText( "<html>"
                + "<font color='red'><b> HSV Datenbank ist vorhanden</b></font></html>" );
          if ( frame.rbYCrCB.isSelected() )
            frame.lp2LabelBildDBErstellenDisplay.setText( "<html>"
                + "<font color='red'><b> YCBCR Datenbank ist vorhanden</b></font></html>" );
          frame.btnErstellen.setEnabled( false );
          frame.rbHSV.setEnabled( false );
          frame.rbRGB.setEnabled( false );
          frame.rbYCrCB.setEnabled( false );
          frame.btnBildAuswaehlen.setEnabled( true );
          frame.btnBildAuswaehlen.requestFocusInWindow();
        }
        catch ( Exception e )
        {
          e.printStackTrace();
        }
        finally
        {
          try
          {
            // Close the writer regardless of what happens...
            bw.close();
          }
          catch ( Exception e )
          {}
        }

      }
    };
    /* Start the Thread */
    Thread t = new Thread( runner, "feature extraction Executer" );
    t.start();
  }

  /**
   * Uniform qunatization of an image
   *
   * @param filePath
   * @return
   */
  public StringBuilder uniformQuantization( File filePath )
  {
    // TODO
    double[] Nq = { 16, 4, 4 };
    Mat newImage2 = Imgcodecs
        .imread( filePath.toString() );
    //Imgproc.cvtColor( newImage2, newImage2, Imgproc.COLOR_RGB2HSV );//COLOR_RGB2HSV
    if ( frame.rbHSV.isSelected() )
      Imgproc.cvtColor( newImage2, newImage2, Imgproc.COLOR_RGB2HSV );//COLOR_RGB2HSV
    //if ( frame.rbYCrCB.isSelected() )
    //  Imgproc.cvtColor( newImage2, newImage2, Imgproc.COLOR_RGB2YCrCb );//COLOR_RGB2HSV
    ArrayList<Mat> lHSV = new ArrayList<Mat>( 3 );
    Core.split( newImage2, lHSV );
    // Getting 3 channels R,G,B
    Mat mH = lHSV.get( 0 );
    Mat mS = lHSV.get( 1 );
    Mat mV = lHSV.get( 2 );
    // Hue channel
    Core.multiply( mH, new Scalar( Nq[0] / 255 ), mH );
    // Saturation channel
    Core.multiply( mS, new Scalar( Nq[1] / 255 ), mS );
    // Value channel
    Core.multiply( mV, new Scalar( Nq[2] / 255 ), mV );

    Size sizeA = mH.size();
    for( int i = 0; i < sizeA.height; i++ )
      for( int j = 0; j < sizeA.width; j++ )
      {
        //H
        double[] dataH = mH.get( i, j );
        dataH[0] = Math.floor( dataH[0] );
        mH.put( i, j, dataH );
        //S
        double[] dataS = mS.get( i, j );
        dataS[0] = Math.floor( dataS[0] );
        mS.put( i, j, dataS );
        //V
        double[] dataV = mV.get( i, j );
        dataV[0] = Math.floor( dataV[0] );
        mV.put( i, j, dataV );
      }
    /* Iq = I(:,:,1)*Nq(2)*Nq(3)+ I(:,:,2)*Nq(3)  + I(:,:,3) + 1 ; */
    Core.multiply( mH, new Scalar( Nq[1] * Nq[2] ), mH );
    Core.multiply( mS, new Scalar( Nq[2] ), mS );

    Core.add( mH, mS, mS );
    Core.add( mS, mV, mV );

    StringBuilder sbFeatureX = chHistogram( mV );
    return sbFeatureX;
    //Core.add(mV, new Scalar(1), mV);
    //imageViewer.show( mV, "Loaded image" );
    //System.out.println(mV.dump());

  }

  /**
   * Calculation of histogram feature for an image 1x256 dim
   *
   * @param newImage
   * @return
   */
  private StringBuilder chHistogram( Mat newImage )
  {
    double numberOfPixels = newImage.size().height * newImage.size().width;
    double count = 0;
    int ncount = 0;
    ArrayList<Double> featX = new ArrayList<Double>();
    StringBuilder sbFeatureX = new StringBuilder();
    List<Mat> matList = new LinkedList<Mat>();
    matList.add( newImage );
    Mat histogram = new Mat();
    MatOfFloat ranges = new MatOfFloat( 0, 256 );

    // Histogram calculation
    Imgproc.calcHist(
        matList,
        new MatOfInt( 0 ),
        new Mat(),
        histogram,
        new MatOfInt( 255 ),
        ranges );

    Size sizeA = histogram.size();
    //System.out.println(histogram.dump());
    //System.out.println( histogram.size().height );
    for( int i = 0; i < sizeA.height; i++ )
      for( int j = 0; j < sizeA.width; j++ )
      {
        double[] data = histogram.get( i, j );
        count += data[0];
        ncount += 1;
        sbFeatureX.append( data[0] / numberOfPixels );
        sbFeatureX.append( " " );
        //featX.add( data[0] / numberOfPixels );
        //System.out.println(data[0]);
      }

    return sbFeatureX;
    //System.out.println( featX.toString() );
    //System.out.println( sb );
    //System.out.println( "Todal sum of all pixels:" + count / ( newImage.size().height * newImage.size().width ) );
  }

}