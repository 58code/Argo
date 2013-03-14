package com.bj58.argo.thirdparty.jetty;

import java.nio.charset.Charset;


/** Fast String Utilities.
 *
 * These string utilities provide both conveniance methods and
 * performance improvements over most standard library versions. The
 * main aim of the optimizations is to avoid object creation unless
 * absolutely required.
 *
 * 
 */
public class StringUtil
{
    private final static StringMap<String> CHARSETS= new StringMap<String>(true);
    
    public static final String ALL_INTERFACES="0.0.0.0";
    public static final String CRLF="\015\012";
    public static final String __LINE_SEPARATOR=
        System.getProperty("line.separator","\n");
       
    public static final String __ISO_8859_1="ISO-8859-1";
    public final static String __UTF8="UTF-8";
    public final static String __UTF16="UTF-16";
    
    public final static Charset __UTF8_CHARSET;
    public final static Charset __ISO_8859_1_CHARSET;
    public final static Charset __UTF16_CHARSET;
    
    static
    {
        __UTF8_CHARSET=Charset.forName(__UTF8);
        __ISO_8859_1_CHARSET=Charset.forName(__ISO_8859_1);
        __UTF16_CHARSET=Charset.forName(__UTF16);
        
        CHARSETS.put("UTF-8",__UTF8);
        CHARSETS.put("UTF8",__UTF8);
        CHARSETS.put("UTF-16",__UTF16);
        CHARSETS.put("UTF16",__UTF16);
        CHARSETS.put("ISO-8859-1",__ISO_8859_1);
        CHARSETS.put("ISO_8859_1",__ISO_8859_1);
    }


}
