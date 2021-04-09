package cbir.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import cbir.utils.Distance;
import cbir.utils.HistogramFeature;

public class Test
{

  public Test()
  {
    final File featureList = new File( getClass().getResource( "corel1000.txt" ).getPath() );
    System.out.println( featureList );
  }

  public void myFunc()
  {
    String sCurrentLine;
    HistogramFeature hsvHist = new HistogramFeature();
    StringBuilder queryFeatureX = hsvHist.uniformQuantization( new File( "C:/eclipseLuna/workspace/corelDB/0.jpg" ) );
    final File featureList = new File( getClass().getResource( "../feat.txt" ).getPath() );
    BufferedReader in = null;
    try
    {
      in = new BufferedReader( new FileReader( featureList.getAbsolutePath() ) );
    }
    catch ( FileNotFoundException e )
    {
      e.printStackTrace();
    }
    ArrayList<Double> nDistVal = new ArrayList<Double>();
    ArrayList<String> nImagePath = new ArrayList<String>();

    //progressBar.setValue( 0 );
    try
    {
      while ( ( sCurrentLine = in.readLine() ) != null )
      {
        String[] featX = sCurrentLine.split( ";" )[1].split( " " );
        nImagePath.add( sCurrentLine.split( ";" )[0] );

        System.out.println( "feat File" + sCurrentLine.split( ";" )[0] );
        System.out.println( "query:" + Arrays.toString( queryFeatureX.toString().split( " " ) ) );
        System.out.println( "feat:" + Arrays.toString( featX ) );
        System.out.println( Distance.distL1( featX, queryFeatureX.toString().split( " " ) ) );

        System.out.println( "stop" );
        break;

      }
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }

  }

  public static void listFilesForFolder( final File folder )
  {

    int count = 0;
    int i = 0;
    int j = 1;
    for( final File fileEntry : folder.listFiles() )
    {
      if ( count > 100 )
        break;
      if ( fileEntry.isDirectory() )
      {
        listFilesForFolder( fileEntry );
      }
      else
      {
        //System.out.println( fileEntry.getAbsolutePath() );
        System.out.printf( "(%d, %d)", i - 1, j - 1 );
        if ( count % 10 == 0 )
        {
          i++;
          if ( j > 2 )
            j = 1;
        }
        else j++;

        System.out.printf( "(%d, %d)", i - 1, j - 1 );
        count++;
      }
    }
  }

  public static void main( String[] args )
  {
    Test nTest = new Test();
    nTest.myFunc();

    /*

    ArrayList<Double> nfit = new ArrayList<Double>();
    nfit.add( 1.0 );
    nfit.add( 3.0 );
    nfit.add( 2.0 );
    nfit.add( 4.0 );
    nfit.add( 6.0 );

    ArrayList<Double> nstore = new ArrayList<Double>( nfit ); // may need to be new ArrayList(nfit)
    Collections.sort( nfit, Collections.reverseOrder() );
    int[] indexes = new int[nfit.size()];
    for( int n = 0; n < nfit.size(); n++ )
    {
      indexes[n] = nstore.indexOf( nfit.get( n ) );
      System.out.println( nfit.get( n ) );
    }
    System.out.println( Arrays.toString( indexes ) );
    System.out.println();

     */
    //for(File f: imDir.getCanonicalFile())
    //  System.out.println(f);
    //final File folder = new File( Test.class.getResource( "../image/corelDB" ).getPath() );
    //listFilesForFolder( folder );
  }

}
