import java.io.*;
import java.util.*;
import org.json.*;
import java.time.*;

/*
TODO:
  Implementation of Math
  Possibley implement the keywords: he/she/it
*/

public class English
{
  public JSONObject Variables = new JSONObject();
  private ArrayList Keywords = ToArrayList( new String[]
  {
    "add",
    "ask-user",
    "while",
    "then",
    "say",
    "print",
    "println",
    "change",
    "make",
    "read-file",
    "length",
    "set",
    "if"
  });
  private ArrayList Colors = ToArrayList( new String[] {
    "blue",
    "red",
    "pink",
    "violet",
    "white",
    "black",
    "brown",
    "orange",
    "green",
    "grey"
  } );
  
  public English() {};
  public English( JSONObject Variables )
  {
    this.Variables = Variables;
  }
  public English( boolean ExtraVars, boolean debug )
  {
    try
    {
      if ( ExtraVars )
      {
        this.Variables.put( "alphabet", "abcdefghijklmnopqrstuvwxyz" );
        this.Variables.put( "numbers", "1234567890" );
        this.Variables.put( "hi", "hello" );
        this.Variables.put( "hello", "hi" );
      }
      this.Variables.put( "DEBUG", debug ? "yes" : "no" );
    }
    catch (JSONException e)
    {}
  }
  public String Run( String[] Code )
  {
    String ret = "1: ";
    for( int Line = 0; Line < Code.length; Line++ )
    {
      ret += Execute( Code[ Line ], Code[ Line ] ) + "\n" + ( Line + 2 ) + ": ";
      if ( Line == Code.length - 1 ) ret += "[ Program Finished ]";
    }
    return ret;
  }
  
  public String Execute( String Code )
  {
    return Execute( Code, Code );
  }
  
