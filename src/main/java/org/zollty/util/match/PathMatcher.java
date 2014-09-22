package org.zollty.util.match;

import java.util.List;

public interface PathMatcher {
    
    /**
     * Match the given <code>path</code> against the given <code>pattern</code>,
     * according to this PathMatcher's matching strategy.
     * @param path the path String to test
     * @return <code>true</code> if the supplied <code>path</code> matched
     */
    boolean isMatch(String path);
    
    /**
     * Match the given <code>path</code> against the given <code>pattern</code>,
     * according to this PathMatcher's matching strategy.
     * @param path the path String to test
     * @return matched values
     */
    List<String> match(String path);

}
