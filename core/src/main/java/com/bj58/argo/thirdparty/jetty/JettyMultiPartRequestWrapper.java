/*
*  Copyright Beijing 58 Information Technology Co.,Ltd.
*
*  Licensed to the Apache Software Foundation (ASF) under one
*  or more contributor license agreements.  See the NOTICE file
*  distributed with this work for additional information
*  regarding copyright ownership.  The ASF licenses this file
*  to you under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package com.bj58.argo.thirdparty.jetty;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

/**
 * source: org.eclipse.jetty.servlets.MultiPartFilter
 * Multipart Form Data Filter.
 * <p>
 * This class decodes the multipart/form-data stream sent by a HTML form that uses a file input
 * item.  Any files sent are stored to a temporary file and a File object added to the request
 * as an attribute.  All other values are made available via the normal getParameter API and
 * the setCharacterEncoding mechanism is respected when converting bytes to Strings.
 * <p>
 * If the init parameter "delete" is set to "true", any files created will be deleted when the
 * current request returns.
 * <p>
 * The init parameter maxFormKeys sets the maximum number of keys that may be present in a
 * form (default set by system property org.eclipse.jetty.server.Request.maxFormKeys or 1000) to protect
 * against DOS attacks by bad hash keys.
 * <p>
 * The init parameter deleteFiles controls if uploaded files are automatically deleted after the request
 * completes.
 *
 * Use init parameter "maxFileSize" to set the max size file that can be uploaded.
 *
 * Use init parameter "maxRequestSize" to limit the size of the multipart request.
 *
 */
public class JettyMultiPartRequestWrapper {

    public final static String CONTENT_TYPE_SUFFIX=".org.eclipse.jetty.servlet.contentType";
    private final static String MULTIPART = "org.eclipse.jetty.servlet.MultiPartFile.multiPartInputStream";


    private File tempdir;
    private boolean _deleteFiles;
    private ServletContext _context;
    private int _fileOutputBuffer = 0;
    private long _maxFileSize = -1L;
    private long _maxRequestSize = -1L;
    private int _maxFormKeys = Integer.getInteger("org.eclipse.jetty.server.Request.maxFormKeys", 1000);


    public final static String __UTF8="UTF-8";

    public static int bufferSize = 64*1024;



    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest srequest=(HttpServletRequest)request;

        // guard clause
        if(srequest.getContentType()==null||!srequest.getContentType().startsWith("multipart/form-data"))
        {
            return;
        }

        InputStream in = new BufferedInputStream(request.getInputStream());
        String content_type=srequest.getContentType();

