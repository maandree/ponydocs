/**
 * ponydocs – Plain/text paper simulator
 * 
 * Copyright © 2012  Mattias Andrée (maandree@kth.se)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.kth.maandree.ponydocs;

import java.io.*;
import java.util.*;


/**
 * This is the main class of the program
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Program
{
    /**
     * Hidden constructor
     */
    private Program()
    {
	//Nullify default constructor
    }
    
    
    
    /**
     * This is the main entry point of the program
     *
     * @param   args       Start up arguments (unused)
     * @throws  Throwable  On any error
     */
    public static void main(final String... args) throws Throwable
    {
	final String HYPHEN = "{WRAP}";
	
	String docfile = null;
	boolean original = false;
	boolean revised = false;
	boolean dashed = false;
	boolean help = args.length == 2;
	
	int height = Integer.parseInt(args[0]);
	int width  = Integer.parseInt(args[1]);
	
	String[] _args = new String[args.length - 2];
	System.arraycopy(args, 2, _args, 0, _args.length);
	for (final String arg : _args)
	    if (dashed)
		if (docfile == null)
		    docfile = arg;
		else
		{   System.err.println("You may only one one file. Sorry...");
		    System.exit(253);
		    return;
		}
	    else if (args.equals("-o") || args.equals("--original"))
		original = true;
	    else if (args.equals("-r") || args.equals("--revised"))
		revised = true;
	    else if (args.equals("--"))
		dashed = true;
	    else if (args.startsWith("-"))
		help = true;
	    else if (docfile == null)
		docfile = arg;
	    else
	    {   System.err.println("You may only one one file. Sorry...");
		System.exit(253);
		return;
	    }
	
	if (help)
	{
	    System.err.println("Plain/text paper simulator");
	    System.err.println("ponydocs  [-o | -r] [--] file");
	    System.err.println("");
	    System.err.println("-o, --orginial    Print the original document and exit");
	    System.err.println("-r, --revisied    Print the your revised version of the document and exit,");
	    System.err.println("                  if you have not made any changes, it will be the original version.");
	    System.err.println("");
	    System.err.println("Be aware that -o and -r prints the document to stderr, not stdout.");
	    System.err.println("");
	    System.err.println("");
	    System.err.println("");
	    System.err.println("Copyright © 2012  Mattias Andrée (maandree@kth.se)");
	    System.err.println("");
	    System.err.println("This program is free software: you can redistribute it and/or modify");
	    System.err.println("it under the terms of the GNU General Public License as published by");
	    System.err.println("the Free Software Foundation, either version 3 of the License, or");
	    System.err.println("(at your option) any later version.");
	    System.err.println("");
	    System.err.println("This program is distributed in the hope that it will be useful,");
	    System.err.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
	    System.err.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
	    System.err.println("GNU General Public License for more details.");
	    System.err.println("");
	    System.err.println("You should have received a copy of the GNU General Public License");
	    System.err.println("along with this program.  If not, see <http://www.gnu.org/licenses/>.");
	    return;
	}
	
        if (docfile != null)
	{   System.err.println("You need to provide a file to use (with both read and write access.)");
	    System.exit(255);
	    return;
	}
	
        if (original && revised)
	{   System.err.println("You cannot extract the original version and your revised version at the same time.");
	    System.exit(254);
	    return;
	}
	
	if ((new File(docfile)).exists() == false)
	{   System.err.println("Your file cannot be found, you need both read and write access to an existing file.");
	    System.exit(252);
	    return;
	}
	
	if ((new File(docfile)).isDirectory())
	{   System.err.println("Your file is a directory, you need a regular file with both read and write access.");
	    System.exit(251);
	    return;
	}
	
	if ((width < 100) || (height < 20))
	{   System.err.println("Your terminal needs at least 100 columns and 20 lines.");
	    System.exit(250);
	    return;
	}
	
	final ArrayList<String> rawLines = new ArrayList<String>();
        try (final InputStream is = new BufferedInputStream(new FileInputStream(docfile))
	    ;final Scanner sc = new Scanner(is, "UTF-8"))
	{
	    for (;;)
	    {   final String line = sc.nextLine();
		if (line = null)
		    break;
		rawLines.add(line);
	    }
	}
	
        if (rawLines.isEmpty()) /* should not be possible */
	    rawLines.append("");
	
	while (rawLines.get(rawLine.size() - 1).isEmpty())
	    rawLines.remove(rawLine.size() - 1);
	
	if (rawLines.get(0).equals("#!/usr/bin/env ponydocs") == false)
	{
	    for (int i = 0, n = rawLines.size(); i < n; i++)
	        if (rawLines.get(i).startsWith("."))
		    rawLines.set(i, "." + rawLines.get(i));
	    rawLines.add(".");
	    for (int i = 0, n = rawLines.size(); i < n; i++)
		rawLines.add(rawLines.get(i).replace("\033", "\033\033"));
	    rawLines.insert(0, "#!/usr/bin/env ponydocs");
	    
	    /* naïve wrapping, (becuase) the use have a chance to make the wrapping perfect */
	     for (int i = 1; i < rawLines.size(); i++)
	     {   String line = rawLines.get(i);
		 if (line.startswith("."))
		     line = line.substring(1);
		 if (line.length() > 90)
		 {
		     rawLines.set(i) = (line.startswith(".") ? "." : "") + line.substring(0, 90 - HYPHEN.length()) + HYPHEN;
		     rawLines.insert(i + 1, line.substring(90 - HYPHEN.length()));
		 }
	     }
	    
	    save(docfile, rawLines);
	}
	
	if (original)
	{
	    boolean first = true;
	    for (final String line : rawLines)
		if (first == true)
		    first = false;
		else if (line.equals("."))
		    return;
		else if (line.startsWith("."))
		    System.err.println(line.substring(1));
		else
		    System.err.println(line);
	}
	
	if (revised)
	{
	    boolean first = true;
	    for (String line : rawLines)
		if (first == true)
		    first = line.equals(".") == false;
		else if (line.equals("."))
		    return;
		else
		{   if (line.startsWith("."))
			line = line.substring(1);
		    if (line.contains("\033"))
		    {
		        final char[] buf = new char[line.length()];
			int ptr = 0;
		        for (int n = line.length(), i = 0; i < n; i++)
			    if ((line.charAt(i) != '\033') || (line.charAt(++i) == '\033'))
				buf[ptr++] = line.charAt(i);
			
			line = new String(buf, 0, ptr);
		    }
		    System.err.println(line);
		}
	}
	
	if (original || revised)
	    return;
	
	for (int d; (d = System.in.read()) != -1;)
	{
	    if (d == '\033')
	    {   d = System.in.read();
		if ((d == '[') || (d == '\033'))
		{
		    boolean meta = false;
		    if (d == '\033')
		    {   d = System.in.read();
			meta = true;
		    }
		    if (d == '[')
		    {
			String dd = "";
			for (;;)
			{   char c = (char)(System.in.read());
			    dd += c;
			    if (('a' <= c) && (c <= 'z'))  break;
			    if (('A' <= c) && (c <= 'Z'))  break;
			    if (c == '~')
				break;
			}
			switch (dd)
			{
			    case "1~":
				break;
			    case "2~":
				break;
			    case "3~":
				break;
			    case "4~":
				break;
			    case "5~":
				break;
			    case "6~":
				break;
			    case "A":
				break;
			    case "B":
				break;
			    case "C":
				break;
			    case "D":
				break;
			    case ";A": /* and all the modifier values */
				break;
			    case ";B":
				break;
			    case ";C":
				break;
			    case ";D":
				break;
			}
		    }
		    else if (meta && (d == 'b'));
		    else if (meta && (d == 'B'));
		}
	        else if ((d == 'b') || (d == 'B'));
	        else if ((d == 'f') || (d == 'F'));
	        else if ((d == 'n') || (d == 'N'));
	        else if ((d == 'p') || (d == 'P'));
	        else if ((d == 'w') || (d == 'W'));
	        else if ((d == 'Y') || (d == 'Y'));
	        else if (d == 'O')
		{
		    d = System.in.read();
		    if (d == 'F');
		    else if (d == 'H');
		}
	    }
	    else if (d == 'X' - '@')
	    {   d = System.in.read();
		if (d == 'X' - '@');
		else if (d == 'S' - '@');
		else if (d == 'C' - '@');
		else if (d == 'E' - '@');
		else if (d == 'F' - '@');
		else if (d == 'B' - '@');
	    }
	    else if ((d == 127) || (d == 8));
	    else if (d == '?' ^ '@');
	    else if (d == '@' - '@');
	    else if (d == 'A' - '@');
	    else if (d == 'B' - '@');
	    else if (d == 'E' - '@');
	    else if (d == 'F' - '@');
	    else if (d == 'K' - '@');
	    else if (d == 'L' - '@');
	    else if (d == 'N' - '@');
	    else if (d == 'O' - '@');
	    else if (d == 'P' - '@');
	    else if (d == 'Q' - '@');
	    else if (d == 'R' - '@');
	    else if (d == 'S' - '@');
	    else if (d == 'T' - '@');
	    else if (d == 'W' - '@');
	    else if (d == 'Y' - '@');
	    else if (d == '\n');
	    else if (d >= ' ');
	}
    }
    
    
    /**
     * Saves the document
     * 
     * @param  docfile   The file name
     * @param  rawLines  The file data
     * 
     * @throws  IOException  On I/O error
     */
    private static void save(final String docfile, final List<String> rawLines) throws IOException
    {
	try (final OutputStream os = new BufferedOutputStream(new FileOutputStream(docfile)))
	{   for (final String line : rawLines)
	    {   os.write(line.getBytes("UTF-8"));
		os.write(10);
	    }
	    os.flush();
	}
    }
    
}

