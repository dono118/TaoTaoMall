package com.taotao.search.utils;  
  
import java.io.PrintWriter;  
import java.io.StringWriter;  
import java.io.Writer;  
  
public class StackTrace {  
      
    public static String getStackTrace(Throwable aThrowable) {  
        final Writer result = new StringWriter();  
        final PrintWriter printWriter = new PrintWriter(result);  
        aThrowable.printStackTrace(printWriter);  
        return result.toString();  
    }  
}
