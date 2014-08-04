package org.nashua.tt151.libraries.parsers;

import org.nashua.tt151.util.BitwiseTools;
import org.nashua.tt151.util.MathTools;
import org.nashua.tt151.util.StringTools;

/**
 * This class contains classes and enums for parsing messages transmitted from 
 * the laptop/robot. Use the static class Parsing for parsing messages.
 * A message is a string containing the command, key, value and args.
 * For more information on the structure of the message view the wiki.
 * @author Brennan Ringey
 * @version 1.5
 */
public class ProtocolParsing {
    
    private ProtocolParsing() {}
    
    /**
     * A class representing a pseudo-enum for the avaiable commands
     */
    public static class Command {
    
        public char shorthand;
        public byte bitsum;
        
        private Command(char c, byte b) {
            this.shorthand = c;
            this.bitsum = b;
        }
    
        public static final Command Query = new Command('Q', (byte)1);
        public static final Command Reply = new Command('R', (byte)2);
        public static final Command Send = new Command('S', (byte)3);
        public static final Command Unknown = new Command('U', (byte)0);
        public static final Command[] ALL = { Query, Reply, Send, Unknown };

    }
    
    /**
     * A class representing a pseudo-enum for the available keys
     */
    public static class Key {
        
        public String shorthand;
        public byte bitsum;
        
        private Key(String c, byte l) { 
            this.shorthand = c; 
            this.bitsum = l;
        }
        
        public static final Key CamAngle = new Key("CA", (byte)28);
        public static final Key ShooterSpeed = new Key("SS", (byte)4);
        public static final Key AnalogValue = new Key("AV", (byte)8);
        public static final Key Position = new Key("DO", (byte)16);
        public static final Key PWMValue = new Key("PV", (byte)12);
        public static final Key RelayValue = new Key("RV", (byte)24);
        public static final Key CANValue = new Key("CV", (byte)20);
        public static final Key DigitalIO = new Key("DI", (byte)0);
        public static final Key[] ALL = { CamAngle, ShooterSpeed, AnalogValue, Position, PWMValue, RelayValue, CANValue, DigitalIO };
        
    }
    
    public static class Value {
        public char shorthand;
        public byte bitsum;
        
        private Value(char c, byte b) {
            shorthand = c;
            bitsum = b;
        }
        
        public static final Value BYTE = new Value('b', (byte)0);
        public static final Value SHORT = new Value('s', (byte)32);
        public static final Value DOUBLE_BYTE = new Value('d', (byte)64);
        public static final Value DOUBLE_SHORT = new Value('e', (byte)96);
        public static final Value[] ALL = { BYTE, SHORT, DOUBLE_BYTE, DOUBLE_SHORT };
    }
    
    
    
    /**
    * This class is responsible for parsing messages sent from the laptop to the
    * robot and vice versa. For more information on the protocol visit the wiki.
    * @author Brennan Ringey
    * @version 3.5
    */
    public static final class StringParser {
        
        //Prevent instantiation
        private StringParser() { }
        
        public static StringMessage parse(String msg) {
        	StringMessage sm = new StringMessage();
        	
        	sm.setCommand(getCommand(msg));
        	sm.setKey(getKey(msg));
        	sm.setValue(getValue(msg));
        	sm.setArgs(getArgs(msg));
        	
        	return sm;
        }
        
        /**
        * Tests if the message is a Query (aka Request). A Query message
        * begins with the character Q and is not case-sensitive.
        * @param msg The message to test
        * @return True if the message is a query, false otherwise
        */
       public static boolean isQuery(String msg) {
           return (msg.charAt(0) == 'Q' || msg.charAt(0) == 'q');
       }
       
       public static boolean isQuery(StringMessage msg) {
    	   return (msg.getCommand().shorthand == 'Q');
       }

       /**
        * Test if the message is a Reply. A Reply message begins with the 
        * character R and is not case-sensitive.
        * @param msg The message to test
        * @return True if the message is a Reply, false otherwise
        */
       public static boolean isReply(String msg) {
           return (msg.charAt(0) == 'R' || msg.charAt(0) == 'r');
       }
       
