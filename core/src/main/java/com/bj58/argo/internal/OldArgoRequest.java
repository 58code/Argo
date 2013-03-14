package com.bj58.argo.internal;

import com.bj58.argo.ArgoException;
import com.bj58.argo.thirdparty.jetty.LazyList;
import com.bj58.argo.thirdparty.jetty.MultiMap;
import com.bj58.argo.thirdparty.jetty.MultiPartInputStreamParser;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class OldArgoRequest extends HttpServletRequestWrapper {

    /**
     * 创建时间
     */
//    private final long birthTime = System.currentTimeMillis();

    private final MultiPartInputStreamParser mpis;





    /**
     * Constructs a request object wrapping the given request.
     *
     * @throws IllegalArgumentException
     *          if the request is null
     */
    OldArgoRequest(HttpServletRequest request, MultipartConfigElement config) {
        super(request);

        if (!isMultiPart(request)) {
            mpis = null;
            return;
        }

        try {

            InputStream in = new BufferedInputStream(request.getInputStream());
            String content_type=request.getContentType();

            //Get current parameters so we can merge into them
            MultiMap<Object> params = new MultiMap<Object>();
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet())
            {
                String[] value = entry.getValue();
                System.out.println("entry key:" + entry.getKey() + "value:" + value[0]);
                if (value instanceof String[]) {
                    params.addValues(entry.getKey(), (String[])value);
//                    System.out.println("entry key:" + entry.getKey() + "value:" + );
                }
                else
                    params.add(entry.getKey(), value);
            }

            mpis = isMultiPart(request) ? getMultiPartInputStreamParser(request, config) : null;
//            _params = paramMultiMap(request, mpis);
        } catch (Exception e) {
            clean();
            throw ArgoException.raise(e);
        }
    }

    private boolean isMultiPart(HttpServletRequest request) {
//        return (request.getContentType()==null||!request.getContentType().startsWith("multipart/form-data"));

        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase().startsWith("multipart/")) {
            return true;
        }
        return false;
    }



    private MultiPartInputStreamParser getMultiPartInputStreamParser(HttpServletRequest request, MultipartConfigElement config) throws IOException {
        InputStream in = new BufferedInputStream(request.getInputStream());
        String content_type = request.getContentType();

        File tempDir = new File(config.getLocation());

        MultiPartInputStreamParser mpis = new MultiPartInputStreamParser(in, content_type, config, tempDir);
        mpis.setDeleteOnExit(true);
        return mpis;
    }


    void clean()
    {
        if (mpis == null)
            return;

        try
        {
            mpis.deleteParts();
        }
        catch (Exception e)
        {
            throw ArgoException.raise("Error deleting multipart tmp files", e);
        }
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return mpis.getParts();
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return mpis.getPart(name);
    }



//
//    public final static String CONTENT_TYPE_SUFFIX=".org.eclipse.jetty.servlet.contentType";
//
//    public static int bufferSize = 64*1024;
//
//    private final static String MULTIPART = "org.eclipse.jetty.servlet.MultiPartFile.multiPartInputStream";
//
//    public final static String __UTF8="UTF-8";
//    private final MultiPartInputStreamParser mpis;
//
////    private File tempdir;
//    private int _fileOutputBuffer = 0;
//    private long _maxFileSize = -1L;
//    private long _maxRequestSize = -1L;
    private int _maxFormKeys = 1000;



//    private String _encoding = __UTF8;

//    private final MultiMap<Object> _params;

//        private MultiMap<Object> paramMultiMap(HttpServletRequest request, MultiPartInputStreamParser mpis) throws Exception {
//
//        //Get current parameters so we can merge into them
//        MultiMap<Object> params = new MultiMap<Object>();
//        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet())
//        {
//            Object value = entry.getValue();
//            if (value instanceof String[])
//                params.addValues(entry.getKey(), (String[])value);
//            else
//                params.add(entry.getKey(), value);
//        }
//
//
//        Collection<Part> parts = mpis.getUploads();
//        if (parts != null)
//        {
//            Iterator<Part> iterator = parts.iterator();
//            while (iterator.hasNext() && params.size() < _maxFormKeys)
//            {
//                Part p = iterator.next();
//                MultiPartInputStreamParser.MultiPart mp = (MultiPartInputStreamParser.MultiPart)p;
//                if (mp.getFile() != null)
//                {
//                    request.setAttribute(mp.getName(),mp.getFile());
//                    if (mp.getContentDispositionFilename() != null)
//                    {
//                        params.add(mp.getName(), mp.getContentDispositionFilename());
//                        if (mp.getContentType() != null)
//                            params.add(mp.getName()+CONTENT_TYPE_SUFFIX, mp.getContentType());
//                    }
//                }
//                else
//                {
//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    IoCopy(p.getInputStream(), bytes);
//                    params.add(p.getName(), bytes.toByteArray());
//                    if (p.getContentType() != null)
//                        params.add(p.getName()+CONTENT_TYPE_SUFFIX, p.getContentType());
//                }
//            }
//        }
//
//        return params;
//
//
//    }

    //
