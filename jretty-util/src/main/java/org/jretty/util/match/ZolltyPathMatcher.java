/* 
 * Copyright (C) 2014-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2014-6-02 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.match;

import java.util.ArrayList;
import java.util.List;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.ArrayUtils;
import org.jretty.util.Const;

/**
 * A high-performance path matching algorithm. (10 times faster than AntPathMather) 
 * <p>
 * 路径匹配算法 （URL路径、文件路径均可，但必须以'/'分割路径）
 * 
 * @author zollty
 * @since 2014-6-02
 * @see ZolltyPathMatcherTest 单元测试类
 */
public class ZolltyPathMatcher {

    private static final Logger LOG = LogFactory.getLogger(ZolltyPathMatcher.class);

    private final String pattern;
    private final List<MatchInfo> miList = new ArrayList<MatchInfo>();
    private final boolean notStartWithWildcard;
    
    private boolean notPattern;
    
    public ZolltyPathMatcher(final String pattern) {
        
        this.pattern = pattern.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
        this.notPattern = ! isPattern(pattern);
        this.check();
        
        if ( pattern.startsWith("*") || pattern.startsWith("**")) {
            this.notStartWithWildcard = false;
        } else {
            this.notStartWithWildcard = true;
        }
         
    }
    
    
    /**
     * Match the given <code>path</code> against the given <code>pattern</code>, 
     * test whether it matched.
     * @param path the path String to test (must split by '/')
     * @return <code>true</code> if the supplied <code>path</code> matched
     */
    public boolean isMatch(String path) {
        
        return null!=match(path);
    }


