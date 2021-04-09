package cbir.utils;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class StatusBar extends JLabel
{

  public StatusBar()
  {
    this.setBorder( BorderFactory.createLineBorder( Color.DARK_GRAY ) );

    // Für die Anzeige der ProgressBar nutzen wir hier ein BORDERLAYOUT
    this.setLayout( new BorderLayout() );
  }

  public void setMessage( String msg )
  {
    this.setText( "  " + msg );
  }

}
