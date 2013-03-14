package com.bj58.argo.thirdparty.jetty;
/* ------------------------------------------------------------ */
/**
 * UTF-8 StringBuffer.
 *
 * This class wraps a standard {@link java.lang.StringBuffer} and provides methods to append
 * UTF-8 encoded bytes, that are converted into characters.
 *
 * This class is stateful and up to 4 calls to {@link #append(byte)} may be needed before
 * state a character is appended to the string buffer.
 *
 * The UTF-8 decoding is done by this class and no additional buffers or Readers are used.
 * The UTF-8 code was inspired by http://bjoern.hoehrmann.de/utf-8/decoder/dfa/
 */
public class Utf8StringBuffer extends Utf8Appendable
{
    final StringBuffer _buffer;

    public Utf8StringBuffer()
    {
        super(new StringBuffer());
        _buffer = (StringBuffer)_appendable;
    }

    public Utf8StringBuffer(int capacity)
    {
        super(new StringBuffer(capacity));
        _buffer = (StringBuffer)_appendable;
    }

    @Override
    public int length()
    {
        return _buffer.length();
    }

    @Override
    public void reset()
    {
        super.reset();
        _buffer.setLength(0);
    }

    public StringBuffer getStringBuffer()
    {
        checkState();
        return _buffer;
    }

    @Override
    public String toString()
    {
        checkState();
        return _buffer.toString();
    }
}