    /**
     * Match the given <code>path</code> against the given <code>pattern</code>,
     * return the matched values.
     * 
     * @param path the path String to test (must split by '/')
     * @return matched values in array
     */
    public List<String> match(String path) {

        if (null == path)
            return null;
        
        if(notPattern){
            if(pattern.equals(path)) {
                return new ArrayList<String>(0);
            }
            return null;
        }
        
        String src = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
        
        if ( notStartWithWildcard && !path.startsWith(miList.get(0).matchStr) ) {
            return null;
        }

        List<TempMatchValue> tempValueList = new ArrayList<TempMatchValue>();
        List<String> valueList = new ArrayList<String>();

        List<MatchFlag> tempMfList = new ArrayList<MatchFlag>(miList.size());
        for (MatchInfo ma : miList) {
            tempMfList.add(new MatchFlag(ma));
        }

        if (!this.isMatch(src, tempMfList, tempValueList)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Matched Failure. Pattern={}, Src={}", pattern, src);
            }
            return null;
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("Matched Success. Pattern={}, Src={}", pattern, src);
        }
        this.handleValue(valueList, tempValueList);
        return valueList;
    }

    private boolean isMatch(String src, List<MatchFlag> tempMfList, List<TempMatchValue> tempValueList) {
        MatchFlag mf;
        int inx;
        String tempStr;
        int[] b = new int[tempMfList.size() + 1];
        int i = 0;
        for (; i < tempMfList.size(); i++) {
            mf = tempMfList.get(i);
            inx = src.indexOf(mf.ma.matchStr, b[i]);
            while (inx != -1) {
                tempStr = src.substring(b[i], inx);
                if (LOG.isTraceEnabled()) {
                    LOG.trace(mf.ma.matchStr + "[" + tempStr + "]");
                }
                tempValueList.add(new TempMatchValue(mf, tempStr));
                if (mf.ma.matchType == 0 && tempStr.indexOf("/") != -1) { // 没匹配到，继续变换上一个index尝试，如果没有匹配则return
                    b[i + 1] = inx + mf.ma.matchStr.length();
                    b[i] = b[i + 1];
                    inx = -1;
                    break;
                }
                // OK，匹配到一个，重新定义src的寻址位置，继续下一个for循环matchStr
                b[i + 1] = inx + mf.ma.matchStr.length();
                b[i] = b[i + 1];
                if (i + 2 > tempMfList.size()) {
                    tempValueList.add(new TempMatchValue(new MatchFlag(null), src.substring(b[i + 1])));
                }
                break;
            }
            if (inx == -1) {
                --i; // 回溯
                if (--i < -1) {
                    return false;
                }
                continue;
            }
        }
        
        
        if (pattern.endsWith("**")) {
        }
        else if (pattern.endsWith("*")) {
            if (src.indexOf("/", b[i]) != -1) {
                return false;
            }
        }
        else if (tempValueList.get(tempValueList.size() - 1).value.length() != 0) {
            return false;
        }
        return true;
    }

    private void handleValue(List<String> valueList, List<TempMatchValue> tempValueList) {
        int size = tempValueList.size() - 1;
        TempMatchValue t1, t2;
        for (int i = 0; i < size; i++) {
            t1 = tempValueList.get(i);
            for (int j = i - 1; j >= 0; j--) {
                t2 = tempValueList.get(j);
                if (t2.mf.append) {
                    break;
                }
                if (t2.mf.equals(t1.mf)) {
                    if (canAppend(t1.mf, tempValueList)) {
                        t2.mf.append = true;
                    }
                    break;
                }
            }
        }
        List<TempMatchValue> tv = new ArrayList<TempMatchValue>();
        for (int i = 0; i < size; i++) {
            t1 = tempValueList.get(i);
            if (!t1.mf.used) {
                tv = new ArrayList<TempMatchValue>();
                tv.add(t1);
                for (int j = 0; j < size; j++) {
                    if (j != i) {
                        t2 = tempValueList.get(j);
                        if (t2.mf.equals(t1.mf)) {
                            tv.add(t2);
                        }
                    }
                }
                if (!t1.mf.append) {
                    valueList.add(tv.get(tv.size() - 1).value);
                }
                else {
                    StringBuilder val = new StringBuilder();
                    String mstr = t1.mf.ma.matchStr;
                    for (int k = 0; k < tv.size(); k++) {
                        if (k != tv.size() - 1) {
                            val.append(tv.get(k).value).append(mstr);
                        }
                        else {
                            val.append(tv.get(k).value);
                        }
                    }
                    valueList.add(val.toString());
                }
                t1.mf.used = true;
            }
        }

//        if (!pattern.startsWith("*") && !pattern.startsWith("**")) {
//            valueList.remove(0);
//        }
        if( notStartWithWildcard ) {
            valueList.remove(0);
        }
        
        if (pattern.endsWith("*") || pattern.endsWith("**")) {
            valueList.add(tempValueList.get(tempValueList.size() - 1).value);
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("ValueList={}", valueList.toString());
        }
    }

    private boolean canAppend(MatchFlag mf, List<TempMatchValue> tempValueList) {
        int size = tempValueList.size() - 1;
        TempMatchValue t1, t2;
        for (int i = size - 1; i > 0; i--) {
            t1 = tempValueList.get(i);
            if (t1.mf.equals(mf)) {
                t2 = tempValueList.get(i - 1);
                if (t2.mf.append) {
                    return false;
                }
            }
        }
        return true;
    }

    private final void check() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("begin init pattern[{}]", pattern);
        }
        char[] pchar = pattern.toCharArray();
        pchar = ArrayUtils.add(pchar, '$');
        int p1 = -1, p2 = -1;
        String val1;
        int mf = -1; // mf标记，1代表“**匹配”, 0代表“*匹配”
        for (int i = 0; i < pchar.length - 1; i++) {
            if (pchar[i] == '*' && pchar[i + 1] == '*') {
                if (p1 == -1) {
                    p1 = i + 2; // System.out.println(p1+"---p1");
                    i++;
                }
                else {
                    p2 = i;
                    val1 = pattern.substring(p1, p2);
                    if (p1 > 1 && pchar[p1 - 2] == '*' && pchar[p1 - 1] == '*') {
                        mf = 1;
                    }
                    else {
                        mf = 0;
                    }
                    // System.out.println(new MatchFlag(mf, val1));
                    miList.add(new MatchInfo(mf, val1));
                    p1 = -1;
                    i++;
                }
            }
            else if (pchar[i] == '*' && pchar[i + 1] != '*') {
                if (p1 == -1) {
                    p1 = i + 1; // System.out.println(p1+"---p1");
                }
                else {
                    p2 = i;
                    val1 = pattern.substring(p1, p2);
                    if (p1 > 1 && pchar[p1 - 2] == '*' && pchar[p1 - 1] == '*') {
                        mf = 1;
                    }
                    else {
                        mf = 0;
                    }
                    // System.out.println(new MatchFlag(mf, val1));
                    miList.add(new MatchInfo(mf, val1));
                    p1 = -1;
                }
            }
            else if (pchar[i + 1] == '$' && p1 != -1) { // !a
                p2 = i;
                val1 = pattern.substring(p1, p2 + 1);
                if (p1 > 1 && pchar[p1 - 2] == '*' && pchar[p1 - 1] == '*') {
                    mf = 1;
                }
                else {
                    mf = 0;
                }
                // System.out.println(new MatchFlag(mf, val1));
                miList.add(new MatchInfo(mf, val1));
                p1 = -1;
            }
            else if (p1 == -1) {
                p1 = i;
            }
        }
        if (p1 != -1) {
            p2 = pchar.length - 1;
            val1 = pattern.substring(p1, p2);
            if (p1 > 1 && pchar[p1 - 2] == '*' && pchar[p1 - 1] == '*') {
                mf = 1;
            }
            else {
                mf = 0;
            }
            // System.out.println(new MatchFlag(mf, val1));
            miList.add(new MatchInfo(mf, val1));
            p1 = -1;
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("MatchInfo List = {}", miList.toString());
        }
    }
    
    
    /**
     * 判断<code>pattern</code>是否为通配Pattern格式
     */
    public static boolean isPattern(String pattern) {
        return (pattern.indexOf('*') != -1);
    }

    /**
     * 判断两个URL Pattern 是否有包含或重叠。
     */
    public static boolean isTwoPatternSimilar(String pattern1, String pattern2) {
        return isTwoPatternSimilar(new ZolltyPathMatcher(pattern1), new ZolltyPathMatcher(pattern2));
    }
    
    
    /**
     * Match the given <code>path</code> against the given <code>pattern</code>, 
     * test whether it matched.
     * 
     * @param pattern the pattern to match against
     * @param path the path String to test
     * @return <code>true</code> if the supplied <code>path</code> matched,
     * <code>false</code> if it didn't
     */
    public static boolean match(String pattern, String path){
        return new ZolltyPathMatcher(pattern).isMatch(path);
    }
    

    /**
     * 判断两个URL Pattern 是否有包含或重叠。
     * <p>
     * 注意： 当两个Pattern存在交集时，该方法并不能全面检测出URI的重复匹配。  <br>
     * 
     * 例如：  <br>
     * 
     * Pattern1: \a**c\*\*\b Pattern2: \a\*bc\**\b <br>
     * 
     * URI: \a\bc\k\k\b - ALL \acc\k\k\b - 1 \a\bc\k\k\k\b - 2 <br>
     * 
     * 这种情况很难检测出来。
     */
    public static boolean isTwoPatternSimilar(ZolltyPathMatcher pattern1, ZolltyPathMatcher pattern2) {

        String tmpPattern;

        // 首先，用pattern2当做“PathMatcher”去匹配pattern1

        // 比如 已存在了/app/*/*，那么当前如果是 /app/user/*，则是冲突的
        tmpPattern = pattern1.getPattern().replace("**", "a");
        tmpPattern = tmpPattern.replace('*', 'a');
        if (pattern2.isMatch(tmpPattern)) { // 匹配到了
            return true;
        }
        // 再试一次
        tmpPattern = pattern1.getPattern().replace("**", "a/a");
        tmpPattern = tmpPattern.replace('*', 'a');
        if (pattern2.isMatch(tmpPattern)) { // 匹配到了
            return true;
        }

        // 其次，反过来，用pattern1当做“PathMatcher”去匹配pattern2

        tmpPattern = pattern2.getPattern().replace("**", "a");
        tmpPattern = tmpPattern.replace('*', 'a');
        if (pattern1.isMatch(tmpPattern)) { // 匹配到了
            return true;
        }
        // 再试一次
        tmpPattern = pattern2.getPattern().replace("**", "a/a");
        tmpPattern = tmpPattern.replace('*', 'a');
        if (pattern1.isMatch(tmpPattern)) { // 匹配到了
            return true;
        }

        return false;
    }

    private static class MatchInfo {
        /** 匹配类型, 1代表"**匹配", 0代表"*匹配" */
        int matchType;
        String matchStr;

        public MatchInfo(int matchType, String matchStr) {
            this.matchType = matchType;
            this.matchStr = matchStr;
        }

        public String toString() {
            return "MatchInfo(matchStr=" + matchStr + ", type=" + matchType + ")";
        }
    }

    private static class MatchFlag {
        MatchInfo ma;
        boolean append;
        boolean used;

        public MatchFlag(MatchInfo ma) {
            if (ma != null) {
                this.ma = new MatchInfo(ma.matchType, ma.matchStr);
            }
            else {
                this.ma = null;
            }
        }
    }

    private static class TempMatchValue {
        MatchFlag mf;
        String value;

        public TempMatchValue(MatchFlag mf, String value) {
            super();
            this.mf = mf;
            this.value = value;
        }
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " - " + pattern;
    }
    
}
