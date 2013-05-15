package com.bj58.argo.servlet;

import com.bj58.argo.Argo;
import com.bj58.argo.ArgoException;
import com.bj58.argo.client.SafeParameter;
import com.bj58.argo.client.Upload;
import com.bj58.argo.thirdparty.jetty.MultiMap;
import com.bj58.argo.thirdparty.jetty.MultiPartInputStreamParser;
import com.bj58.argo.thirdparty.jetty.UrlEncoded;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.*;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.io.CharStreams;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

/**
 * 封装的request请求，包括参数处理，文件上传的
 *
 * @author renjun
 */
public class ArgoRequest extends HttpServletRequestWrapper implements Closeable {

    private final HttpServletRequest request;
    private final MultipartConfigElement config;


    private Type type = Type.UNKNOWN;
    private Map<String, Collection<String>> queryStrings = null;
    private Map<String, Collection<String>> forms = ImmutableMap.of();
    private Map<String, Collection<String>> params = ImmutableMap.of();
    private Map<String, Collection<Upload>> uploadMap = ImmutableMap.of();

    private Map<String, Collection<Upload>> originParts = ImmutableMap.of();

    private Collection<Upload> uploads = ImmutableSet.of();

    private MultiPartInputStreamParser multiParser;


    private int maxFormKeys = 1000;  // 最大的key数量
    private int maxFormContentSize = 200000; // post form数据的最大尺寸，jetty 200k， tomcat 2M
    public static int bufferSize = 64*1024;



    /**
     * Constructs a request object wrapping the given request.
     *
     * @throws IllegalArgumentException
     *          if the request is null
     */
    public ArgoRequest(HttpServletRequest request, MultipartConfigElement config) {
        super(request);

        this.request = request;
        this.config = config;

    }


    @Override
    public Collection<Part> getParts() throws IOException, ServletException {

        parsePost();
        // TODO:协变
        return Collections2.transform(uploads, new Function<Upload, Part>() {
            @Override
            public Part apply(Upload input) {
                return input;
            }
        });
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return getUpload(name);
    }

    /* ------------------------------------------------------------------------------- */
    /**
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String name) {

        parsePost();

        return getParamsString(params, name, false);

    }


        /* ------------------------------------------------------------------------------- */
    /**
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    @Override
    public Map<String, String[]> getParameterMap()
    {
        parsePost();

        return Maps.transformValues(params, new Function<Collection<String>, String[]>() {
            @Override
            public String[] apply(Collection<String> input) {
                String[] values = new String[input.size()];
                return input.toArray(values);
            }
        });
    }

        /* ------------------------------------------------------------------------------- */
    /**
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    @Override
    public Enumeration<String> getParameterNames()
    {
        parsePost();

        return Collections.enumeration(params.keySet());
    }

        /* ------------------------------------------------------------------------------- */
    /**
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(String name)
    {
        parsePost();

        Collection<String> vs = params.get(name);
        return vs == null ? new String[0]
                : vs.toArray(new String[vs.size()]);
    }

    @Override
    public void close() throws IOException {

        if (Type.MULTI != type)
            return;

        if (multiParser != null)
            multiParser.deleteParts();
    }



    public Map<String, Collection<String>> queryStrings() {

        if (queryStrings != null)
            return queryStrings;

        MultiMap<String> params = new MultiMap<String>();
        String originQueryString = super.getQueryString();
        
        if(queryStrings != null){
        	
	        UrlEncoded.decodeTo(originQueryString, params, "UTF-8", maxFormKeys);
	
	        queryStrings = NullToEmptyMap.safeWrapper(params, getSafeParameter());
        }
        return queryStrings;

    }


    public String queryString(String name) {

        return getParamsString(queryStrings(), name, true);
    }

    public Map<String, Collection<String>> forms() {

        parsePost();

        return forms;
    }

    public String form(String name) {
        return getParamsString(forms(), name, true);
    }

    public Upload getUpload(String name) {

        parsePost();


        Collection<Upload> parts = uploadMap.get(name);
        return parts == null ? null
                : parts.size() == 0 ? null
                :  parts.iterator().next();
    }

    public Collection<Upload> uploads() {

        parsePost();
        return uploads;
    }


    private String getParamsString(Map<String, Collection<String>> params
            , String name, boolean nullToEmpty) {

        Collection<String> ps = params.get(name);
        return (ps == null || ps.size() == 0) ? nullToEmpty ? "" : null
                : ps.iterator().next();
    }



    private void parsePost() {
        if(Type.UNKNOWN != type)
            return;

        type = (!isPost(request)) ? Type.GET
                : isMultiPart(request) ? Type.MULTI : Type.POST;

        if (Type.GET == type)
            return;

        queryStrings();


        try {

            if (Type.MULTI == type)
                parsePostMulti();
            else
                parsePostUrlEncoded();

        } catch (Exception e) {
            throw ArgoException.raise(e);
        }

    }


    /**
     * application/x-www-form-urlencoded
     *
     */
    private void parsePostUrlEncoded() throws IOException {
        MultiMap<String> params = new MultiMap<String>();

        UrlEncoded.decodeTo(super.getInputStream(), params, "UTF-8", maxFormContentSize, maxFormKeys);

        forms = NullToEmptyMap.safeWrapper(params, getSafeParameter());
        
        Builder<String, Collection<String>> builder =  ImmutableMap.<String, Collection<String>>builder();
        
        if(queryStrings != null)
        	builder.putAll(queryStrings);
        
        if(forms != null)
        	builder.putAll(forms);
       
        this.params = builder.build();

    }