  public String Execute( String Code, String OriginalCode )
  {
    if ( Code.startsWith( "-S " ) ) return Code.replace( "-S ", "" );
    
    Code = RemoveIfStart( Code, "what is the ", "" );
    Code = RemoveIfStart( Code, "what is ", "" );
    Code = RemoveIfStart( Code, "ask the user ", "ask-user " );
    OriginalCode = RemoveIfStart( Code, "ask the user to ", "ask-user " );
    Code = OriginalCode;
    //Code = RemoveIfStart( Code, "please"
    
    Code = RemoveIfStart( Code, "the sum of ", "sum ");
    Code = RemoveIfStart( Code, "sum of ", "sum " );
    
    Code = RemoveIfStart( Code, "read the file in ", "read-file" );
    Code = RemoveIfStart( Code, "read the file ", "read-file " );
    Code = RemoveIfStart( Code, "read file ", "read-file " );
    
    Code = RemoveIfStart( Code, "the length of the ", "length " );
    Code = RemoveIfStart( Code, "the length of ", "length " );
    Code = RemoveIfStart( Code, "length of the ", "length " );
    Code = RemoveIfStart( Code, "length of ", "length " );
    
    Code = RemoveIfStart( Code, "the size of the ", "length " );
    Code = RemoveIfStart( Code, "the size of ", "size " );
    Code = RemoveIfStart( Code, "size of ", "size " );
    
    Code = RemoveIfStart( Code, "set the value of ", "set " );
    Code = RemoveIfStart( Code, "set the number of ", "set " );
    Code = RemoveIfStart( Code, "set the ammount of ", "set " );
    Code = RemoveIfStart( Code, "set the ", "set " );
    Code = RemoveIfStart( Code, "make the ", "make " );
    Code = RemoveIfStart( Code, "change the ", "change " );
    
    Code = RemoveIfStart( Code, "if the ", "if " );
    Code = RemoveIfStart( Code, "say the ", "say ");
    Code = RemoveIfStart( Code, "print the ", "print " );
    Code = RemoveIfStart( Code, "while the ", "while " );
    
    if ( Keywords.indexOf( Code.split( " " )[ 0 ].toLowerCase() ) > -1 )
    {
      int os = Code.split( " " )[ 0 ].toLowerCase().length();
      Code = RemoveIfStart( Code, os, "number of ", "" );
      Code = RemoveIfStart( Code, os, "ammount of " , "" );
      Code = RemoveIfStart( Code, os, "pieces of ", "" );
    }
    
    Code = RemoveIfStart( Code, "make a variable named ", "set-b " );
    Code = RemoveIfStart( Code, "make a variable called ", "set-b " );
    Code = RemoveIfStart( Code, "create a variable named ", "set-b " );
    Code = RemoveIfStart( Code, "create a variable called ", "set-b " );
    
    Code = RemoveIfStart( Code, "how many ", "how-many " );
    
    boolean IsQuestion = Code.endsWith( "?" );
    
    Code = ReplaceIfEnds( Code, ".", "" );
    Code = ReplaceIfEnds( Code, "?", "" );
    Code = Code.replaceAll( "  ", " " );
    //Code = RemoveIfStart( Code, "the ", "" ); Could break the code
    
    if ( Code.endsWith( " -RO" ) ) return Code;
    
    if ( Code.indexOf( " of the " ) > -1 )
    {
      String a = Code.substring( Code.indexOf( " of the " ) + 8 );
      if ( ParseSentence( a ) != "no" ) Code = Code.replace( " of the ", " of " );
    }
    else if ( Code.indexOf( " to the " ) > -1 )
    {
      String a = Code.substring( Code.indexOf( " to the " ) + 8 );
      if ( ParseSentence( a ) != "no" ) Code = Code.replace( " to the ", " to " );
    }
    else if ( Code.indexOf( " and the " ) > -1 )
    {
      String a = Code.substring( Code.indexOf( " and the " ) + 9 );
      if ( ParseSentence( a ) != "no" ) Code = Code.replace( " and the ", " and " );
    }
    
    String ret = "";
    
    String[] ParsedCode = Code.split( " " );
    ArrayList ParsedCode2 = ToArrayList( ParsedCode );
    ArrayList ParsedCode3 = ToArrayList( Code.toLowerCase().split( " " ) );
    
    int Until = ParsedCode2.indexOf( "END" ) > -1 ?
      ParsedCode2.indexOf( "END" ) - 1 : ParsedCode.length - 1;
    
    if ( ParsedCode.length > 2 && GetNearestKeyword( ParsedCode, new String[] {} ) == ParsedCode.length - 1 )
    {
      if ( ( IsInt( ParsedCode[ 0 ] ) || IsInt( ParsedCode[ 1 ] ) ) )
      {
        if ( ( IsInt( GetVariable( ParsedCode[ 0 ] ) ) || IsInt( GetVariable( ParsedCode[ 1 ] ) ) ) && ( ParsedCode3.indexOf( "letter" ) > - 1 || ParsedCode3.indexOf( "character" ) > - 1 ))
        {
          String Variable = ParseSentence( Slice( ParsedCode, ParsedCode3.indexOf( "of" ) + 1, Until ) );
          int Index = IsInt( ParsedCode[ 1 ] ) ? ParseInt( ParsedCode[ 1 ] ) : ParseInt( ParsedCode[ 0 ] );
          
          return "" + Variable.charAt( Index - 1 );
        }
        else
        {
          return "no";
        }
      }
    }
    
    switch( ParsedCode[ 0 ].toLowerCase() )
    {
      case "then":
        return Execute( Slice( ParsedCode, 1, ParsedCode.length - 1 ), OriginalCode );
      case "say":
      case "print":
      case "println":
        if ( Code.indexOf( ":" ) == -1 ) // might cause the code to break
        {
          ArrayList ToOutput = ToArrayList( Slice( ParsedCode, 1, ParsedCode.length - 1 ).split( "AND" ) );
          for( int ToO = 0; ToO < ToOutput.toArray().length; ToO++ )
          {
            ret += Execute( ( String ) ToOutput.get( ToO ) ) + ( ToO == ToOutput.toArray().length - 1 ? "" : " " );
          }
          ret += ParsedCode[ 0 ].toLowerCase() == "println" ? "\n" : "";
        }
        else
        {
          ret += Slice( ParsedCode, 1, IndexOf1( ParsedCode, ":" ) ) + " ";
          ret += Execute( Slice( ParsedCode, IndexOf1( ParsedCode, ":" ) + 1, ParsedCode.length - 1 ) );
        }
      break;
      case "ask-user":
        System.out.print( Execute( Slice( ParsedCode, 1, ParsedCode.length - 1 ) ) + ": " );
        SetVariable( new String[] { "user", "answer" }, new Scanner( System.in ).nextLine(), 1 );
      break;
      case "if":
      {
        if ( ParsedCode[ 1 ].toLowerCase().equals( "yes" ) ) return "yes";
        else if ( ParsedCode[ 1 ].toLowerCase().equals( "no" ) ) return "no";
        else if ( ParsedCode.length == 2 && HasVariable( ParsedCode[ 1 ] ) ) return GetVariable( ParsedCode[ 1 ] );
        
          Code = Code.replaceAll( " or else if not ", " else " )
        .replaceAll( " or else ", " else " )
        .replaceAll( " but if not ", " else " )
        .replaceAll( " if not ", " else " );
        
        ParsedCode = Code.split( " " );
        ParsedCode2 = ToArrayList( ParsedCode );
        ParsedCode3 = ToArrayList( Code.toLowerCase().split( " " ) );
        
        String TheIf = Slice( ParsedCode, 1, ParsedCode.length - 1);// GetNearestKeyword( ToArrayList( Slice( ParsedCode, 1, ParsedCode.length - 1 ).split( " " ) ), Until ) );
        TheIf = TheIf
          .replace( " are higher than ", " > " )
          .replace( " is higher than ", " > " )
          .replace( " higher than ", " > " )
          .replace( " are lower than ", " < " )
          .replace( " is lower than ", " < " )
          .replace( " lower than ", " < " )
          .replace( " is less or equal than ", " <= " )
          .replace( " are less or equal than ", " <= " )
          .replace( " less or equal than ", " <= " )
          .replace( " is more or equal than ", " >= " )
          .replace( " are more or equal than ", " >= " )
          .replace( " more or equal than ", " >= " )
          .replace( " is less than ", " < " )
          .replace( " are less than ", " < ")
          .replace( " less than ", " < " )
          .replace( " is more than ", " > " )
          .replace( " are more than ", " > " )
          .replace( " more than ", " > " )
          .replace( " is equal to ", " == " )
          .replace( " is not equal to ", " != " )
          .replace( " are equal to ", " == " )
          .replace( " are not equal to ", " != " )
          .replace( " equal to ", " == " )
          .replace( " not equal to ", " != " )
          .replace( " is ", " == " ) //WARNING: THIS MIGHT CAUSE AN ERROR
          .replace( " has ", " ~= " );
          
        String[] Full = TheIf.split( " " );
        ArrayList Full2 = ToArrayList( Full );
        
        int Start = Full2.indexOf( "==" ) != -1 ? Full2.indexOf( "==" ) : Full.length - 1 ;
        
        if ( Full2.indexOf( "!=" ) < Start && Full2.indexOf( "!=" ) != -1 ) Start = Full2.indexOf( "!=" );
        if ( Full2.indexOf( "<" ) < Start && Full2.indexOf( "<" ) != -1 ) Start = Full2.indexOf( "<" );
        if ( Full2.indexOf( ">" ) < Start && Full2.indexOf( ">" ) != -1 ) Start = Full2.indexOf( ">" );
        if ( Full2.indexOf( "<" ) < Start && Full2.indexOf( "<=" ) != -1 ) Start = Full2.indexOf( "<=" );
        if ( Full2.indexOf( ">" ) < Start && Full2.indexOf( ">=" ) != -1 ) Start = Full2.indexOf( ">=" );
        if ( Full2.indexOf( "~=" ) < Start && Full2.indexOf( "~=" ) != -1 ) Start = Full2.indexOf( "~=" );
        
        boolean IsValid = true;
        
        if ( Start == Full.length - 1 )
        {
          IsValid = false;
          Start = GetNearestKeyword( Full );
        }
        
        String[] Full3 = Slice( Full, Start + ( IsValid ? 1 :0 ), Full.length - 1 ).split( " " );
        
        String VariableA = Execute( Slice( Full, 0, Start - 1 ) );
        String VariableB = null;
        if ( IsValid ) VariableB = Execute( Slice( Full, Start + 1, GetNearestKeyword( Full, new String[] { "and", "add" } ) - 1 ) );
        
        String Yes = null;
        String No = null;
        
        if ( GetNearestKeyword( Full3, new String[] { "add", "sum", "read-file", "length" } ) != Full3.length - 1 )
        {
          Yes = Slice( Full3, GetNearestKeyword( Full3, new String[] { "add", "sum", "read-file", "length" } ), ToArrayList( Full3 ).indexOf( "else" ) > -1 ? ToArrayList( Full3 ).indexOf( "else" ) - 1 : Full3.length - 1 );
          Yes = Yes.trim().equals( "" ) ? null : Yes;
          if ( ToArrayList( Full3 ).indexOf( "else" ) > -1 ) No = Slice( Full3, ToArrayList( Full3 ).indexOf( "else" ) + 1, Full3.length - 1 );
        }
        
        if ( !IsValid )
          if ( HasVariable( Slice( Full, 0, Start - 1 ) ) ) return Yes != null ? Execute( Yes ) : "yes";
          else return No != null ? Execute( No ) : "no";
        
        if ( Full[ Start ].equals( "==" ) )
        {
          if ( VariableA.equals( VariableB ) ) return Yes != null ? Execute( Yes ) : "yes";
          else return No != null ? Execute( No ) :  "no";
        }
        else if ( Full[ Start ].equals( "!=" ) )
        {
          if ( !VariableA.equals( VariableB ) ) return Yes != null ? Execute( Yes ) : "yes";
          else return No != null ? Execute( No ) :  "no";
        }
        else if ( Full[ Start ].equals( "<" ) )
        {
          if ( !IsInt( VariableA ) || !IsInt( VariableB ) ) return "One or both variables are not integers";
          if ( ParseInt( VariableA ) < ParseInt( VariableB ) ) return Yes != null ? Execute( Yes ) : "yes";
          else return No != null ? Execute( No ) : "no";
        }
        else if ( Full[ Start ].equals( ">" ) )
        {
          if ( !IsInt( VariableA ) || !IsInt( VariableB ) ) return "One or both variables are not integers";

          if ( ParseInt( VariableA ) > ParseInt( VariableB ) ) return Yes != null ? Execute( Yes ) : "yes";
          else return No != null ? Execute( No ) : "no";
        }
        else if ( Full[ Start ].equals( "<=" ) )
        {
          if ( !IsInt( VariableA ) || !IsInt( VariableB ) ) return "One or both variables are not integers";

          if ( ParseInt( VariableA ) <= ParseInt( VariableB ) ) return Yes != null ? Execute( Yes ) : "yes";
          else return No != null ? Execute( No ) : "no";
        }
        else if ( Full[ Start ].equals( ">=" ) )
        {
          if ( !IsInt( VariableA ) || !IsInt( VariableB ) ) return "One or both variables are not integers";

          if ( ParseInt( VariableA ) >= ParseInt( VariableB ) ) return Yes != null ? Execute( Yes ) : "yes";
          else return No != null ? Execute( No ) : "no";
        }
        else if ( Full[ Start ].equals( "~=" ) )
        {
          if ( VariableA.indexOf( VariableB ) > -1 ) return Yes != null ? Execute( Yes ) : "yes";
          else return No != null ? Execute( No ) : "no";
        }
        else return "Sorry, But the oprator you used isn't supported yet";
      }
      case "does":
      {
        Code = Code.replace( " have any ", " have " ).replace( " have an ", " have " );
        
        String ObjectName = Code.substring( 5, GetNearest( Code, new String[] { "have" } ) - 1 );
        String VariableName = Code.substring( Code.lastIndexOf( " have " ) + 6 );
        
        ret = GetVariableFromObject( ObjectName, VariableName ) == "no" ? "no" : "yes";
      }
      break;
      case "how-many":
      {
        String VariableName = Code.substring( 9, Code.indexOf( " does " ) );
        String ObjectName = Code.substring( Code.indexOf( " does " ) + 6, Code.indexOf( " have" ) );
        
        if ( IsInt( GetVariableFromObject( ObjectName, VariableName ) ) ) ret = GetVariableFromObject( ObjectName, VariableName );
        else ret = "0";
      }
      break;
      case "set":
      case "make":
      case "change":
        Code = Code.replaceAll( " in the ", " in " )
        .replaceAll( " of the ", " in " );
          
        ParsedCode = Code.split( " " );
        ParsedCode2 = ToArrayList( ParsedCode );
        ParsedCode3 = ToArrayList( Code.toLowerCase().split( " " ) );
        
        if ( Code.indexOf( "'s" ) == -1 && ( ParsedCode2.indexOf( "=" ) > -1 || ParsedCode3.indexOf( "to" ) > -1 ) )
        {
          if ( ParsedCode2.lastIndexOf( "=" ) > -1 )
          {
            String VariableName = Slice( ParsedCode, 1, ParsedCode2.lastIndexOf( "=" ) - 1 );
            String VariableValue = Execute( Slice( ParsedCode, ParsedCode2.lastIndexOf( "=" ) + 1, Until ), Slice( ParsedCode, ParsedCode2.lastIndexOf( "=" ) + 1, Until ) );
            SetVariable( VariableName, VariableValue );
            
            if ( GetVariable( "DEBUG" ).equals( "yes" ) ) ret = "Changed: " + VariableName + "'s value to " + VariableValue;
          }
          else if ( ParsedCode2.indexOf( "to" ) > -1 )
          {
            String VariableName = Slice( ParsedCode, 1, ParsedCode2.indexOf( "to" ) - 1 );
            String VariableValue = Execute( Slice( ParsedCode, ParsedCode2.indexOf( "to" ) + 1, Until ) ,Slice( ParsedCode, ParsedCode2.indexOf( "to" ) + 1, Until ) );
            SetVariable( VariableName, VariableValue );
            
            if ( GetVariable( "DEBUG" ).equals( "yes" ) ) ret = "Changed: " + VariableName + "'s value to " + VariableValue;
          }
          else
          {
            return "I can't find the = or the \"to\" in your sentence";
          }
        }
        else if ( ParsedCode3.indexOf( "in" ) > -1 )
        {
          int Until1 = ParsedCode2.indexOf( "=" ) > -1 ? ParsedCode2.indexOf( "=" ) : ParsedCode2.indexOf( "to" );
          int Start = ParsedCode2.indexOf( "in" ) > - 1 ? ParsedCode2.indexOf( "in" ) : ParsedCode2.indexOf( "of" );
          
          if ( Until1 == -1 ) return "I can't find the = or the \"to\" in this sentence";
          
          Until1--;
          Start--;
          
          if ( Start < 0 ) return "I can't find the \"in\" or the \"of\" in this sentence";
          
          String VariableName = Slice( ParsedCode, 1, Start );
          String VariableValue = Execute( Slice( ParsedCode, Until1 + 2, Until ), Slice( ParsedCode, Until1 + 2, Until ) );
          String ObjectName = Slice( ParsedCode, Start + 2, Until1 );
          
          ChangeObject( ObjectName, VariableName, VariableValue );
          
          if ( GetVariable( "DEBUG" ).equals( "yes" ) ) ret = "Changed the \"" + VariableName + "\" of \"" + ObjectName + "\" to \"" + VariableValue + "\"";
        }
        else if ( Code.indexOf( "'s" ) > -1 )
        {
          if ( ParsedCode3.indexOf( "to" ) > -1 && ParsedCode3.indexOf( "of" ) > -1 )
          {
            String VariableName = Slice( ParsedCode, 1, ParsedCode3.indexOf( "of" ) - 1 );
            String ObjectName = Slice( ParsedCode, IndexOf1( ParsedCode, "'s" ), IndexOf1( ParsedCode, "'s" ) ).replace( "'s", "" );
            String ObjectName1 = Slice( ParsedCode, IndexOf1( ParsedCode, "'s" ) + 1, ParsedCode3.indexOf( "to" ) - 1 );
            String VariableValue = Slice( ParsedCode, ParsedCode3.indexOf( "to" ) + 1, ParsedCode.length - 1 );
            
            try
            {
              JSONObject a = Variables.has( ObjectName ) && Variables.getInt( ObjectName + "-Type" ) == 2 ? Variables.getJSONObject( ObjectName ) : new JSONObject();
              JSONObject b = a.has( ObjectName1 ) && a.getInt( ObjectName1 + "-Type" ) == 2 ? a.getJSONObject( ObjectName1 ) : new JSONObject();
                
              b.put( VariableName, VariableValue );
              b.put( VariableName + "-Type", 1 );
                  
              a.put( ObjectName1, b );
              a.put( ObjectName1 + "-Type", 2 );
                  
              Variables.put( ObjectName, a );
              Variables.put( ObjectName + "-Type", 2 );
            }
            catch( JSONException e ) {}
          }
          else
          {
            String ObjectName = Slice( ParsedCode, 1, IndexOf1( ParsedCode, "'s" ) );
            ObjectName = ReplaceIfEnds( ObjectName, "'s", "" );
            String[] VariableName = Slice( ParsedCode, IndexOf1( ParsedCode, "'s" ) + 1, ParsedCode2.indexOf( "to" ) - 1 ).split( " " );
            String VariableValue = ParseSentence( Slice( ParsedCode, ParsedCode2.indexOf( "to" ) + 1, ParsedCode.length - 1 ) );
            String[] VariablePath =  Join( new String[] { ObjectName }, VariableName );
            
            SetVariable( VariablePath, VariableValue, 1 );
            try
            {
              if ( GetVariable( "DEBUG" ).equals( "yes" ) ) ret = "Changed " + ObjectName + ">" + new JSONArray( VariableName ).join( ">" ) + " to \"" + VariableValue + "\"";
            }
            catch (JSONException e)
            {}
          }
        }
        else return "Failed to execute";
      break;
      case "set-b":
      {
        Code = Code.replace( "value of it to ", "value of " );
        
        int UseThis = Code.indexOf( "value of " ) > -1 ? Code.indexOf( "value of " ) : Code.indexOf( "value to ");
        
        String VariableName = UseThis == -1 ? Code.substring( 6 ) : Code.substring( 6, GetNearest( Code, new String[] { "with", "then", "and" } ) - 1 );
        String VariableValue = UseThis > -1 ? Code.substring( UseThis + 9 ) : "0";
        
        Execute( "set " + VariableName + " to " + VariableValue );
        
        if ( GetVariable( "DEBUG" ).equals( "yes" ) ) ret = "Changed: " + VariableName + "'s value to " + VariableValue;
      }
      break;
      case "rename":
        return "In Development";
      //break;
      case "read-file":
        try {
          File FileToRead = new File( Slice( ParsedCode, 1, Until ) );
          Scanner ScannerForTheFile = new Scanner( new FileInputStream( FileToRead ) );
          while( ScannerForTheFile.hasNext() ) ret += ScannerForTheFile.next();
        } catch( Exception e )
        {
          return "no";
        }
      break;
      case "while":
        ArrayList ToExecute = ToArrayList( Slice( ParsedCode, GetNearestKeyword( ToArrayList( Slice( ParsedCode, 1, Until ).split( " " ) ) ) + 1, Until ).split( " ALSO " ) );
        
        while( true )
        {
          String ttt = Execute( "if " + Slice( ParsedCode, 1, GetNearestKeyword( ToArrayList( Slice( ParsedCode, 1, Until ).split( " " ) ) ) ) );
          if ( ttt == "no" || ttt == "One or both variables are not integers"  )
          {
            ret = ttt == "One or both variables are not integers" ? "Failed to use while" : ret;
            break;
          }
          for( int i = 0; i < ToExecute.toArray().length; i++ ) ret += Execute( ( String ) ToExecute.get( i ) );
        }
      break;
      case "add":
      case "sum":
      {
        int StartOfB = ParsedCode3.indexOf( "and" ) > -1 ? ParsedCode3.indexOf( "and" ) : ParsedCode3.indexOf( "to" );
        if ( StartOfB == -1 ) return "Can't find the \"and\" or the \"to\" in this sentence";
        
        String VariableA = ParseSentence( Slice( ParsedCode, ParsedCode[ 1 ].equals( "the" ) ? 2 : 1, StartOfB - 1 ) );
        String VariableB = ParseSentence( Slice( ParsedCode, StartOfB + 1, Until ) );
        
        if ( !IsInt( VariableA ) || !IsInt( VariableB ) ) return "Both or one of the variables are not integers";
        
        ret = "" + ( ParseInt( VariableA ) + ParseInt( VariableB ) );
        
        if ( !ParsedCode[ 0 ].toLowerCase().equals( "sum" ) )
        {
          if ( ParsedCode3.indexOf( "and" ) > -1 ) Execute( "set " + Slice( ParsedCode, 1, ParsedCode3.indexOf( "and" ) - 1 ) + " to " + ( ParseInt( VariableA ) + ParseInt( VariableB ) ) );
          else Execute( "set " + Slice( ParsedCode, StartOfB + 1, Until ) + " to " + ( ParseInt( VariableA ) + ParseInt( VariableB ) ) );
        }
      }
      break;
      case "give":
      {
        String[] VariablePath = Join( Slice1( ParsedCode, 1, GetNearestNumber( ParsedCode ) - 1 ), Slice1( ParsedCode, GetNearestNumber( ParsedCode ) + 1, ParsedCode.length - 1 ) );
        int VariableValue = ParseInt( Slice( ParsedCode, GetNearestNumber( ParsedCode ), GetNearestNumber( ParsedCode ) ) );
        int CurrentValue = IsInt( GetVariable( VariablePath ) ) ? ParseInt( GetVariable( VariablePath ) ) : 0;
        
        SetVariable( VariablePath, "" + ( VariableValue + CurrentValue ), 1 );
      }
      break;
      case "size":
      case "length":
        return "" + Execute( Slice( ParsedCode, 1, ParsedCode.length - 1 ), Slice( ParsedCode, 1, ParsedCode.length - 1 ) ).length();
      default:
        ret = ParseSentence( Slice( ParsedCode, 0, ParsedCode.length - 1 ) );
    }
    
    return ret;
  }

