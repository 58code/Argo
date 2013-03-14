package com.bj58.argo.utils;

import com.bj58.argo.ArgoException;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;

/**
 * @author renjun
 */
public class PathUtils {
    private PathUtils() {}
    
    /**
     * 通过将给定路径名字符串转换为抽象路径名来创建一个新 File实例。如果给定字符串是空字符串，那么结果是空抽象路径名。
     * @param pathName 路径名字符串
     * @return 新 File实例
     */
    public static File newFile(String pathName) {
        return new File(pathName);
    }

    /**
     * 判断父类文件和子类文件间是否存在包含关系
     * @param parent 父类文件
     * @param child 子类文件
     * @return 若子类文件是父类文件目录下的文件，返回true;否则返回false
     */
    public static boolean contains(File parent, File child) {
        String parentName = conventPath(parent);
        String childName = conventPath(child);
        return childName.indexOf(parentName) == 0;
    }


    public static String relativePath(File parent, File child) {    	
        Preconditions.checkArgument(contains(parent, child));

        String parentName = conventPath(parent);
        String childName = conventPath(child);

        String relative = childName.substring(parentName.length());

        if (relative.length() == 0) return "/";

        return  "/" + relative.substring(0, relative.length() - 1);
    }


    /**
     * 规一化文件格式，结尾均以路径分隔符标识
     * @param file 待规一化文件
     * @return 规一化后的文件名 
     */
    static String conventPath(File file) {
        try {
            String path = file.getCanonicalPath();
            return path.endsWith(File.separator) ? path
                    : path + File.separator;
        } catch (IOException e) {
            throw ArgoException.raise(e);
        }
    }

    /**
     * 对两个URL路径进行拼装
     * @param parentUrl 父URL路径,可为空字符串
     * @param childUrl 子URL路径,可为空字符串
     * @return 拼装后URL路径
     */
    public static String combine(String parentUrl, String childUrl) {
        StringBuilder simplyParent = simplyStringBuilder(parentUrl);

        if (simplyParent.length() == 0 || '/' != simplyParent.charAt(simplyParent.length() - 1))
            simplyParent.append('/');

        String simplyChild = simplyWithoutPrefix(childUrl);

        return simplyParent.append(simplyChild).toString();
    }

    /**
     * 判断URL路径是否合法，判断逻辑参考{@link #simply(String)},并去掉以'/'开头的斜杠前缀
     * @param url 待判断URL路径
     * @return 去掉斜杠前缀的符合规范的URL路径字符串
     * 
     * @see #simply
     */
    public static String simplyWithoutPrefix(String url) {
        StringBuilder simply = simplyStringBuilder(url);
        
        if (simply.length() > 0 && '/' == simply.charAt(0))
            simply.deleteCharAt(0);

        return simply.toString();
    }

    /**
     * 判断URL路径是否合法，判断逻辑参考{@link #simply(String)},并去掉以'/'结尾的斜杠后缀
     * @param url 待判断URL路径
     * @return 去掉斜杠后缀的符合规范的URL路径字符串
     * 
     * @see #simply
     */
    public static String simplyWithoutSuffix(String url) {
        StringBuilder simply = simplyStringBuilder(url);
        if (simply.length() > 1 && '/' == simply.charAt(simply.length() - 1))
        	simply.deleteCharAt(simply.length() - 1);

        return simply.toString();
    }

    /**
     * 判断URL路径是否合法，并省略掉URL特殊字符后面的内容，同时进行规一划处理（将正反斜杠统一为正斜杠表示）
     * @param url 待判断URL路径
     * @return 符合规范的URL路径字符串
     */
    public static String simply(String url) {

        StringBuilder sb = simplyStringBuilder(url);

        return sb.toString();
    }



    private static StringBuilder simplyStringBuilder(String url) {
        StringBuilder sb = new StringBuilder(url.length());
        StringBuilder path = new StringBuilder();
        boolean illegalPath = true;
        for (int index = 0; index < url.length(); index++) {
            char c = url.charAt(index);

            switch (c) {
                case '/' :
                case '\\' :               // url 中不包含 "\"
                    if (path.length() == 0 && index == 0) {
                        sb.append(path)
                                .append('/');
                        continue;
                    }

                    if (illegalPath)
                        throwException(url);

                    sb.append(path)
                            .append('/'); // File.pathSeparatorChar;

                    path = new StringBuilder();
                    illegalPath = true;
                    break;

                case '.':
                    path.append('.');
                    break;

                default:

                    if (isBreakChar(c)) {
                        index = Integer.MAX_VALUE - 100;
                        break;
                    }

                    if (!isLegalChar(c))
                        throwException(url);

                    illegalPath = false;
                    path.append(c);
            }
        }

        if (illegalPath & path.length() > 0)
            throwException(url);
        else
            sb.append(path);
        return sb;
    }

    private static void throwException(String url) {
        throw ArgoException
                .newBuilder("Illegal URL path!")
                .addContextVariable("url", url)
                .build();
    }

    final static char[] legalChars = ("abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "`.~!@$%^&()-_=+[{]};', ").toCharArray();
    /**
     * 针对URL路径中的字符，判断是否合法字符
     * @param c 待判断字符
     * @return 若是非ASCII字符，直接认为是合法字符；若是ASCII字符，则排除不允许为文件命名的字符(如\/:*?"<>|)之外的所有字符都认为是合法字符；其他情况都返回false
     */
    private static boolean isLegalChar(char c) {
        if (c >= 128)
            return true;

        for (char legal : legalChars)
            if (c == legal)
                return true;

        return false;
    }

    /**
     * 判断是否是URL路径中的特殊字符，现在主要考虑'?'和'#'
     * @param c 待判断字符
     * @return 当待判断字符是'?'或者'#'时，返回true，否则返回false
     */
    //    char[] breakChars = new char[] {'?', '#'};
    private static boolean isBreakChar(char c) {
        return ('?' == c || '#' == c);
    }
}