       public static boolean isReply(StringMessage msg) {
    	   return (msg.getCommand().shorthand ==  'R');
       }

       /**
        * Test if the message is a Send. A Send message begins with the
        * character S and is not case-sensitive.
        * @param msg The message to test
        * @return True if the message is a Send, false otherwise
        */
       public static boolean isSend(String msg) {
           return (msg.charAt(0) == 'S' || msg.charAt(0) == 's');
       }
       
       public static boolean isSend(StringMessage msg) {
    	   return (msg.getCommand().shorthand == 'S');
       }
       /**
        * An alternative to the above methods, this method gets the command from
        * the message header rather than testing it.
        * @param msg The message to get the command from
        * @return An enumerator from Commands, unknown is returned if no other commands match
        */
       public static Command getCommand(String msg) {
           char c = Character.toUpperCase(msg.charAt(0));
           Command command = Command.Unknown;

           for (int i = 0; i != Command.ALL.length; i++) {
               if (c == Command.ALL[i].shorthand) {
                   command = Command.ALL[i];
               }
           }
           
           return command;
       }

       /**
        * Identifies the key in the message. All of the possible keys are located on
        * the wiki and are represented by the enum Keys. If no possible key was
        * identified, null is returned
        * @param msg The message containing the key to retrieve
        * @return An enumerator from Keys representing the key in the message
        */
       public static Key getKey(String msg) {
           String k = separateByColon(msg)[1];
           Key key = null;

           for (int i = 0; i != Key.ALL.length; i++) {
               if (k.equals(Key.ALL[i].shorthand)) {
                   key = Key.ALL[i];
               }
           }

           return key;
       }

       /**
        * Gets the value from the message. The value varies based on the key. If a 
        * value was not identified, null is returned
        * @param msg The message to get the value from
        * @return A string containing the value.
        */
       public static String getValue(String msg) {
           String[] a = separateByColon(msg);
           try {
               return a[2];
           } catch (IndexOutOfBoundsException ex) {
               return null;
           }
       }

       /**
        * Gets the additional args (if any) from the message. Arguments are 
        * separated by commas, after the final colon
        * @param msg The message containing args (if any)
        * @return An array of strings, representing the arguments
        */
       public static String[] getArgs(String msg) {
           String args = "";
           try {
               args = StringTools.split(msg, ':')[3];
           } catch (IndexOutOfBoundsException ex) {
               System.out.println(ex.toString());
           }
           String[] rargs = null;
           
           if (!args.equals("")) {
               rargs = StringTools.split(args, ',');
           }
           
           
           
           return rargs;
       }

       /**
        * Create a string to be transmitted over TCP. Query does not require a value
        * or arguments (Leave value null). Arguments are NOT required.
        * @param command A enumerator from Commands representing the command (Must not be Commands.Unknown)
        * @param key An enumerator from Keys representing the key (Must not be null!)
        * @param value A string representing the value (Leave empty string if Query)
        * @param args A string of args to be added
        * @return A String containing the message to be sent
        */
       public static String createMessage(Command command, Key key, String value, String[] args) {
           String packet = null;

           if (command == null || key == null) { return null; }

           if (command != Command.Unknown && key != null) {
               packet = command.shorthand + ":" + key.shorthand + ":" + value;
               if (args != null) {
                    if (args.length > 0) {
                        packet += ":";
                        for (int i = 0; i != args.length; i++) {
                            packet += args[i];
                            if (i != args.length - 1) { packet += ","; }
                        }
                    }
               }
           }

           return packet;
       }
       
       //public static String toFastFormat(Message msg) {
           
       //}

       //Helper method used by parsing methods
       private static String[] separateByColon(String msg) {
           return StringTools.split(msg, ':');
       } 
       
       
    }
    
    /**
     * This class parses FastFormat messages transmitted to and from robot/laptop.
     * The FastFormat uses 1 byte for the header and the rest being a value/args.
     * For more information on FastFormat go to http://goo.gl/S8fwa
     * @author Brennan Ringey
     * @version 1.0
     */
    public static class FastParser {
        
