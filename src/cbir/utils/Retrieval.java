package cbir.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import cbir.GUI;

/**
 * Class Retrieval for the retrieve of all images with given query
 * Distance calulation
 *
 * @author Alfa
 *
 */
public class Retrieval
{

  private ArrayList<String> sortedNImagePath;
  private ArrayList<String> sortedNImageDistanceValues = new ArrayList<String>();
  private GUI               frame;

  public ArrayList<String> getSortedNImagePath()
  {
    return sortedNImagePath;
  }

  public Retrieval( GUI frame )
  {
    this.frame = frame;
    dateiLesenStringBuilder();
  }

  private void dateiLesenStringBuilder()
  {
    //"../feat.txt"
    // fix for whole project
    final File featureList = new File( getClass().getResource( "../feat.txt" ).getPath() );

    Runnable runner = new Runnable()
    {
      BufferedReader in = null;

      @Override
      public void run()
      {
        try
        {
          System.out.println( "fquery ->" + frame.queryFeatureX.toString() );
          String sCurrentLine;
          int lines = 0;
          BufferedReader newbw = new BufferedReader( new FileReader( featureList.getAbsolutePath() ) );
          while ( newbw.readLine() != null )
            lines++;

          in = new BufferedReader( new FileReader( featureList.getAbsolutePath() ) );
          ArrayList<Double> nDistVal = new ArrayList<Double>();
          ArrayList<String> nImagePath = new ArrayList<String>();
          frame.lp5LabelStatus.setText( "<html><font color='orange'><b> Suche läuft ...</b></font></html>" );
          int count = 0;
          frame.progressBar.setMaximum( lines );
          //progressBar.setValue( 0 );
          while ( ( sCurrentLine = in.readLine() ) != null )
          {
            String[] featX = sCurrentLine.split( ";" )[1].split( " " );
            nImagePath.add( sCurrentLine.split( ";" )[0] );

            // Retrieval with Manhatten distance
            if ( frame.rbL1.isSelected() )
            {
              nDistVal.add(
                  Distance.distL1( featX, frame.queryFeatureX.toString().split( " " ) )
                  );
            }
            // Retrieval with Euclidean distance
            if ( frame.rbL2.isSelected() )
            {
              nDistVal.add(
                  Distance.distL2( featX, frame.queryFeatureX.toString().split( " " ) )
                  );
            }
            // Retrieval with Tanimoto distance
            if ( frame.rbJSD.isSelected() )
            {
              nDistVal.add(
                  Distance.jsd( featX, frame.queryFeatureX.toString().split( " " ) )
                  );
            }

            count++;
            frame.progressBar.setValue( count );

          }

          // Sorting out the distances and finding the index
          ArrayList<Double> nstore = new ArrayList<Double>( nDistVal ); // may need to be new ArrayList(nfit)
          //Normalizing distance
          double maxDistVal = Collections.max( nDistVal );
          // sorting list in descending order
          Collections.sort( nDistVal );
          sortedNImagePath = new ArrayList<String>();
          //int[] indexes = new int[nDistVal.size()];
          for( int n = 0; n < nDistVal.size(); n++ )
          {
            int index = nstore.indexOf( nDistVal.get( n ) );
            //System.out.println( "dist:" + nImagePath.get( index ) );
            sortedNImagePath.add( nImagePath.get( index ) );
            sortedNImageDistanceValues.add( String.valueOf( nDistVal.get( n ) / maxDistVal ) );
            frame.progressBar.setValue( count + n );
          }
          //System.out.println( "Distance values -> " + sortedNImageDistanceValues.toString() );
          // Show in Tabs
          frame.resultTabbedPane.createImagesInTab(
              sortedNImagePath,
              sortedNImageDistanceValues );
          frame.lp5LabelStatus.setText( "<html><font color='green'><b> Ergebnisbilder erstellt !</b></font></html>" );
          //System.out.println( Arrays.toString( indexes ) );
          //textArea.append( sb.toString() );
        }
        catch ( Exception ex )
        {
          System.out.println( ex.getMessage() );
        }

        if ( in != null ) try
        {
          in.close();
        }
        catch ( IOException e )
        {
          e.printStackTrace();
        }

      }
    };
    Thread t = new Thread( runner, "Code Executer" );
    t.start();

  }
}