  private int GetNearest( String code, String[] string )
  {
    int ret = code.length() - 1;
    for( int i = 0; i < string.length; i++ )
    {
      if ( code.indexOf( string[ i ] ) < ret && code.indexOf( string[ i ] ) != -1 ) ret = code.indexOf( string[ i ] );
    }
    return ret;
  }
  
  private String GetVariable( String Name )
  {
    try
    {
      return Variables.getString( Name );
    }
    catch( JSONException e )
    {
      return Name;
    }
  }
  private void SetIt( String[] Path )
  {
    try
    {
      Variables.put( "it", new JSONArray( Path ) );
      Variables.put( "her", new JSONArray( Path ) );
      Variables.put( "his", new JSONArray( Path ) );
    }
    catch( JSONException e )
    {}
  }
  private void SetVariable( String Name, String Value )
  {
    try
    {
      Variables.put( Name, Value );
      Variables.put( Name + "Type", 1 );
    }
    catch( Exception e ) {}
  }
  public void SetVariable( String[] Path, String Value, int Type )
  {
    try
    {
      if ( Path.length == 1 )
      {
        Variables.put( Path[ 0 ], Type == 1 ? Value : Type == 2 ? new JSONObject( Value ) : new JSONArray( Value ) );
        Variables.put( Path[ 0 ] + "-Type", Type );
      }
      else
      {
        JSONObject[] ElPath = new JSONObject[ Path.length ];
        for( int Depth = 0; Depth < Path.length; Depth++ )
        {
          JSONObject Parent = Depth == 0 ? Variables : ElPath[ Depth - 1 ];
          String Name = Path[ Depth ];
          JSONObject Child = Parent.has( Name ) ? Parent.getInt( Name + "-Type" ) == 2 ? Parent.getJSONObject( Name ) : new JSONObject() : new JSONObject();
          Parent.put( Name, Child );
          Parent.put( Name + "-Type", 2 );
          ElPath[ Depth ] = Child;
        }
        ElPath[ Path.length - 2 ].put( Path[ Path.length - 1 ], Type == 1 ? Value : Type == 2 ? new JSONObject( Value ) : new JSONArray( Value ) );
        ElPath[ Path.length - 2 ].put( Path[ Path.length - 1 ] + "-Type", Type );
      }
    }
    catch( JSONException e )
    {}
  }
  public String GetVariable( String[] Path )
  {
    try
    {
      JSONObject[] a = new JSONObject[ Path.length ];
      for( int b = 0; b < Path.length - 1; b++ )
      {
        a[ b ] = ( b == 0 ? Variables : a[ b - 1 ] ).getJSONObject( Path[ b ] );
      }
      return a[ Path.length - 2 ].getString( Path[ Path.length - 1 ] );
    }
    catch( JSONException e )
    {
      return "no";
    }
  }
  private boolean HasVariable( String Name )
  {
    return Variables.has( Name );
  }
  private void ChangeObject( String ObjectName, String VariableName, String NewValue )
  {
    try
    {
      if ( Variables.has( ObjectName ) )
      {
        if ( GetType( ObjectName ) == 2 )
        {
          JSONObject ObjectToChange = Variables.getJSONObject( ObjectName );
          ObjectToChange.put( VariableName, NewValue );
          ObjectToChange.put( VariableName + "-Type", 1 );
          Variables.put( ObjectName, ObjectToChange );
          Variables.put( ObjectName + "-Type", 2 );
        }
      }
      else
      {
        JSONObject ObjectToChange = new JSONObject( ObjectName );
        ObjectToChange.put( VariableName, NewValue );
        ObjectToChange.put( VariableName + "-Type", 1 );
        Variables.put( ObjectName, ObjectToChange );
        Variables.put( ObjectName + "-Type", 2 );
      }
    }
    catch( JSONException e )
    {
      try
      {
        Variables.put( ObjectName, new JSONObject( "{\"" + VariableName + "\":\"" + NewValue + "\",\"" + VariableName + "-Type\":1}" ) );
        Variables.put( ObjectName + "-Type", 2 );
      }
      catch( JSONException e1 ) {}
    }
  }
  private void ChangeArray( String ArrayName, int Index, String NewValue )
  {
    try
    {
      JSONArray ArrayToChange = Variables.getJSONArray( ArrayName );
      ArrayToChange.put( Index - 1, NewValue );
      Variables.put( ArrayName, ArrayToChange );
      Variables.put( ArrayName + "-Type", 3 );
    }
    catch( JSONException e )
    {
      try {
        JSONArray NewArray = new JSONArray();
        NewArray.put( Index, NewValue );
        Variables.put( ArrayName, NewArray );
        Variables.put( ArrayName + "-Type", 3 );
      } catch( JSONException e1 ) {}
    }
  }
  private String GetVariableFromArray( String ArrayName, String Index )
  {
    try
    {
      return Variables.getJSONArray( ArrayName ).getString( ParseInt( Index ) );
    }
    catch( JSONException e )
    {
      return "no";
    }
  }
  private String GetVariableFromObject( String ObjectName, String VariableName )
  {
    try
    {
      return Variables.getJSONObject( ObjectName ).getString( VariableName );
    }
    catch( JSONException e )
    {
      return "no";
    }
  }
  private String GetVariableFromObject( String ObjectName, String ObjectName2, String VariableName )
  {
    try
    {
      return Variables.getJSONObject( ObjectName ).getJSONObject( ObjectName2 ).getString( VariableName );
    }
    catch( JSONException e )
    {
      return "no";
    }
  }
  private int GetType( String VariableName )
  {
    try
    {
      return Variables.getInt( VariableName + "-Type" );
    }
    catch( Exception e )
    {
      return 0;
    }
  }
  private String ParseSentence( String Sentence )
  {
    String[] Sentence4 = Sentence.split( " " );
    ArrayList Sentence3 = ToArrayList( Sentence.toLowerCase().split( " " ) );
    
    boolean Thingy = ( IndexOf1( Sentence4, "'s" ) == Sentence4.length - 1 );
    int Until = Thingy ? Sentence4.length - 1 : IndexOf1( Sentence4, "'s" ) ;
    
    if ( Sentence.indexOf( "'s" ) > -1 )
    {
      if ( Sentence3.indexOf( "of" ) > -1 )
      {
        String VariableName = Slice( Sentence4, Sentence3.indexOf( "of" ) + 1, IndexOf1( Sentence.toLowerCase().split( " " ), "'s" ) ).replace( "'s", "" );
        if ( !Thingy )
        {
          String VariableName1 = Slice( Sentence4, IndexOf1( Sentence4, "'s" ) + 1, Sentence4.length - 1 );
          String VariableKey = Slice( Sentence4, 0, Sentence3.indexOf( "of" ) - 1 );
        
          return GetVariableFromObject( VariableName, VariableName1, VariableKey );
        }
        else
        {
          String VariableKey = Slice( Sentence4, IndexOf1( Sentence4, "'s" ) + 1, Sentence4.length - 1 );
          return GetVariableFromObject( VariableName, VariableKey );
        }
      }
      else
      {
        String VariableName = ReplaceIfEnds( Slice( Sentence4, 0, IndexOf1( Sentence4, "'s" ) ), "'s", "" );
        String VariableKey[] = Slice( Sentence4, IndexOf1( Sentence4, "'s" ) + 1, Sentence4.length - 1 ).split( " " );

        return GetVariable( Join( new String[] { VariableName }, VariableKey ) );
      }
    }
    
    int Start = Sentence3.indexOf( "in" ) != -1 ? Sentence3.indexOf( "in" ) : Sentence3.indexOf( "of" );
    if ( Start > -1 )
    {
      String ret = GetVariableFromObject( Slice( Sentence4, Start + 1, Sentence4.length - 1 ), Slice( Sentence4, 0, Start - 1 ) );
      return ret.equals( "no" ) ? Sentence : ret;
    }
    else
    {
      return GetVariable( Sentence );
    }
  }
  