        //Get current parameters so we can merge into them
        MultiMap params = new MultiMap();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet())
        {
            Object value = entry.getValue();
            if (value instanceof String[])
                params.addValues(entry.getKey(), (String[])value);
            else
                params.add(entry.getKey(), value);
        }

        MultipartConfigElement config = new MultipartConfigElement(tempdir.getCanonicalPath(), _maxFileSize, _maxRequestSize, _fileOutputBuffer);
        MultiPartInputStreamParser mpis = new MultiPartInputStreamParser(in, content_type, config, tempdir);
        mpis.setDeleteOnExit(_deleteFiles);
        request.setAttribute(MULTIPART, mpis);
        try
        {
            Collection<Part> parts = mpis.getParts();
            if (parts != null)
            {
                Iterator<Part> itor = parts.iterator();
                while (itor.hasNext() && params.size() < _maxFormKeys)
                {
                    Part p = itor.next();
                    MultiPartInputStreamParser.MultiPart mp = (MultiPartInputStreamParser.MultiPart)p;
                    if (mp.getFile() != null)
                    {
                        request.setAttribute(mp.getName(),mp.getFile());
                        if (mp.getContentDispositionFilename() != null)
                        {
                            params.add(mp.getName(), mp.getContentDispositionFilename());
                            if (mp.getContentType() != null)
                                params.add(mp.getName()+CONTENT_TYPE_SUFFIX, mp.getContentType());
                        }
                    }
                    else
                    {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        IoCopy(p.getInputStream(), bytes);
                        params.add(p.getName(), bytes.toByteArray());
                        if (p.getContentType() != null)
                            params.add(p.getName()+CONTENT_TYPE_SUFFIX, p.getContentType());
                    }
                }
            }

            // handle request
            chain.doFilter(new Wrapper(srequest,params),response);
        }
        finally
        {
            deleteFiles(request);
        }
    }

    private void deleteFiles(ServletRequest request)
    {
        if (!_deleteFiles)
            return;

        MultiPartInputStreamParser mpis = (MultiPartInputStreamParser)request.getAttribute(MULTIPART);
        if (mpis != null)
        {
            try
            {
                mpis.deleteParts();
            }
            catch (Exception e)
            {
                _context.log("Error deleting multipart tmp files", e);
            }
        }
        request.removeAttribute(MULTIPART);
    }

        /* ------------------------------------------------------------------- */
    /** Copy Stream in to Stream out until EOF or exception.
     */
    public static void IoCopy(InputStream in, OutputStream out)
            throws IOException
    {
        IoCopy(in, out, -1);
    }

        /* ------------------------------------------------------------------- */
    /** Copy Stream in to Stream for byteCount bytes or until EOF or exception.
     */
    public static void IoCopy(InputStream in,
                            OutputStream out,
                            long byteCount)
            throws IOException
    {
        byte buffer[] = new byte[bufferSize];
        int len=bufferSize;

        if (byteCount>=0)
        {
            while (byteCount>0)
            {
                int max = byteCount<bufferSize?(int)byteCount:bufferSize;
                len=in.read(buffer,0,max);

                if (len==-1)
                    break;

                byteCount -= len;
                out.write(buffer,0,len);
            }
        }
        else
        {
            while (true)
            {
                len=in.read(buffer,0,bufferSize);
                if (len<0 )
                    break;
                out.write(buffer,0,len);
            }
        }
    }

    /* ------------------------------------------------------------------------------- */
    /* ------------------------------------------------------------------------------- */
    private static class Wrapper extends HttpServletRequestWrapper
    {
        String _encoding=__UTF8;
        MultiMap<Object> _params;

        /* ------------------------------------------------------------------------------- */
        /** Constructor.
         * @param request
         */
        public Wrapper(HttpServletRequest request, MultiMap map)
        {
            super(request);
            this._params=map;
        }

        /* ------------------------------------------------------------------------------- */
        /**
         * @see javax.servlet.ServletRequest#getContentLength()
         */
        @Override
        public int getContentLength()
        {
            return 0;
        }

        /* ------------------------------------------------------------------------------- */
        /**
         * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
         */
        @Override
        public String getParameter(String name)
        {
            Object o=_params.get(name);
            if (!(o instanceof byte[]) && LazyList.size(o)>0)
                o=LazyList.get(o,0);

            if (o instanceof byte[])
            {
                try
                {
                    return new String((byte[])o,_encoding);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (o!=null)
                return String.valueOf(o);
            return null;
        }

        /* ------------------------------------------------------------------------------- */
        /**
         * @see javax.servlet.ServletRequest#getParameterMap()
         */
        @Override
        public Map<String, String[]> getParameterMap()
        {
            Map<String, String[]> cmap = new HashMap<String,String[]>();

            for ( Object key : _params.keySet() )
            {
                String[] a = LazyList.toStringArray(getParameter((String)key));
                cmap.put((String)key,a);

            }

            return Collections.unmodifiableMap(cmap);
        }

        /* ------------------------------------------------------------------------------- */
        /**
         * @see javax.servlet.ServletRequest#getParameterNames()
         */
        @Override
        public Enumeration<String> getParameterNames()
        {
            return Collections.enumeration(_params.keySet());
        }

        /* ------------------------------------------------------------------------------- */
        /**
         * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
         */
        @Override
        public String[] getParameterValues(String name)
        {
            List l=_params.getValues(name);
            if (l==null || l.size()==0)
                return new String[0];
            String[] v = new String[l.size()];
            for (int i=0;i<l.size();i++)
            {
                Object o=l.get(i);
                if (o instanceof byte[])
                {
                    try
                    {
                        v[i]=new String((byte[])o,_encoding);
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                else if (o instanceof String)
                    v[i]=(String)o;
            }
            return v;
        }

        /* ------------------------------------------------------------------------------- */
        /**
         * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
         */
        @Override
        public void setCharacterEncoding(String enc)
                throws UnsupportedEncodingException
        {
            _encoding=enc;
        }
    }
}