        private FastParser() {}
        
        public static boolean hasArgs(String msg) {
        	int header = (int)msg.charAt(0);
        	header = BitwiseTools.negatebits(header, 0, 6);
        	return (header == 128);
        }
        
        public static Command getCommand(String msg) {
            Command command = Command.Unknown;
            int header = (int)msg.charAt(0);
            //Set all bits after the first two to zero
            int cheader = BitwiseTools.negatebits(header, 2, 7);
            
            
            //Iterate through all commands
            for (int i = 0; i != Command.ALL.length; i++) {
                Command c = Command.ALL[i];
                
                if (cheader == c.bitsum) {
                    command = c;
                    break;
                }
            }
            
            return command;
        }
        
        public static Key getKey(String msg) {
            Key key = null;
            int header = (int)msg.charAt(0);
            int kheader = BitwiseTools.negatebits(header, 0, 1);
            kheader = BitwiseTools.negatebits(kheader, 5, 7);
            
            for (int i = 0; i != Key.ALL.length; i++) {
                Key k = Key.ALL[i];
                if (kheader == k.bitsum) {
                    key = k;
                    break;
                }
            }
            return key;
        }
        
        public static String getValue(String msg) {
            String value = null;
            Value val = null;
            int header = (int)msg.charAt(0);
            int vheader = BitwiseTools.negatebits(header, 0, 4);
            vheader = BitwiseTools.negatebit(vheader, 7);
            
            for (int i = 0; i != Value.ALL.length; i++) {
                Value v = Value.ALL[i];
                if (vheader == v.bitsum) {
                    val = v;
                    break;
                }
            }
            
            if (val == Value.BYTE) {
                byte b = (byte)msg.charAt(1);
                value = String.valueOf((int)b);
            } 
            if (val == Value.SHORT) {
            	char[] s = msg.substring(1).toCharArray();
                int num = BitwiseTools.makeShort((int)s[0], (int)s[1]);
                value = String.valueOf((short)num);
            }
            if (val == Value.DOUBLE_BYTE) {
            	int wholepart = (int)msg.charAt(1);
        		int fracpart = (int)msg.charAt(2);
        		value = wholepart + "." + fracpart;
            }
            if (val == Value.DOUBLE_SHORT) {
            	char[] wholeshort = msg.substring(1, 3).toCharArray();
        		char[] fracshort = msg.substring(3, 5).toCharArray();
        		int wholepart = BitwiseTools.makeShort((int)wholeshort[0], (int)wholeshort[1]);
        		int fracpart = BitwiseTools.makeShort((int)fracshort[0], (int)fracshort[1]);
        		value = wholepart + "." + fracpart;
            }
            
            
            
            return value;
        }
        
        public static String createMessage(Command command, Key key, Value val, String value, String[] args) {
            
            int header = 0;
            String vsect = "", asect = "";
            
            header += command.bitsum;
            header += key.bitsum;
            header += val.bitsum;
            
            if (val == Value.BYTE) {
            	int num = Integer.parseInt(value);
            	vsect += (char)num;
            }
            if (val == Value.SHORT) {
            	vsect = BitwiseTools.toCharShort(Integer.parseInt(value));
            }
            if (val == Value.DOUBLE_BYTE || val == Value.DOUBLE_SHORT) {
            	double d = Double.parseDouble(value);
            	if (MathTools.numOfPlaces(d) <= 3 && MathTools.fpart(d) <= 255 && MathTools.ipart(d) <= 255) {
            		vsect = BitwiseTools.toByteDouble(d);
            	} else if (MathTools.numOfPlaces(d) <= 5 && MathTools.fpart(d) <= 65535 && MathTools.ipart(d) <= 65535){
            		vsect = BitwiseTools.toShortDouble(d);
            	}
            }
            
            if (args != null) { 
            	header += 128; 
            	for (int i = 0; i != args.length; i++) {
            		asect += args[i];
            		if (i != args.length-1) {
            			asect += ",";
            		}
            	}
            }
            
            
            
            return (char)header + vsect + asect;
        }
    }
       
    
    
}
