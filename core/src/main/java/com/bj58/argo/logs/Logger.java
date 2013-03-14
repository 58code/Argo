package com.bj58.argo.logs;

/**
 * Log日志接口
 * 
 *  @author Service Platform Architecture Team (spat@58.com)
 */
public interface Logger {

    /**
	 * Log a message at the DEBUG level.
	 * 
	 * @param msg the message string to be logged.
	 */
	void debug(String msg);

    /**
     * Log a message at the DEBUG level according to the specified format
     * and argument.
     * <p/>
     * <p>This form avoids superfluous object creation when the logger
     * is disabled for the DEBUG level. </p>
     *
     * @param format the format string
     * @param arg    the argument
     */
    public void debug(String format, Object arg);
	
	/**
	 * Log an exception at the DEBUG level with an accompanying message.
	 *  
	 * @param msg the message accompanying the exception
	 * @param t   the exception to be logged
	 */
	void debug(String msg, Throwable t);
	
	/**
	 * Log a message at the INFO level.
	 * 
	 * @param msg the message string to be logged
	 */
	void info(String msg);

    /**
     * Log a message at the INFO level according to the specified format
     * and argument.
     * <p/>
     * <p>This form avoids superfluous object creation when the logger
     * is disabled for the INFO level. </p>
     *
     * @param format the format string
     * @param arg    the argument
     */
    public void info(String format, Object arg);
	
	/**
	 * Log an exception at the INFO level with an accompanying message.
	 * 
	 * @param msg the message accompanying the exception
	 * @param t   the exception to be logged
	 */
	void info(String msg, Throwable t);
	
	/**
	 * Log a message at the ERROR level.
	 * 
	 * @param msg the message string to be logged
	 */
	void error(String msg);
	
	/**
	 * Log an exception at the ERROR level.
	 * 
	 * @param msg the message accompanying the exception
	 * @param t   the exception to log
	 */
	void error(String msg, Throwable t);

    /**
     * Log a message at the ERROR level according to the specified format
     * and argument.
     * <p/>
     * <p>This form avoids superfluous object creation when the logger
     * is disabled for the ERROR level. </p>
     *
     * @param format the format string
     * @param arg    the argument
     */
    public void error(String format, Object arg);

}