  private String RemoveIfStart( String ToCheck, String StartsWith, String ReplaceWith )
  {
    if ( ToCheck.startsWith( StartsWith ) ) return ToCheck.replace( StartsWith, ReplaceWith );
    else return ToCheck;
  }
  private String RemoveIfStart( String ToCheck, int Offset, String StartsWith, String ReplaceWith )
  {
    if ( ToCheck.startsWith( StartsWith, Offset ) ) return ToCheck.replace( StartsWith, ReplaceWith );
    else return ToCheck;
  }
  private String ReplaceIfEnds( String ToCheck, String EndsWith, String Replacement )
  {
    if ( ToCheck.endsWith( EndsWith ) )
    {
      
      String a = ToCheck.substring( 0, ToCheck.length() - EndsWith.length() );
      String b = ToCheck.substring( ToCheck.length() - EndsWith.length() );
      
      if ( b.equalsIgnoreCase( EndsWith ) ) b = Replacement;
      
      return a + b;
    }
    else return ToCheck;
  }
  private int GetNearestKeyword( String[] a )
  {
    return GetNearestKeyword( ToArrayList( a ) );
  }
  private String[] SplitBy( String[] ToSplit, String ByWhat )
  {
    int len = 0;
    for( int i = 0; i < ToSplit.length; i++ )
    {
      if ( ToSplit[ i ].indexOf( ByWhat ) > -1 ) len++;
    }
    String[] ret = new String[ len ];
    for( int i = 0, i1 = 0; i < ToSplit.length; i++ )
    {
      if ( ToSplit[ i ].indexOf( ByWhat ) > -1 )
      {
        ret[ i1 ] = ToSplit[ i ];
        i1++;
      }
    }
    return ret;
  }
  private String[] Join( String[] a, String[] b )
  {
    String[] c = new String[ a.length + b.length ];
    for( int d = 0; d < a.length; d++ )
    {
      c[ d ] = a[ d ];
    }
    for( int d = a.length, e = 0; d < a.length + b.length; d++ )
    {
      c[ d ] = b[ e ];
      e++;
    }
    return c;
  }
  private int GetNearestKeyword( ArrayList a, int len )
  {
    int ret = len;
    
    for( int i = 0; i < Keywords.toArray().length; i++ )
    {
      if ( a.indexOf( ( String ) Keywords.get( i ) ) <  ret && a.indexOf( ( String ) Keywords.get( i ) ) != -1 ) ret = a.indexOf( ( String ) Keywords.get( i ) );
    }
    
    return ret;
  }
  private int GetNearestKeyword( ArrayList a )
  {
    int ret = a.toArray().length;

    for( int i = 0; i < Keywords.toArray().length; i++ )
    {
      if ( a.indexOf( ( String ) Keywords.get( i ) ) <  ret && a.indexOf( ( String ) Keywords.get( i ) ) != -1 ) ret = a.indexOf( ( String ) Keywords.get( i ) );
    }

    return ret;
  }
  private int GetNearestKeyword( String[] a, String[] Except )
  {
    int ret = a.length;

    for( int i = 0; i < Keywords.toArray().length; i++ )
    {
      if ( ToArrayList( Except ).indexOf( Keywords.get( i ) ) == -1 && ToArrayList( a ).indexOf( ( String ) Keywords.get( i ) ) <  ret && ToArrayList( a ).indexOf( ( String ) Keywords.get( i ) ) != -1 ) ret = ToArrayList( a ).indexOf( ( String ) Keywords.get( i ) );
    }

    return ret;
  }
  private int GetNearestNumber( String[] a )
  {
    int ret = a.length - 1;
    for( int i = 0; i < a.length; i++ )
    {
      if ( IsInt( a[ i ] ) )
        if ( i < ret ) ret = i;
    }
    return ret;
  }
  private int ParseInt( String ToParse )
  {
    try
    {
      ArrayList nums = ToArrayList( new String[] { "0","1","2","3","4","5","6","7","8","9" } );
     
      String pp = "";
      
      for( int i = 0; i < ToParse.length(); i++ )
      {
        if ( nums.indexOf( "" + ToParse.charAt( i ) ) > -1) pp += ToParse.charAt( i );
      }
      
      return ToParse.startsWith( "-" ) ? -Integer.parseInt( pp ) : Integer.parseInt( pp );
    }
    catch( Exception e )
    {
      return 0;
    }
  }
  private boolean IsInt( String ToCheck )
  {
    try
    {
      Integer.parseInt(
        ToCheck.replace( "st", "" )
        .replace( "nd", "" )
        .replace( "rd", "" )
        .replace( "th", "" )
        .replace( "first", "1" )
        .replace( "second", "2" )
        .replace( "third", "3" )
      );
      return true;
    }
    catch( Exception e )
    {
      return false;
    }
  }
  private int IndexOf1( String[] Array, String ToFind )
  {
    for( int Index = 0; Index < Array.length; Index++ )
    {
      if ( Array[ Index ].indexOf( ToFind ) > -1 ) return Index;
    }
    return -1;
  }
  private String Slice( String[] Array, int From, int To )
  {
    String ret2 = "";
    ArrayList ret = new ArrayList();
    for( int Index = 0; Index < Array.length; Index++ )
    {
      ret.add( Array[ Index ] );
    }
    List<Object> ret1 = ret.subList( From, To + 1 );
    Iterator it = ret1.iterator();
    while( it.hasNext() ) ret2 += it.next() + " ";
    return ret2.trim();
  }
  private String[] Slice1( String[] Array, int From, int To )
  {
    String[] ret = new String[ ( To + 1 ) - From ];
    for( int i = 0, i1 = 0; i < Array.length; i++ )
    {
      if ( i >= From && i <= To )
      {
        ret[ i1 ] = ReplaceIfEnds( Array[ i ], "'s", "" );
        i1++;
      }
    }
    return ret;
  }
  private ArrayList ToArrayList( String[] a )
  {
    ArrayList b = new ArrayList();
    for( int c = 0; c < a.length; c++ )
    {
      b.add( a[ c ] );
    }
    return b;
  }
}
