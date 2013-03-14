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
 package com.bj58.argo.thirdparty;


import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


 /**
  * Ant-style的路径匹配工具类
  *
  * @author Service Platform Architecture Team (spat@58.com)
  */
 public class AntPathMatcher implements PathMatcher {

     private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?\\}");

     /** Default path separator: "/" */
     public static final String DEFAULT_PATH_SEPARATOR = "/";

     private String pathSeparator = DEFAULT_PATH_SEPARATOR;


     /** Set the path separator to use for pattern parsing. Default is "/", as in Ant. */
     public void setPathSeparator(String pathSeparator) {
         this.pathSeparator = (pathSeparator != null ? pathSeparator : DEFAULT_PATH_SEPARATOR);
     }


     public boolean isPattern(String path) {
         return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
     }

     public boolean match(String pattern, String path) {
         return doMatch(pattern, path, true, null);
     }

     public boolean matchStart(String pattern, String path) {
         return doMatch(pattern, path, false, null);
     }


     /**
      * Actually match the given <code>path</code> against the given <code>pattern</code>.
      * @param pattern the pattern to match against
      * @param path the path String to test
      * @param fullMatch whether a full pattern match is required (else a pattern match
      * as far as the given base path goes is sufficient)
      * @return <code>true</code> if the supplied <code>path</code> matched, <code>false</code> if it didn't
      */
     public boolean doMatch(String pattern, String path, boolean fullMatch,
             Map<String, String> uriTemplateVariables) {

         if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
             return false;
         }

         String[] pattDirs = StringUtil.tokenizeToStringArray(pattern, this.pathSeparator);
         String[] pathDirs = StringUtil.tokenizeToStringArray(path, this.pathSeparator);

         int pattIdxStart = 0;
         int pattIdxEnd = pattDirs.length - 1;
         int pathIdxStart = 0;
         int pathIdxEnd = pathDirs.length - 1;

         // Match all elements up to the first **
         while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
             String patDir = pattDirs[pattIdxStart];
             if ("**".equals(patDir)) {
                 break;
             }
             if (!matchStrings(patDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
                 return false;
             }
             pattIdxStart++;
             pathIdxStart++;
         }

         if (pathIdxStart > pathIdxEnd) {
             // Path is exhausted, only match if rest of pattern is * or **'s
             if (pattIdxStart > pattIdxEnd) {
                 return (pattern.endsWith(this.pathSeparator) ? path.endsWith(this.pathSeparator) :
                         !path.endsWith(this.pathSeparator));
             }
             if (!fullMatch) {
                 return true;
             }
             if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(this.pathSeparator)) {
                 return true;
             }
             for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                 if (!pattDirs[i].equals("**")) {
                     return false;
                 }
             }
             return true;
         }
         else if (pattIdxStart > pattIdxEnd) {
             // String not exhausted, but pattern is. Failure.
             return false;
         }
         else if (!fullMatch && "**".equals(pattDirs[pattIdxStart])) {
             // Path start definitely matches due to "**" part in pattern.
             return true;
         }

         // up to last '**'
         while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
             String patDir = pattDirs[pattIdxEnd];
             if (patDir.equals("**")) {
                 break;
             }
             if (!matchStrings(patDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
                 return false;
             }
             pattIdxEnd--;
             pathIdxEnd--;
         }
         if (pathIdxStart > pathIdxEnd) {
             // String is exhausted
             for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                 if (!pattDirs[i].equals("**")) {
                     return false;
                 }
             }
             return true;
         }

         while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
             int patIdxTmp = -1;
             for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
                 if (pattDirs[i].equals("**")) {
                     patIdxTmp = i;
                     break;
                 }
             }
             if (patIdxTmp == pattIdxStart + 1) {
                 // '**/**' situation, so skip one
                 pattIdxStart++;
                 continue;
             }
             // Find the pattern between padIdxStart & padIdxTmp in str between
             // strIdxStart & strIdxEnd
             int patLength = (patIdxTmp - pattIdxStart - 1);
             int strLength = (pathIdxEnd - pathIdxStart + 1);
             int foundIdx = -1;

             strLoop:
             for (int i = 0; i <= strLength - patLength; i++) {
                 for (int j = 0; j < patLength; j++) {
                     String subPat = pattDirs[pattIdxStart + j + 1];
                     String subStr = pathDirs[pathIdxStart + i + j];
                     if (!matchStrings(subPat, subStr, uriTemplateVariables)) {
                         continue strLoop;
                     }
                 }
                 foundIdx = pathIdxStart + i;
                 break;
             }

             if (foundIdx == -1) {
                 return false;
             }

             pattIdxStart = patIdxTmp;
             pathIdxStart = foundIdx + patLength;
         }

         for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
             if (!pattDirs[i].equals("**")) {
                 return false;
             }
         }

         return true;
     }

     /**
      * Tests whether or not a string matches against a pattern. The pattern may contain two special characters:<br> '*'
      * means zero or more characters<br> '?' means one and only one character
      * @param pattern pattern to match against. Must not be <code>null</code>.
      * @param str string which must be matched against the pattern. Must not be <code>null</code>.
      * @return <code>true</code> if the string matches against the pattern, or <code>false</code> otherwise.
      */
     private boolean matchStrings(String pattern, String str, Map<String, String> uriTemplateVariables) {
         AntPathStringMatcher matcher = new AntPathStringMatcher(pattern, str, uriTemplateVariables);
         return matcher.matchStrings();
     }

     /**
      * Given a pattern and a full path, determine the pattern-mapped part. <p>For example: <ul>
      * <li>'<code>/docs/cvs/commit.html</code>' and '<code>/docs/cvs/commit.html</code> -> ''</li>
      * <li>'<code>/docs/*</code>' and '<code>/docs/cvs/commit</code> -> '<code>cvs/commit</code>'</li>
      * <li>'<code>/docs/cvs/*.html</code>' and '<code>/docs/cvs/commit.html</code> -> '<code>commit.html</code>'</li>
      * <li>'<code>/docs/**</code>' and '<code>/docs/cvs/commit</code> -> '<code>cvs/commit</code>'</li>
      * <li>'<code>/docs/**\/*.html</code>' and '<code>/docs/cvs/commit.html</code> -> '<code>cvs/commit.html</code>'</li>
      * <li>'<code>/*.html</code>' and '<code>/docs/cvs/commit.html</code> -> '<code>docs/cvs/commit.html</code>'</li>
      * <li>'<code>*.html</code>' and '<code>/docs/cvs/commit.html</code> -> '<code>/docs/cvs/commit.html</code>'</li>
      * <li>'<code>*</code>' and '<code>/docs/cvs/commit.html</code> -> '<code>/docs/cvs/commit.html</code>'</li> </ul>
      * <p>Assumes that {@link #match} returns <code>true</code> for '<code>pattern</code>' and '<code>path</code>', but
      * does <strong>not</strong> enforce this.
      */
     public String extractPathWithinPattern(String pattern, String path) {
         String[] patternParts = StringUtil.tokenizeToStringArray(pattern, this.pathSeparator);
         String[] pathParts = StringUtil.tokenizeToStringArray(path, this.pathSeparator);

         StringBuilder builder = new StringBuilder();

         // Add any path parts that have a wildcarded pattern part.
         int puts = 0;
         for (int i = 0; i < patternParts.length; i++) {
             String patternPart = patternParts[i];
             if ((patternPart.indexOf('*') > -1 || patternPart.indexOf('?') > -1) && pathParts.length >= i + 1) {
                 if (puts > 0 || (i == 0 && !pattern.startsWith(this.pathSeparator))) {
                     builder.append(this.pathSeparator);
                 }
                 builder.append(pathParts[i]);
                 puts++;
             }
         }

         // Append any trailing path parts.
         for (int i = patternParts.length; i < pathParts.length; i++) {
             if (puts > 0 || i > 0) {
                 builder.append(this.pathSeparator);
             }
             builder.append(pathParts[i]);
         }

         return builder.toString();
     }

     public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
         Map<String, String> variables = new LinkedHashMap<String, String>();
         boolean result = doMatch(pattern, path, true, variables);
         Preconditions.checkState(result, "Pattern \"" + pattern + "\" is not a match for \"" + path + "\"");
         return variables;
     }

     /**
      * Combines two patterns into a new pattern that is returned.
      * <p>This implementation simply concatenates the two patterns, unless the first pattern
      * contains a file extension match (such as {@code *.html}. In that case, the second pattern
      * should be included in the first, or an {@code IllegalArgumentException} is thrown.
      * <p>For example: <table>
      * <tr><th>Pattern 1</th><th>Pattern 2</th><th>Result</th></tr> <tr><td>/hotels</td><td>{@code
      * null}</td><td>/hotels</td></tr> <tr><td>{@code null}</td><td>/hotels</td><td>/hotels</td></tr>
      * <tr><td>/hotels</td><td>/bookings</td><td>/hotels/bookings</td></tr> <tr><td>/hotels</td><td>bookings</td><td>/hotels/bookings</td></tr>
      * <tr><td>/hotels/*</td><td>/bookings</td><td>/hotels/bookings</td></tr> <tr><td>/hotels/&#42;&#42;</td><td>/bookings</td><td>/hotels/&#42;&#42;/bookings</td></tr>
      * <tr><td>/hotels</td><td>{hotel}</td><td>/hotels/{hotel}</td></tr> <tr><td>/hotels/*</td><td>{hotel}</td><td>/hotels/{hotel}</td></tr>
      * <tr><td>/hotels/&#42;&#42;</td><td>{hotel}</td><td>/hotels/&#42;&#42;/{hotel}</td></tr>
      * <tr><td>/*.html</td><td>/hotels.html</td><td>/hotels.html</td></tr> <tr><td>/*.html</td><td>/hotels</td><td>/hotels.html</td></tr>
      * <tr><td>/*.html</td><td>/*.txt</td><td>IllegalArgumentException</td></tr> </table>
      * @param pattern1 the first pattern
      * @param pattern2 the second pattern
      * @return the combination of the two patterns
      * @throws IllegalArgumentException when the two patterns cannot be combined
      */
     public String combine(String pattern1, String pattern2) {
         if (!StringUtil.hasText(pattern1) && !StringUtil.hasText(pattern2)) {
             return "";
         }
         else if (!StringUtil.hasText(pattern1)) {
             return pattern2;
         }
         else if (!StringUtil.hasText(pattern2)) {
             return pattern1;
         }
         else if (match(pattern1, pattern2)) {
             return pattern2;
         }
         else if (pattern1.endsWith("/*")) {
             if (pattern2.startsWith("/")) {
                 // /hotels/* + /booking -> /hotels/booking
                 return pattern1.substring(0, pattern1.length() - 1) + pattern2.substring(1);
             }
             else {
                 // /hotels/* + booking -> /hotels/booking
                 return pattern1.substring(0, pattern1.length() - 1) + pattern2;
             }
         }
         else if (pattern1.endsWith("/**")) {
             if (pattern2.startsWith("/")) {
                 // /hotels/** + /booking -> /hotels/**/booking
                 return pattern1 + pattern2;
             }
             else {
                 // /hotels/** + booking -> /hotels/**/booking
                 return pattern1 + "/" + pattern2;
             }
         }
         else {
             int dotPos1 = pattern1.indexOf('.');
             if (dotPos1 == -1) {
                 // simply concatenate the two patterns
                 if (pattern1.endsWith("/") || pattern2.startsWith("/")) {
                     return pattern1 + pattern2;
                 }
                 else {
                     return pattern1 + "/" + pattern2;
                 }
             }
             String fileName1 = pattern1.substring(0, dotPos1);
             String extension1 = pattern1.substring(dotPos1);
             String fileName2;
             String extension2;
             int dotPos2 = pattern2.indexOf('.');
             if (dotPos2 != -1) {
                 fileName2 = pattern2.substring(0, dotPos2);
                 extension2 = pattern2.substring(dotPos2);
             }
             else {
                 fileName2 = pattern2;
                 extension2 = "";
             }
             String fileName = fileName1.endsWith("*") ? fileName2 : fileName1;
             String extension = extension1.startsWith("*") ? extension2 : extension1;

             return fileName + extension;
         }
     }

     /**
      * Given a full path, returns a {@link java.util.Comparator} suitable for sorting patterns in order of explicitness.
      * <p>The returned <code>Comparator</code> will {@linkplain java.util.Collections#sort(java.util.List,
      * java.util.Comparator) sort} a list so that more specific patterns (without uri templates or wild cards) come before
      * generic patterns. So given a list with the following patterns: <ol> <li><code>/hotels/new</code></li>
      * <li><code>/hotels/{hotel}</code></li> <li><code>/hotels/*</code></li> </ol> the returned comparator will sort this
      * list so that the order will be as indicated.
      * <p>The full path given as parameter is used to test for exact matches. So when the given path is {@code /hotels/2},
      * the pattern {@code /hotels/2} will be sorted before {@code /hotels/1}.
      * @param path the full path to use for comparison
      * @return a comparator capable of sorting patterns in order of explicitness
      */
     public Comparator<String> getPatternComparator(String path) {
         return new AntPatternComparator(path);
     }


     private static class AntPatternComparator implements Comparator<String> {

         private final String path;

         private AntPatternComparator(String path) {
             this.path = path;
         }

         public int compare(String pattern1, String pattern2) {
             if (pattern1 == null && pattern2 == null) {
                 return 0;
             }
             else if (pattern1 == null) {
                 return 1;
             }
             else if (pattern2 == null) {
                 return -1;
             }
             boolean pattern1EqualsPath = pattern1.equals(path);
             boolean pattern2EqualsPath = pattern2.equals(path);
             if (pattern1EqualsPath && pattern2EqualsPath) {
                 return 0;
             }
             else if (pattern1EqualsPath) {
                 return -1;
             }
             else if (pattern2EqualsPath) {
                 return 1;
             }
             int wildCardCount1 = getWildCardCount(pattern1);
             int wildCardCount2 = getWildCardCount(pattern2);

             int bracketCount1 = StringUtil.countOccurrencesOf(pattern1, "{");
             int bracketCount2 = StringUtil.countOccurrencesOf(pattern2, "{");

             int totalCount1 = wildCardCount1 + bracketCount1;
             int totalCount2 = wildCardCount2 + bracketCount2;

             if (totalCount1 != totalCount2) {
                 return totalCount1 - totalCount2;
             }

             int pattern1Length = getPatternLength(pattern1);
             int pattern2Length = getPatternLength(pattern2);

             if (pattern1Length != pattern2Length) {
                 return pattern2Length - pattern1Length;
             }

             if (wildCardCount1 < wildCardCount2) {
                 return -1;
             }
             else if (wildCardCount2 < wildCardCount1) {
                 return 1;
             }

             if (bracketCount1 < bracketCount2) {
                 return -1;
             }
             else if (bracketCount2 < bracketCount1) {
                 return 1;
             }

             return 0;
         }

         private int getWildCardCount(String pattern) {
             if (pattern.endsWith(".*")) {
                 pattern = pattern.substring(0, pattern.length() - 2);
             }
             return StringUtil.countOccurrencesOf(pattern, "*");
         }

         /**
          * Returns the length of the given pattern, where template variables are considered to be 1 long.
          */
         private int getPatternLength(String pattern) {
             Matcher m = VARIABLE_PATTERN.matcher(pattern);
             return m.replaceAll("#").length();
         }
     }

 }