//    /* ------------------------------------------------------------------------------- */
//    /**
//     * @see javax.servlet.ServletRequest#getContentLength()
//     */
//    @Override
//    public int getContentLength()
//    {
//        return 0;
//    }
//
//        /* ------------------------------------------------------------------------------- */
//    /**
//     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
//     */
//    @Override
//    public String getParameter(String name)
//    {
//        Object o=_params.get(name);
//        if (!(o instanceof byte[]) && LazyList.size(o)>0)
//            o=LazyList.get(o,0);
//
//        if (o instanceof byte[])
//        {
//            try
//            {
//                return new String((byte[])o,_encoding);
//            }
//            catch(Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//        else if (o!=null)
//            return String.valueOf(o);
//        return null;
//    }
//
//        /* ------------------------------------------------------------------------------- */
//    /**
//     * @see javax.servlet.ServletRequest#getParameterMap()
//     */
//    @Override
//    public Map<String, String[]> getParameterMap()
//    {
//        Map<String, String[]> cmap = new HashMap<String,String[]>();
//
//        for ( Object key : _params.keySet() )
//        {
//            String[] a = LazyList.toStringArray(getParameter((String)key));
//            cmap.put((String)key,a);
//
//        }
//
//        return Collections.unmodifiableMap(cmap);
//    }
//
//        /* ------------------------------------------------------------------------------- */
//    /**
//     * @see javax.servlet.ServletRequest#getParameterNames()
//     */
//    @Override
//    public Enumeration<String> getParameterNames()
//    {
//        return Collections.enumeration(_params.keySet());
//    }
//
//        /* ------------------------------------------------------------------------------- */
//    /**
//     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
//     */
//    @Override
//    public String[] getParameterValues(String name)
//    {
//        List l=_params.getValues(name);
//        if (l==null || l.size()==0)
//            return new String[0];
//        String[] v = new String[l.size()];
//        for (int i=0;i<l.size();i++)
//        {
//            Object o=l.get(i);
//            if (o instanceof byte[])
//            {
//                try
//                {
//                    v[i]=new String((byte[])o,_encoding);
//                }
//                catch(Exception e)
//                {
//                    throw new RuntimeException(e);
//                }
//            }
//            else if (o instanceof String)
//                v[i]=(String)o;
//        }
//        return v;
//    }
//
//        /* ------------------------------------------------------------------------------- */
//    /**
//     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
//     */
//    @Override
//    public void setCharacterEncoding(String enc)
//            throws UnsupportedEncodingException
//    {
//        _encoding=enc;
//    }
//
//
//        /* ------------------------------------------------------------------- */
//    /** Copy Stream in to Stream out until EOF or exception.
//     */
//    private static void IoCopy(InputStream in, OutputStream out)
//            throws IOException
//    {
//        IoCopy(in, out, -1);
//    }
//
//        /* ------------------------------------------------------------------- */
//    /** Copy Stream in to Stream for byteCount bytes or until EOF or exception.
//     */
//    private static void IoCopy(InputStream in,
//                              OutputStream out,
//                              long byteCount)
//            throws IOException
//    {
//        byte buffer[] = new byte[bufferSize];
//        int len=bufferSize;
//
//        if (byteCount>=0)
//        {
//            while (byteCount>0)
//            {
//                int max = byteCount<bufferSize?(int)byteCount:bufferSize;
//                len=in.read(buffer,0,max);
//
//                if (len==-1)
//                    break;
//
//                byteCount -= len;
//                out.write(buffer,0,len);
//            }
//        }
//        else
//        {
//            while (true)
//            {
//                len=in.read(buffer,0,bufferSize);
//                if (len<0 )
//                    break;
//                out.write(buffer,0,len);
//            }
//        }
//    }
}
