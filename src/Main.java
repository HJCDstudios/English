import java.lang.reflect.*;
import java.util.*;
import java.io.*;

public class Main
{
  private static English en = new English( false, false );
	private static Scanner InputScanner = new Scanner( System.in );
  public static void main( String[] args )
	{
    System.out.println( "English Interpreter\n" );
    System.out.print( "Enter sentence:\n> " );
    loop( InputScanner.nextLine() );
  }
  private static void loop( String exec )
  {
    System.out.println( "Output:\n" + en.Execute( exec ) + "\n" );
    System.out.print( "Enter sentence:\n> " );
    loop( InputScanner.nextLine() );
  }
}
