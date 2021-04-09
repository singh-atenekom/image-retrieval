package cbir.utils;

/**
 * Class is for the calculation of distances between Images
 *
 * @author Shiwang SIngh
 *
 */
public class Distance
{

  public Distance()
  {}

  public static void main( String[] args )
  {
    //ArrayList<Integer> h1 = new ArrayList<Integer>();
    //System.out.println(distL1(h1, h2));
  }

  /**
   * Manhattan distance
   * range [0, 1]
   *
   * @param h1
   * @param h2
   * @return
   */
  public static double distL1( String[] h1, String[] h2 )
  {
    double sum = 0d;
    for( int i = 0; i < h1.length; i++ )
    {
      sum += Math.abs( ( Double.parseDouble( h1[i] ) - Double.parseDouble( h2[i] ) ) );
    }
    return sum / h1.length;
  }

  /**
   * Euclidean distance
   *
   * @param h1
   * @param h2
   * @return
   */
  static double distL2( String[] h1, String[] h2 )
  {
    double sum = 0d;
    for( int i = 0; i < h1.length; i++ )
    {
      sum += ( ( Double.parseDouble( h1[i] ) - Double.parseDouble( h2[i] ) ) )
          * ( ( Double.parseDouble( h1[i] ) - Double.parseDouble( h2[i] ) ) );
    }
    return Math.sqrt( sum ) / h1.length;
  }

  /**
   * Jeffrey Divergence or Jensen-Shannon divergence (JSD) from
   * Deselaers, T.; Keysers, D. & Ney, H. Features for image retrieval: an
   * experimental comparison Inf. Retr., Kluwer Academic Publishers, 2008, 11,
   * 77-107
   *
   * @param h1
   * @param h2
   * @return
   */
  static double jsd( String[] h1, String[] h2 )
  {
    double sum = 0d;
    for( int i = 0; i < h1.length; i++ )
    {
      sum += Double.parseDouble( h1[i] ) > 0 ? Double.parseDouble( h1[i] )
          * Math.log( 2d * Double.parseDouble( h1[i] ) / ( Double.parseDouble( h1[i] ) + Double.parseDouble( h2[i] ) ) )
          : 0 +
          Double.parseDouble( h2[i] ) > 0 ? Double.parseDouble( h2[i] )
              * Math.log( 2d * Double.parseDouble( h2[i] )
                  / ( Double.parseDouble( h1[i] ) + Double.parseDouble( h2[i] ) ) ) : 0;
    }
    return sum / h1.length;
  }

  static double tanimoto( String[] h1, String[] h2 )
  {
    double result = 0;
    double tmp1 = 0;
    double tmp2 = 0;

    double tmpCnt1 = 0, tmpCnt2 = 0, tmpCnt3 = 0;

    for( int i = 0; i < h1.length; i++ )
    {
      tmp1 += Double.parseDouble( h1[i] );
      tmp2 += Double.parseDouble( h2[i] );
    }

    if ( tmp1 == 0 || tmp2 == 0 ) result = 100;
    if ( tmp1 == 0 && tmp2 == 0 ) result = 0;

    if ( tmp1 > 0 && tmp2 > 0 )
    {
      for( int i = 0; i < h1.length; i++ )
      {
        tmpCnt1 += ( Double.parseDouble( h1[i] ) / tmp1 ) * ( Double.parseDouble( h2[i] ) / tmp2 );
        tmpCnt2 += ( Double.parseDouble( h1[i] ) / tmp2 ) * ( Double.parseDouble( h2[i] ) / tmp2 );
        tmpCnt3 += ( Double.parseDouble( h1[i] ) / tmp1 ) * ( Double.parseDouble( h2[i] ) / tmp1 );

      }

      result = ( 100 - 100 * ( tmpCnt1 / ( tmpCnt2 + tmpCnt3
          - tmpCnt1 ) ) ); //Tanimoto
    }
    System.out.println( "Dist T val -> " + result );
    return result / h1.length;
  }
}