    private void parsePostMulti() throws IOException, ServletException {
        InputStream in = new BufferedInputStream(super.getInputStream());
        String content_type = super.getContentType();

        File tempDir = new File(config.getLocation());

        multiParser = new MultiPartInputStreamParser(in, content_type, config, tempDir);
        multiParser.setDeleteOnExit(true);

        Collection<Part> parts = multiParser.getParts();

        int keyCount = queryStrings.size();

        ImmutableMultimap.Builder<String, String> formMultiMapBuildBuilder = ImmutableMultimap.builder();
        ImmutableMultimap.Builder<String, Upload> partMultiMapBuilder = ImmutableMultimap.builder();
        ImmutableMultimap.Builder<String, Upload> originPartMultiMapBuilder = ImmutableMultimap.builder();
        ImmutableSet.Builder<Upload> partBuilder = ImmutableSet.builder();

        Iterator<Part> iterator = parts.iterator();
        while (iterator.hasNext() && (keyCount++) < maxFormKeys) {
            Part part = iterator.next();

            if (part == null)
                continue;
            MultiPartInputStreamParser.MultiPart mp = (MultiPartInputStreamParser.MultiPart) part;

            Upload upload = wrapPart(part, mp.getContentDispositionFilename());

            originPartMultiMapBuilder.put(part.getName(), upload);





            if (mp.getFile() != null) {
                partMultiMapBuilder.put(part.getName(), upload);
                partBuilder.add(upload);
            } else {
                String value = CharStreams.toString(
                        new InputStreamReader(part.getInputStream(), Charsets.UTF_8));

                formMultiMapBuildBuilder.put(part.getName(), value);
            }
        }

        this.forms = NullToEmptyMap.safeWrapper(
                formMultiMapBuildBuilder.build().asMap()
                , getSafeParameter());

        params = NullToEmptyMap.wrapper(
                ImmutableMap.<String, Collection<String>>builder()
                        .putAll(params)
                        .putAll(forms)
                        .build());

        this.uploadMap = NullToEmptyMap.wrapper(partMultiMapBuilder.build().asMap());
        this.originParts = NullToEmptyMap.wrapper(originPartMultiMapBuilder.build().asMap());
        this.uploads = partBuilder.build();

    }

    private boolean isPost(HttpServletRequest request) {
        return "post".equals(request.getMethod().toLowerCase());
    }

    private boolean isMultiPart(HttpServletRequest request) {

        String contentType = request.getContentType();

        return contentType != null
                && contentType.toLowerCase().startsWith("multipart/");

    }


        /* ------------------------------------------------------------------- */
    /** Copy Stream in to Stream out until EOF or exception.
     */
    private static void IoCopy(InputStream in, OutputStream out)
            throws IOException
    {
        IoCopy(in, out, -1);
    }

        /* ------------------------------------------------------------------- */
    /** Copy Stream in to Stream for byteCount bytes or until EOF or exception.
     */
    private static void IoCopy(InputStream in,
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

    private SafeParameter safeParameter = null;
    private SafeParameter getSafeParameter() {
        if (safeParameter != null)
            return safeParameter;

        safeParameter = Argo.instance.getInstance(SafeParameter.class);

        return safeParameter;

    }

    private Upload wrapPart(final Part part, final String filename) {
        return new Upload() {
            @Override
            public String getFileName() {
                return filename; // header: filename
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return part.getInputStream();
            }

            @Override
            public String getContentType() {
                return part.getContentType();
            }

            @Override
            public String getName() {
                return part.getName();
            }

            @Override
            public long getSize() {
                return part.getSize();
            }

            @Override
            public void write(String fileName) throws IOException {
                part.write(fileName);
            }

            @Override
            public void delete() throws IOException {
                part.delete();
            }

            @Override
            public String getHeader(String name) {
                return part.getHeader(name);
            }

            @Override
            public Collection<String> getHeaders(String name) {
                return part.getHeaders(name);
            }

            @Override
            public Collection<String> getHeaderNames() {
                return part.getHeaderNames();
            }
        };
    }

    private static enum Type {
        UNKNOWN,
        GET,
        POST,
        MULTI
    }



    private static class NullToEmptyMap<P, V extends Collection<P>>
            extends ForwardingMap<String, V>{

        public static NullToEmptyMap<String ,Collection<String>>
        safeWrapper(Map<String, ? extends Collection<String>> map , final SafeParameter safeParameter) {

            return wrapper(Maps.transformValues(map, new Function<Collection<String>, Collection<String>>() {
                @Override
                public Collection<String> apply(Collection<String> input) {
                    if (input == null)
                        return Collections.emptyList();

                    return Collections2.transform(input, new Function<String, String>() {
                        @Override
                        public String apply(String input) {
                            return safeParameter.encoding(input);
                        }
                    });
                }
            }));
        }

        public static <P, V extends Collection<P>> NullToEmptyMap<P, V>
        wrapper(Map<String, V> map) {
            return new NullToEmptyMap<P, V> (map);
        }

        private final Map<String, V> map;
        public NullToEmptyMap(Map<String, V> map) {
            this.map = map;
        }

        @Override
        protected Map<String, V> delegate() {
            return map;
        }

        @Override
        public V get(Object key) {
            V v = super.get(key);

            return v == null ? (V)Collections.emptyList() : v;
        }
    }

}
