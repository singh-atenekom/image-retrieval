package cbir.test;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCVHello
{
  public static void main( String[] args )
  {
    System.out.println( System.getProperty( "java.library.path" ) );
    System.loadLibrary( "lib/x64/opencv_java300" );
    Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
    System.out.println( "mat = " + mat.dump() );
  }
}